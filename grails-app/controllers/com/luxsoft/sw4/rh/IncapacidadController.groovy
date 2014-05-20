package com.luxsoft.sw4.rh

import grails.plugin.springsecurity.annotation.Secured
import static org.springframework.http.HttpStatus.*

@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
class IncapacidadController {
    
	static scaffold = true
	
	def incapacidadService
	
	def index(Integer max) {
		params.max = Math.min(max ?: 15, 100)
		params.sort=params.sort?:'dateCreated'
		params.order='desc'
		def list=Incapacidad.list(params)
		println 'List: '+list
		[incapacidadesList:list,incapacidadTotalCount:Incapacidad.count()]
		
	}
	
	def create() {
		[incapacidadInstance:new Incapacidad(params)]
	}
	def edit(Incapacidad incapacidadInstance) {
		[incapacidadInstance:incapacidadInstance]
	}
	
	def save(Incapacidad incapacidadInstance) {
		if(incapacidadInstance==null) {
			notFound()
			return
		}
		if(incapacidadInstance.hasErrors()) {
			respond view:'create',model:[incapacidadInstance:incapacidadInstance]
		}
		incapacidadInstance=incapacidadService.salvar(incapacidadInstance)
		flash.message="Incapacidad $incapacidadInstance.id creada"
		redirect action:'index'
	}
	
	def agregarFecha(Incapacidad incapacidadInstance) {
		if(incapacidadInstance==null) {
			notFound()
			return
		}
		incapacidadInstance.addToPartidas(fecha:params.date('fecha', 'dd/MM/yyyy'))
		incapacidadInstance=incapacidadService.salvar(incapacidadInstance)
		flash.message="Fecha agregada"
		render view:'edit',model:[incapacidadInstance:incapacidadInstance]
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
