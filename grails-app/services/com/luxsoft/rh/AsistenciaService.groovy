package com.luxsoft.rh

import java.sql.Time;

import org.apache.commons.io.FileUtils;

import grails.transaction.Transactional
import grails.transaction.NotTransactional
import groovy.time.TimeCategory;
import groovy.time.TimeDuration;

import com.luxsoft.sw4.Periodo
import com.luxsoft.sw4.rh.Asistencia;
import com.luxsoft.sw4.rh.AsistenciaDet
import com.luxsoft.sw4.rh.BajaDeEmpleado;
import com.luxsoft.sw4.rh.Checado
import com.luxsoft.sw4.rh.Nomina;
import com.luxsoft.sw4.rh.NominaPorEmpleado
import com.luxsoft.sw4.rh.NominaPorEmpleadoDet

import com.luxsoft.sw4.rh.Empleado
import com.luxsoft.sw4.rh.CalendarioDet

import org.apache.commons.lang.exception.ExceptionUtils
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.LocalTime

import grails.events.Listener

@Transactional
class AsistenciaService {

	def grailsApplication
	
	def procesadorDeChecadas
	
	def incapacidadService
	
	def incidenciaService
	
	def vacacionesService
	


	@NotTransactional
    def actualizarAsistencia(Asistencia asistencia){
    	def calendarioDet=asistencia.calendarioDet
    	def empleado=asistencia.empleado
    	assert(calendarioDet)
    	assert(empleado)
    	def tipo=calendarioDet.calendario.tipo=='SEMANA'?'SEMANAL':'QUINCENAL'
    	actualizarAsistencia(empleado,tipo,calendarioDet)
    }

    @NotTransactional
	def actualizarAsistencia(CalendarioDet calendarioDet){
		assert(calendarioDet)
		def tipo=calendarioDet.calendario.tipo=='SEMANA'?'SEMANAL':'QUINCENAL'
		
		
		def empleados=Empleado.findAll(
			"from Empleado e where e.salario.periodicidad=? order by e.perfil.ubicacion.clave,e.apellidoPaterno asc",[tipo])
		
		
		empleados.each{ empleado ->
			try {
				actualizarAsistencia(empleado,tipo,calendarioDet)
				
			} catch (Exception ex) {
			   ex.printStackTrace()
				
				def msg=ExceptionUtils.getRootCauseMessage(ex)
				log.error "Error actualizando asistencia $msg  $empleado"
			}
		}
	}
	
	//@NotTransactional
	def actualizarAsistencia(Empleado empleado,String tipo,CalendarioDet cal) {
		
		def periodo=cal.asistencia
		//def periodo=new Periodo("07/07/2014","13/07/2014")
		
		log.info "Actualizando asistencias ${empleado} ${periodo}"
		
		def asistencia =Asistencia.find(
				"from Asistencia a where a.empleado=? and a.calendarioDet=?"
				,[empleado,cal])
		
		boolean valid=validarEmpleado(empleado,cal,asistencia)
		if(!valid){
			log.debug 'Empleado no valido para control de asistencias '+empleado
			return
		}

		if(!asistencia) {
			log.debug 'Generando registro nuevo de asistencia para '+empleado+" Periodo: "+cal.asistencia
			asistencia=new Asistencia(empleado:empleado,tipo:tipo,periodo:periodo,calendarioDet:cal)
		}
		if(asistencia.diasTrabajados>0){
			return asistencia
		}
		
		//for(date in periodo.fechaInicial..periodo.fechaFinal){
		List dias=periodo.getListaDeDias()
		
		dias.each{ date->
			//log.info 'Genrando asistencia det para: '+date
			//println 'Agregando asistenciadet para: '+date
			def asistenciaDet=asistencia.partidas.find(){det->
				DateUtils.isSameDay(det.fecha, date)
			}
			if(!asistenciaDet){
				asistenciaDet=new AsistenciaDet(fecha:date,ubicacion:empleado.perfil.ubicacion,tipo:'ASISTENCIA')
				asistencia.addToPartidas(asistenciaDet)
			}else {
				if(!asistenciaDet.manual){
					asistenciaDet.entrada1=null
					asistenciaDet.salida1=null
					asistenciaDet.entrada2=null
					asistenciaDet.salida2=null
				}
			}
		}
		if(!empleado.controlDeAsistencia){
			def periodoPago=new Periodo(cal.inicio,cal.fin)
			def diasPagados=periodoPago.getListaDeDias().size()
			asistencia.diasTrabajados=diasPagados
		}
		procesadorDeChecadas.registrarChecadas(asistencia)
		
		recalcularRetardos(asistencia)
		incapacidadService.procesar(asistencia)
		vacacionesService.procesar(asistencia)
		incidenciaService.procesar(asistencia)
		
		if(!empleado.controlDeAsistencia){
			asistencia.partidas.each{
				if(it.tipo=='FALTA'){
					
					it.tipo=='ASISTENCIA'
				}
			}
		}
		
		asistencia.asistencias=asistencia.partidas.sum 0,{it.tipo=='ASISTENCIA'?1:0}
		asistencia.faltas=asistencia.partidas.sum 0,{it.tipo=='FALTA'?1:0}
		
		
		
		//Actualizar horas trabajadas (Horas de trabajo)
		asistencia.horasTrabajadas=0
		asistencia.partidas.each{ it->
			if(it.tipo!='ASISTENCIA'){
				it.horasTrabajadas=0.0
			}else{
				asistencia.horasTrabajadas+=it.horasTrabajadas
			}
		}
		
		asistencia.save failOnError:true
		return asistencia
		
	}
	
