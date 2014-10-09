package com.luxsoft.sw4.rh

import static org.springframework.http.HttpStatus.*

import com.luxsoft.sw4.Autorizacion;

import grails.plugin.springsecurity.annotation.Secured;
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN','RH_USER'])
@Transactional(readOnly = true)
class VacacionesController {

	def vacacionesService
	
	def index(Integer max) {
		
		params.max = Math.min(max ?: 500, 1000)
		//def tipo=params.tipo?:'QUINCENAL'
		def list=Vacaciones.findAll("from Vacaciones i order by i.lastUpdated desc")
		/*list=list.sort{a,b ->
			a.empleado.perfil.ubicacion.clave<=>b.empleado.perfil.ubicacion.clave?:a.empleado.nombre<=>b.empleado.nombre
		}*/
		[vacacionesList:list,vacacinesTotalCount:Vacaciones.count()]
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
		def ejercicio=session.ejercicio
		def control=ControlDeVacaciones.findByEmpleadoAndEjercicio(vacacionesInstance.empleado,ejercicio)
		vacacionesInstance.control=control
		vacacionesInstance.save flush:true
		flash.message="Solicitud generada: "+vacacionesInstance.id
		redirect action:'edit',params:[id:vacacionesInstance.id]
	}
	
	def edit(Vacaciones vacacionesInstance) {
		if(vacacionesInstance==null) {
			notFound()
			return
		}
		def ejercicio=session.ejercicio
		def tipo=vacacionesInstance.empleado.salario.periodicidad=='SEMANAL'?'SEMANA':'QUINCENA'
		def periodos=CalendarioDet.findAll{calendario.ejercicio==ejercicio && calendario.tipo==tipo}
		[vacacionesInstance:vacacionesInstance,tipo:vacacionesInstance.empleado.salario.periodicidad,periodos:periodos]
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
		if(vacacionesInstance.pg){
			vacacionesInstance.dias.clear
		}
		vacacionesInstance.save flush:true
		event('VacacionesTopic',vacacionesInstance.id)
		flash.message="Solicitud de vacaciones actualizada: "+vacacionesInstance.id
		redirect action:'index',params:[tipo:vacacionesInstance.empleado.salario.periodicidad]
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
			//event('VacacionesTopic',vacacionesInstance.id)
			if(vacacionesInstance.control)
				vacacionesService.actualizarControl(vacacionesInstance.control)
			flash.message="Fecha agregada"
		}
		respond vacacionesInstance,[view:'edit',model:[tipo:vacacionesInstance.empleado.salario.periodicidad]]
	}
	
	@Transactional
	def eliminarFecha(Vacaciones vacacionesInstance) {
		def dia=params.date('fecha', 'dd/MM/yyyy')
		if(dia) {
			vacacionesInstance.removeFromDias(dia)
			vacacionesInstance.save flush:true
			//event('VacacionesTopic',vacacionesInstance.id)
			if(vacacionesInstance.control)
				vacacionesService.actualizarControl(vacacionesInstance.control)
			flash.message="Fecha eliminada"
		}
		respond vacacionesInstance,[view:'edit',model:[tipo:vacacionesInstance.empleado.salario.periodicidad]]
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
		respond vacacionesInstance,[view:'edit',model:[tipo:vacacionesInstance.empleado.salario.periodicidad]]
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
		respond vacacionesInstance,[view:'edit',model:[tipo:vacacionesInstance.empleado.salario.periodicidad]]
	}
	
	@Transactional
	def delete(Vacaciones vacacionesInstance) {
		if(vacacionesInstance==null) {
			notFound()
			return
		}
		def control=vacacionesInstance.control
		vacacionesInstance.delete flush:true
		if(control){
			vacacionesService.actualizarControl(control)
		}
		flash.message="Solicitud $vacacionesInstance.id eliminada"
		redirect action:'index',params:[tipo:vacacionesInstance.empleado.salario.periodicidad]
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
