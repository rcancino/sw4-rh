package com.luxsoft.sw4.rh

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional;
import static org.springframework.http.HttpStatus.*

@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
class IncapacidadController {
    
	
	
	def incapacidadService
	
	def index(Integer max) {
		params.max = Math.min(max ?: 15, 100)
		params.sort=params.sort?:'dateCreated'
		params.order='desc'
		def tipo=params.tipo?:'QUINCENAL'
		def list=Incapacidad.findAll("from Incapacidad i where i.empleado.salario.periodicidad=?",[tipo])
		[incapacidadesList:list,incapacidadTotalCount:Incapacidad.count(),tipo:tipo]
		
	}
	
	def create() {
		[incapacidadInstance:new Incapacidad(params)]
	}
	
	def edit(Incapacidad incapacidadInstance) {
		if(incapacidadInstance==null) {
			notFound()
			return
		}
		[incapacidadInstance:incapacidadInstance,tipo:incapacidadInstance.empleado.salario.periodicidad]
	}
	
	def update(Incapacidad incapacidadInstance) {
		if(incapacidadInstance==null) {
			notFound()
			return
		}
		if(incapacidadInstance.hasErrors()) {
			render view:'edit',model:[incapacidadInstance:incapacidadInstance]
		}
		incapacidadInstance=incapacidadService.salvar(incapacidadInstance)
		flash.message="Incapacidad actualizada: "+incapacidadInstance.id
		redirect action:'index',params:[tipo:incapacidadInstance.empleado.salario.periodicidad]
	}
	
	def save(Incapacidad incapacidadInstance) {
		if(incapacidadInstance==null) {
			notFound()
			return
		}
		if(incapacidadInstance.hasErrors()) {
			respond incapacidadInstance,[view:'edit']
		}
		incapacidadInstance=incapacidadService.salvar(incapacidadInstance)
		flash.message="Incapacidad $incapacidadInstance.id creada"
		redirect action:'index',params:[tipo:incapacidadInstance.empleado.salario.periodicidad]
	}
	
	def agregarFecha(Incapacidad incapacidadInstance) {
		if(incapacidadInstance==null) {
			notFound()
			return
		}
		def dia=params.date('fecha', 'dd/MM/yyyy')
		if(dia) {
			incapacidadInstance.addToDias(dia)
			incapacidadInstance=incapacidadService.salvar(incapacidadInstance)
			flash.message="Fecha agregada"
		}
		respond incapacidadInstance,[view:'edit']
	}
	
	def eliminarFecha(Incapacidad incapacidadInstance) {
		if(incapacidadInstance==null) {
			notFound()
			return
		}
		def dia=params.date('fecha', 'dd/MM/yyyy')
		if(dia) {
			incapacidadInstance.removeFromDias(dia)
			incapacidadInstance=incapacidadService.salvar(incapacidadInstance)
			flash.message="Fecha eliminada"
		}
		respond incapacidadInstance,[view:'edit']
	}
	
	def delete(Incapacidad incapacidadInstance) {
		if(incapacidadInstance==null) {
			notFound()
			return
		}
		incapacidadService.eliminar(incapacidadInstance)
		flash.message="Incapacidad $incapacidadInstance.id eliminada"
		redirect view:'index'
	}
	
	protected void notFound() {
		request.withFormat {
			form multipartForm {
				flash.message = message(code: 'default.not.found.message', args: [message(code: 'incapacidad.label', default: 'Incapacidad'), params.id])
				redirect action: "index", method: "GET"
			}
			'*'{ render status: NOT_FOUND }
		}
	}
}