	def boolean validarEmpleado(Empleado empleado,CalendarioDet calendarioDet,Asistencia asistencia){
		
		def asistenciaInicial=calendarioDet.asistencia.fechaInicial
		
		
		if(empleado.baja){
			if(empleado.baja.fecha<empleado.alta){ // Re ingreso
				return true
			} else if(asistenciaInicial<=empleado.baja.fecha){ // Trabajo algunos dias
				return true
			} else{  // No es valido y de existir eliminamos la asistencia
				if(asistencia){
					
					NominaPorEmpleado ne=NominaPorEmpleado.findByAsistencia(asistencia)
					if(ne){
						ne.delete flush:true
						log.debug 'NominaPorEmpleado eliminada: '+ne
					}
					asistencia.delete flush:true
					log.debug 'Asistencia invalida  eliminada '+asistencia
				}
				return false
			}
		}else{
			 return true
		}
	}

	
	@NotTransactional
	def recalcularRetardos(Asistencia asistencia) {
		log.info 'Recalculando retardos para: '+asistencia.empleado+"  Periodo: "+asistencia.periodo
		def retardoMenor=0
		asistencia.faltas=0
		asistencia.partidas.each{ it->
			
			it.retardoMenor=0
			it.retardoMayor=0
			it.retardoComida=0
			it.retardoMenorComida=0
			it.minutosNoLaborados=0
			def turnoDet=it.turnoDet
			
			if(it.entrada1) {
				LocalTime inicio=it.turnoDet.entrada1
				LocalTime entrada=LocalTime.fromDateFields(it.entrada1)
				
				//def retraso=(entrada.getLocalMillis()-inicio.getLocalMillis())/(1000*60) 
				def retraso=(((entrada.getHourOfDay()*60)+entrada.getMinuteOfHour())-((inicio.getHourOfDay()*60)+inicio.getMinuteOfHour()))
				
			//	def retraso=(entrada.getMinuteOfHour()-inicio.getMinuteOfHour())
				
				if(retraso>0){
					
					if(retraso>0 && retraso<=10) {
						it.retardoMenor=retraso
					}
					if(retraso>10) {
						it.retardoMayor=retraso
					}
				}
			}
			
			if(turnoDet.salida1 && turnoDet.entrada2) {
			//if(it.salida1 && it.entrada2) {
				if(it.salida1 && it.entrada2) {
					LocalTime salida=LocalTime.fromDateFields(it.salida1)
					LocalTime entrada=LocalTime.fromDateFields(it.entrada2)
					//TimeDuration comida=TimeCategory.minus(it.entrada2,it.salida1)
					//def retardoComida=( (comida.getHours()-1)*60 + comida.getMinutes() )
					//def retardoComida=(entrada.getMinuteOfHour()-salida.getMinuteOfHour())
					def tiempoDeComida=( ((entrada.getHourOfDay()*60)+entrada.getMinuteOfHour()) - ((salida.getHourOfDay()*60)+salida.getMinuteOfHour()) )
					def retardoComida=tiempoDeComida-60
					//log.info 'Retardo comida: '+retardoComida
					//def retardoComida=( (comida.getHours()-1)*60 + comida.getMinutes() )
				
					if(retardoComida>0) {
						if(retardoComida<=10){
							it.retardoMenorComida=retardoComida
						}else
							it.retardoComida=retardoComida
					}
				}
				
			}
			
			
			def dia=it.fecha.toCalendar().get(Calendar.DAY_OF_WEEK)
			
			if(turnoDet.entrada1==null){
				it.comentario='DESCANSO'
				it.tipo='DESCANSO'
			}
			else if(turnoDet.entrada1 && turnoDet.salida2){  //Turno completo
				if(it.entrada1 || it.salida1 || it.entrada2 || it.salida2) {
					it.comentario='ASISTENCIA'
					it.tipo='ASISTENCIA'
				}else if( !it.entrada1 && !it.salida1 &&  !it.entrada2 && !it.salida2){
					it.comentario='FALTA'
					it.tipo='FALTA'
				}
			}
			
			else if(turnoDet.entrada1 && (turnoDet.entrada2==null)){  //Turno medio
				if(it.entrada1 || it.salida1) {
					it.comentario='ASISTENCIA'
					it.tipo='ASISTENCIA'
				}else if( !it.entrada1 && !it.salida1 ){
					it.comentario='FALTA'
					it.tipo='FALTA'
				}
			}
			
			
			
			LocalTime salidaOficial=turnoDet.salida2?:turnoDet.salida1
			
			if(salidaOficial){
				
				def salidaRegistrada=turnoDet.salida2?it.salida2:it.salida1
				
				if(salidaRegistrada){
					
					LocalTime salida=LocalTime.fromDateFields(salidaRegistrada)
					def horas=salidaOficial.getHourOfDay()- salida.getHourOfDay()
					def minutos=salidaOficial.getMinuteOfHour() - salida.getMinuteOfHour()
					//log.info 'Horas: '+horas+ 'Minutos: '+minutos
					//def salidaAnticipada=salidaOficial.getLocalMillis()/(1000*60)-salida.getLocalMillis()/(1000*60)
					def salidaAnticipada=horas*60+minutos
					
					if(salidaAnticipada>0){
						//log.info 'Salida anticipada: '+salidaAnticipada
						it.minutosNoLaborados+=salidaAnticipada
					}
				}
				//it.minutosNoLaborados+=it.retardoMayor
			}
			
		}
		
		asistencia.retardoMenor=asistencia.partidas.sum 0,{it.retardoMenor}
		asistencia.retardoMayor=asistencia.partidas.sum 0,{it.retardoMayor}
		asistencia.retardoComida=asistencia.partidas.sum 0,{it.retardoComida}
		asistencia.retardoMenorComida=asistencia.partidas.sum 0,{it.retardoMenorComida}
		asistencia.minutosNoLaborados=asistencia.partidas.sum 0,{it.minutosNoLaborados}
		return asistencia
	}

