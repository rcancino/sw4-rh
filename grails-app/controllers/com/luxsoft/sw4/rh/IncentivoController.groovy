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
		def target=""
		
		switch (tipo) {
			case 'QUINCENA':
				target="quincenal"
				break
			case 'SEMANAL':
				target="semanal"
				break
			case 'MENSUAL':
				target="mensual"
				break
		}
		redirect action:target
		
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
		//def ejercicio=2014
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
		//CalendarioDet calendarioDet=CalendarioDet.get(calendarioDetId)
		def ejercicio=session.ejercicio
		def mes=Mes.findMesByNombre(params.mes)
		try {
			incentivoService.generarIncentivosMensuales(ejercicio,mes)
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
	def modificarIncentivoMensual(ModificacionDeIncentivoCmd cmd){
		log.info 'Modificando el incentivo: '+cmd
		def incentivos=Incentivo
			.findAll("from Incentivo i where i.ejercicio=? and i.mes=? and i.tipo=? and i.empleado.perfil.ubicacion=?"
				,[cmd.ejercicio,cmd.mes,cmd.tipo,cmd.ubicacion])
		incentivos.each{ 
			
			it.tasaBono1=params.tasaBono1 as BigDecimal 
			incentivoService.calcularIncentivoMensual(it)
		}
		
		
		redirect action:'mensual'
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
		def asistencias=[]
		switch(incentivoInstance.tipo) {
			case 'SEMANAL':
			case 'QUINCENAL':
				asistencias=incentivoInstance.asistencia.partidas
				break
			case 'MENSUAL':
				asistencias=AsistenciaDet
					.findAll("from AsistenciaDet d where d.asistencia.empleado=? and date(d.fecha) between ? and ? "
					,[incentivoInstance.empleado,incentivoInstance.fechaInicial,incentivoInstance.fechaFinal])
					break
			default:		
				break
		}
		
		[incentivoInstance:incentivoInstance,asistencias:asistencias]
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
		
		switch(incentivoInstance.tipo) {
			case 'SEMANAL':
				incentivoInstance=incentivoService.calcularIncentivoSemanal(incentivoInstance)
				break
			case 'QUINCENAL':
				incentivoInstance=incentivoService.calcularIncentivoQuincenal(incentivoInstance)
				break
			case 'MENSUAL':
				incentivoInstance=incentivoService.calcularIncentivoMensual(incentivoInstance)
				break
			default:
				break
		}
		flash.message="Solicitud de incentivo actualizada: "+incentivoInstance.id
		redirect action:'edit',params:[id:incentivoInstance.id]
		//redirect action:'index',params:[tipo:incentivoInstance.tipo]
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
	
	@Transactional
	def recalcular(Incentivo incentivo){
		if(incentivo.tipo=='MENSUAL'){
			incentivo=incentivoService.calcularIncentivoMensual(incentivo)
			redirect action:'edit', params:[id:incentivo.id]
			return
		}else if(incentivo.tipo=='QUINCENAL'){
			incentivo=incentivoService.calcularIncentivoQuincenal(incentivo)
			redirect action:'edit', params:[id:incentivo.id]
			return
		}
		
	}
	
	@Transactional
	def asignarCalendarioDeIncentivoMensual(Integer ejercicio,String mes){
		def calendarioDet=CalendarioDet.get(params.calendarioDetId)
		//println 'Aplicando calendario : '+calendarioDet
		if(calendarioDet){
			def incentivos=Incentivo.findAllByEjercicioAndMes(ejercicio,mes)
			incentivos.each{incentivo->
				def a=Asistencia.findByCalendarioDetAndEmpleado(calendarioDet,incentivo.empleado)
				//println 'Asistencia: '+a
				if(a){
					incentivo.asistencia=a
					incentivo.save failOnError:true
					//println 'Calendario asignado a: '+incentivo
				}
				
			}
			
		}
		redirect action:'mensual' 
	}
	
	

}

@ToString(includeNames=true,includePackage=false)
class ModificacionDeIncentivoCmd{
	
	
	int ejercicio
	String mes
	Ubicacion ubicacion
	double tasaBono1
	String comentario
	String tipo
	
	static constraints = {
		comentario blank:true
	}
	
}
