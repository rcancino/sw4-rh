package com.luxsoft.sw4.rh

import static org.springframework.http.HttpStatus.*

import com.luxsoft.sw4.Autorizacion;

import grails.plugin.springsecurity.annotation.Secured;
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN','RH_USER'])
@Transactional(readOnly = true)
class VacacionesController {
	
	def index(Integer max) {
		params.max = Math.min(max ?: 15, 100)
		[vacacionesList:Vacaciones.list(params), vacacionesTotalCount: Vacaciones.count()]
	}
	
	def create() {
		[vacacionesInstance:new Vacaciones(solicitud:new Date())]
	}
	
	@Transactional
	def save(Vacaciones vacacionesInstance) {
		if(vacacionesInstance==null) {
			notFound()
			return
		}
		if(vacacionesInstance.hasErrors()) {
			render view:'create',model:[vacacionesInstance:vacacionesInstance]
		}
		vacacionesInstance.save flush:true
		flash.message="Solicitud generada: "+vacacionesInstance.id
		//redirect action:'index'
		respond vacacionesInstance,[view:'edit']
	}
	
	def edit(Vacaciones vacacionesInstance) {
		if(vacacionesInstance==null) {
			notFound()
			return
		}
		[vacacionesInstance:vacacionesInstance]
	}
	
	@Transactional
	def update(Vacaciones vacacionesInstance) {
		if(vacacionesInstance==null) {
			notFound()
			return
		}
		if(vacacionesInstance.hasErrors()) {
			render view:'edit',model:[vacacionesInstance:vacacionesInstance]
		}
		vacacionesInstance.save flush:true
		flash.message="Solicitud de vacaciones actualizada: "+vacacionesInstance.id
		redirect action:'index'
	}
	
	@Transactional
	def agregarFecha(Vacaciones vacacionesInstance) {
		
		if(vacacionesInstance==null) {
			notFound()
			return
		}
		def dia=params.date('fecha', 'dd/MM/yyyy')
		if(dia) {
			vacacionesInstance.addToDias(dia)
			vacacionesInstance.save flush:true
			flash.message="Fecha agregada"
		}
		respond vacacionesInstance,[view:'edit']
	}
	
	@Transactional
	def eliminarFecha(Vacaciones vacacionesInstance) {
		def dia=params.date('fecha', 'dd/MM/yyyy')
		if(dia) {
			vacacionesInstance.removeFromDias(dia)
			vacacionesInstance.save flush:true
			flash.message="Fecha eliminada"
		}
		respond vacacionesInstance,[view:'edit']
	}
	
	@Transactional
	def autorizar(Vacaciones vacacionesInstance) {
		
		def comentario=params.comentarioAutorizacion
		if(comentario) {
			def aut=new Autorizacion(
				autorizo:getAuthenticatedUser(),
				descripcion:comentario,
				modulo:'RH',
				tipo:'VACACIONES')
			aut.save failOnError:true
			vacacionesInstance.autorizacion=aut
			//vacacionesInstance.save flush:true
		}
		respond vacacionesInstance,[view:'edit']
	}
	
	@Transactional
	def cancelarAutorizacion(Vacaciones vacacionesInstance) {
		if(vacacionesInstance==null) {
			notFound()
			return
		}
		if(vacacionesInstance.autorizacion) {
			def aut=vacacionesInstance.autorizacion
			vacacionesInstance.autorizacion=null
			aut.delete flush:true
			
		}
		respond vacacionesInstance,[view:'edit']
	}
	
	@Transactional
	def delete(Vacaciones vacacionesInstance) {
		if(vacacionesInstance==null) {
			notFound()
			return
		}
		vacacionesInstance.delete flush:true
		flash.message="Solicitud $vacacionesInstance.id eliminada"
		redirect action:'index'
	}
	protected void notFound() {
		request.withFormat {
			form multipartForm {
				flash.message = message(code: 'default.not.found.message'
					, args: [message(code: 'vacacinesInstance.label', default: 'Vacaciones'), params.id])
				redirect action: "index", method: "GET"
			}
			'*'{ render status: NOT_FOUND }
		}
	}
    
}
