package com.luxsoft.sw4.rh

import static org.springframework.http.HttpStatus.*

import com.luxsoft.sw4.Autorizacion;

import grails.plugin.springsecurity.annotation.Secured;
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN','RH_USER'])
@Transactional(readOnly = true)
class IncentivoController {
	

	def index(Integer max) {
		params.max = Math.min(max ?: 15, 100)
		[incentivoList:Incentivo.list(params), incentivoTotalCount: Incentivo.count()]
	}

	def create() {
		[incentivoInstance:new Incentivo(fecha:new Date())]
	}

	@Transactional
	def save(Incentivo incentivoInstance) {
		if(incentivoInstance==null) {
			notFound()
			return
		}
		if(incentivoInstance.hasErrors()) {
			render view:'create',model:[incentivoInstance:incentivoInstance]
		}
		incentivoInstance.save flush:true
		flash.message="Solicitud generada: "+incentivoInstance.id
		//redirect action:'index'
		respond incentivoInstance,[view:'edit']
	}

	def edit(Incentivo incentivoInstance) {
		if(incentivoInstance==null) {
			notFound()
			return
		}
		[incentivoInstance:incentivoInstance]
	}

	@Transactional
	def update(Incentivo incentivoInstance) {
		if(incentivoInstance==null) {
			notFound()
			return
		}
		if(incentivoInstance.hasErrors()) {
			render view:'edit',model:[incentivoInstance:incentivoInstance]
		}
		incentivoInstance.save flush:true
		flash.message="Solicitud de incentivo actualizada: "+incentivoInstance.id
		redirect action:'index'
	}

	@Transactional
	def autorizar(Incentivo incentivoInstance) {

		def comentario=params.comentarioAutorizacion
		if(comentario) {
			def aut=new Autorizacion(
					autorizo:getAuthenticatedUser(),
					descripcion:comentario,
					modulo:'RH',
					tipo:'INCENTIVO')
			aut.save failOnError:true
			incentivoInstance.autorizacion=aut
			//incentivoInstance.save flush:true
		}
		respond incentivoInstance,[view:'edit']
	}

	@Transactional
	def cancelarAutorizacion(Incentivo incentivoInstance) {
		if(incentivoInstance==null) {
			notFound()
			return
		}
		if(incentivoInstance.autorizacion) {
			def aut=incentivoInstance.autorizacion
			incentivoInstance.autorizacion=null
			aut.delete flush:true

		}
		respond incentivoInstance,[view:'edit']
	}

	@Transactional
	def delete(Incentivo incentivoInstance) {
		if(incentivoInstance==null) {
			notFound()
			return
		}
		incentivoInstance.delete flush:true
		flash.message="Solicitud $incentivoInstance.id eliminada"
		redirect action:'index'
	}
	protected void notFound() {
		request.withFormat {
			form multipartForm {
				flash.message = message(code: 'default.not.found.message'
				, args: [
					message(code: 'vacacinesInstance.label', default: 'Incentivo'),
					params.id
				])
				redirect action: "index", method: "GET"
			}
			'*'{ render status: NOT_FOUND }
		}
	}

}
