package com.luxsoft.sw4.rh

import static org.springframework.http.HttpStatus.*

import com.luxsoft.sw4.Autorizacion
import com.luxsoft.sw4.Mes

import grails.plugin.springsecurity.annotation.Secured;
import grails.transaction.Transactional
import org.apache.commons.lang.exception.ExceptionUtils
import groovy.transform.ToString


@Secured(['ROLE_ADMIN','RH_USER'])
@Transactional(readOnly = true)
class IncentivoController {
	
	def incentivoService

	def index() {
		def calendarioDet
		def tipo=params.tipo?:'QUINCENA'
		redirect action:'quincenal'
		
	}

	def semanal(){
		def tipo='SEMANAL'
		def calendarioDet=session.calendarioSemana
		calendarioDet.attach()
		def ejercicio=session.ejercicio
		def list=Incentivo.findAll("from Incentivo i where i.asistencia.calendarioDet=? and tipo=?"
    			,[calendarioDet,tipo])
		def periodos=CalendarioDet.findAll{calendario.ejercicio==ejercicio && calendario.tipo=='SEMANA'}
		[incentivoInstanceList:list,ejercicio:ejercicio,calendarioDet:calendarioDet,tipo:tipo,periodos:periodos]
	}

	def actualizarCalendarioSemanal(){
		Long calendarioDetId=params.long('calendarioDetId')
		CalendarioDet ini=CalendarioDet.get(calendarioDetId)
		session.calendarioSemana=ini
		redirect action:'semanal' 
	}

	def quincenal(){
		def tipo='QUINCENAL'
		def calendarioDet=session.calendarioQuincena
		calendarioDet.attach()
		def ejercicio=session.ejercicio
		def list=Incentivo.findAll("from Incentivo i where i.asistencia.calendarioDet=? and tipo=?"
    			,[calendarioDet,tipo])
		def periodos=CalendarioDet.findAll{calendario.ejercicio==ejercicio && calendario.tipo=='QUINCENA'}
		
		[incentivoInstanceList:list,ejercicio:ejercicio,calendarioDet:calendarioDet,tipo:tipo,periodos:periodos]
	}

	def actualizarPeriodoQuincenal(){
		Long calendarioDetId=params.long('calendarioDetId')
		CalendarioDet ini=CalendarioDet.get(calendarioDetId)
		session.calendarioQuincena=ini
		redirect action:'quincenal' 
	}

	def mensual(){
		def tipo='MENSUAL'
		def mes=session.mes?:'Enero'
		def ejercicio=session.ejercicio
		def asistenciaId=session.asistenciaSemanalId
		def list=Incentivo.findAll("from Incentivo i where i.ejercicio=? and i.mes=?",[ejercicio,mes])
		def meses=Mes.getMeses()
		def periodos=CalendarioDet.findAll{calendario.ejercicio==ejercicio && calendario.tipo=='SEMANA'}
		[incentivoInstanceList:list,ejercicio:ejercicio,tipo:tipo,mes:mes,meses:meses,periodos:periodos]
	}

	def actualizarPeriodoMensual(){
		session.mes=params.mes
		redirect action:'mensual' 
	}
	
	@Transactional
	def generarIncentivoQuincena(CalendarioDet calendarioDet){
		def asistencias=Asistencia.findAll{calendarioDet==calendarioDet} 
		try {
			incentivoService.generarIncentivosQuincenales(calendarioDet)
			flash.message="Incentivos generados exitosamente"
		}
		catch(Exception e) {
			def msg=ExceptionUtils.getRootCauseMessage(e)
			flash.message=msg
		}
		redirect action:'quincenal'
	}

	
	@Transactional
	def generarIncencivoMensual(){
		
		Long calendarioDetId=params.long('calendarioDetId')
		CalendarioDet calendarioDet=CalendarioDet.get(calendarioDetId)
		def mes=Mes.findMesByNombre(params.mes)
		try {
			incentivoService.generarIncentivosMensuales(calendarioDet,mes)
			flash.message="Incentivos mensuales generados exitosamente $mes"
		}
		catch(Exception e) {
			e.printStackTrace()
			log.error e
			def msg=ExceptionUtils.getRootCauseMessage(e)
			flash.message=msg
		}
		redirect action:'mensual'
	}
	
	@Transactional
	def actualizarIncentivoMensual(ModificacionDeIncentivoCmd cmd){
		log.info 'Modificando el incentivo: '+cmd
		def incentivos=Incentivo.findAll("from Incentivo i where i.ejercicio=? and i.mes=? and i.tipo=?",[cmd.ejercicio,cmd.mes,cmd.tipo])
	}

	
	@Transactional
	def generarIncentivoSemanal(CalendarioDet calendarioDet){
		def asistencias=Asistencia.findAll{calendarioDet==calendarioDet} 
		try {
			incentivoService.generarIncentivosSemanales(calendarioDet)
			flash.message="Incentivos generados exitosamente"
		}
		catch(Exception e) {
			e.printStackTrace()
			def msg=ExceptionUtils.getRootCauseMessage(e)
			flash.message=msg
		}
		redirect action:'semanal'
	}
	

	
	@Transactional
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

@ToString(includeNames=true,includePackage=false)
class ModificacionDeIncentivoCmd{
	
	
	int ejercicio
	String mes
	Ubicacion ubicacion
	BigDecimal tasaBono1
	String comentario
	String tipo
	
	static constraints = {
		comentario blank:true
	}
	
}