	@NotTransactional
	@Listener(namespace='gorm')
	def afterUpdate(AsistenciaDet det){
		log.info 'Modificacion manual en asistencia det: '+det
		def calendarioDet=det.asistencia.calendarioDet
		def tipo=calendarioDet.calendario.tipo=='SEMANA'?'SEMANAL':'QUINCENAL'
		actualizarAsistencia(det.asistencia.empleado,tipo,calendarioDet)
	}
	
	@Listener(namespace='gorm')
	def afterInsert(Empleado emp){
		//Buscar la posible existencia de asistencia
	}
	
	// @NotTransactional
	// def depurar(CalendarioDet calendarioDet){
	// 	log.info 'Depurando asistencias para: '+calendarioDet
	// 	def asistencias=Asistencia.findAll{calendarioDet==calendarioDet}
	// 	def delete=[]
	// 	asistencias.each{ a->
	// 		def emp=a.empleado
	// 		if(emp.baja){
	// 			log.info 'Posible depuracion: '+a+ 'Baja: '+emp.baja+ ' Alta'
	// 		}
			
	// 	}
	// }
	
	/*
	@Listener(namespace='gorm')
	def afterInsert(BajaDeEmpleado baja){
		log.debug 'Localizando la asistencia para el empleado: '+baja
		def emp=baja.empleado
		def fecha=baja.fecha
		def found=Asistencia.executeQuery(
			"from Asistencia det where det.empleado=? and date(?) between det.periodo.fechaInicial and det.periodo.fechaFinal"
			,[emp,fecha])
		if(found){
			def asistencia=found.get(0)
			log.debug 'Eliminar la asistencia: '+asistencia
		}
	}
	*/

	
	def delete(Asistencia asistencia){
		log.info 'Eliminando asistencia: '+asistencia
		try{
			asistencia.delete flush:true
			return "Asistencia eliminada: "+asistencia.id
		}catch(Exception ex){
			def msg="Error al intentar eliminar asistencia causa: " +ExceptionUtils.getRootCauseMessage(ex)
			//throw new AsistenciaException(message:msg,asistencia:asistencia)
			log.error ex
			return msg
		}
		return asistencia
	}
    
}

class AsistenciaException extends RuntimeException{
	String message
	Asistencia asistencia
}
