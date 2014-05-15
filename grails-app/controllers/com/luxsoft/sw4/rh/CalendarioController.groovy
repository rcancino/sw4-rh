package com.luxsoft.sw4.rh

import org.springframework.security.access.annotation.Secured

import com.luxsoft.sw4.Periodo;

import grails.transaction.Transactional
import static org.springframework.http.HttpStatus.*

@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
@Transactional(readOnly = true)
class CalendarioController {

    def index(Integer max) { 
		params.max = Math.min(max ?: 10, 100)
		respond Calendario.list(params), model:[calendarioInstanceCount: Calendario.count()]
	}
	
	def create(){
		[calendarioInstance:new Calendario(params)]
	}
	
	@Transactional
	def save(Calendario calendarioInstance){
		if(calendarioInstance==null){
			notFound()
			return
		}
		if(calendarioInstance.hasErrors()){
			respond calendarioInstance.errors, view:'create'
			return
		}
		calendarioInstance.save flush:true
		flash.message = message(code: 'default.created.message', args: [message(code: 'calendario.label', default: 'Calendario')
			, calendarioInstance.id])
		redirect action:'index'
	}
	
	def edit(Calendario calendarioInstance){
		[calendarioInstance:calendarioInstance]
	}
	
	@Transactional
	def agregarPeriodo(Calendario calendarioInstance){
		println 'Parametros: '+params
		println 'Agregando periodo a calendario: '+calendarioInstance
		CalendarioDet det=new CalendarioDet(params)
		
		calendarioInstance.addToPartidas(det)
		calendarioInstance.save failOnError:true
		render view:'edit', model:[calendarioInstance:calendarioInstance]
	}
	
	def editPeriodo(CalendarioDet calendarioDetInstance){
		[calendarioDetInstance:calendarioDetInstance]
	}
	
	@Transactional
	def eliminarPeriodo(Long id){
		
		CalendarioDet calendarioDet=CalendarioDet.get(id)
		if(calendarioDet==null){
			notFound()
			return
		}
		
		def calendarioInstance=calendarioDet.calendario
		calendarioInstance.removeFromPartidas(calendarioDet)
		calendarioInstance.save failOnError:true
		flash.message="Segmento de calendario eliminado"
		params.id=calendarioInstance.id
		redirect action:'edit',params:params
		//render view:'edit', model:[calendarioInstance:calendarioInstance]
	}
	
	
	protected void notFound() {
		request.withFormat {
			form multipartForm {
				flash.message = message(code: 'default.not.found.message', args: [message(code: 'calendario.label', default: 'Calendario'), params.id])
				redirect action: "index", method: "GET"
			}
			'*'{ render status: NOT_FOUND }
		}
	}
}