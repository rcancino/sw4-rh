package com.luxsoft.sw4.rh

import static org.springframework.http.HttpStatus.*

import com.luxsoft.sw4.Autorizacion

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN','RH_USER'])
@Transactional(readOnly = true)
class IncidenciaController {
    
	
	def index(Long max) {
		def list=Incapacidad.findAll(
			"from Incidencia i where ( year(i.fechaInicial)=? or year(i.fechaFinal)=? ) order by i.dateCreated desc"
			,[session.ejercicio,session.ejercicio])
		[incidenciaList:list]
	}
	
	def create() {
		[incidenciaInstance:new Incidencia(fecha:new Date())]
	}
	
	@Transactional
	def save(Incidencia incidenciaInstance) {
		if(incidenciaInstance==null) {
			notFound()
			return
		}
		if(incidenciaInstance.hasErrors()) {
			render view:'create',model:[incidenciaInstance:incidenciaInstance]
		}
		incidenciaInstance.save flush:true
		flash.message="Incidencia generada: "+incidenciaInstance.id
		respond incidenciaInstance,[view:'edit']
		redirect action:'index',params:[tipo:incidenciaInstance.empleado.salario.periodicidad]
	}
	
	@Transactional
	def edit(Incidencia incidenciaInstance) {
		if(incidenciaInstance==null) {
			notFound()
			return
		}
		[incidenciaInstance:incidenciaInstance,tipo:incidenciaInstance.empleado.salario.periodicidad]
	}
	
	@Transactional
	def update(Incidencia incidenciaInstance) {
		if(incidenciaInstance==null) {
			notFound()
			return
		}
		if(incidenciaInstance.hasErrors()) {
			render view:'edit',model:[incidenciaInstance:incidenciaInstance]
		}
		incidenciaInstance.save flush:true
		flash.message="Incidencia actualizada: "+incidenciaInstance.id
		redirect action:'index'
	}
	
	@Transactional
	def autorizar(Incidencia incidenciaInstance) {
		
		def comentario=params.comentarioAutorizacion
		if(comentario) {
			def aut=new Autorizacion(
				autorizo:getAuthenticatedUser(),
				descripcion:comentario,
				modulo:'RH',
				tipo:'INCIDENCIA')
			aut.save failOnError:true
			incidenciaInstance.autorizacion=aut
		}
		respond incidenciaInstance,[view:'edit',model:[tipo:incidenciaInstance.empleado.salario.periodicidad]]
	}
	
	@Transactional
	def cancelarAutorizacion(Incidencia incidenciaInstance) {
		if(incidenciaInstance==null) {
			notFound()
			return
		}
		if(incidenciaInstance.autorizacion) {
			def aut=incidenciaInstance.autorizacion
			incidenciaInstance.autorizacion=null
			aut.delete flush:true
			
		}
		respond incidenciaInstance,[view:'edit',model:[tipo:incidenciaInstance.empleado.salario.periodicidad]]
	}
	
	@Transactional
	def delete(Incidencia incidenciaInstance) {
		if(incidenciaInstance==null) {
			notFound()
			return
		}
		incidenciaInstance.delete flush:true
		flash.message="Incidencia $incidenciaInstance.id eliminada"
		redirect action:'index',params:[tipo:incidenciaInstance.empleado.salario.periodicidad]
	}
	
	protected void notFound() {
		request.withFormat {
			form multipartForm {
				flash.message = message(code: 'default.not.found.message'
					, args: [message(code: 'incidenciaInstance.label', default: 'Incidencia'), params.id])
				redirect action: "index", method: "GET"
			}
			'*'{ render status: NOT_FOUND }
		}
	}
}
