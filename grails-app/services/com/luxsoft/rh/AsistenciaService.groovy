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
import com.luxsoft.sw4.rh.DiaFestivo;
import com.luxsoft.sw4.rh.Nomina;
import com.luxsoft.sw4.rh.NominaPorEmpleado
import com.luxsoft.sw4.rh.NominaPorEmpleadoDet
import com.luxsoft.sw4.rh.Empleado
import com.luxsoft.sw4.rh.CalendarioDet

import org.apache.commons.lang.exception.ExceptionUtils
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.LocalTime
import com.luxsoft.sw4.rh.Vacaciones

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
	
	@NotTransactional
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
		
		//Buscar vacaciones de cierre anual
		def calendarioFinal=CalendarioDet
			.executeQuery("select max(c.folio) from CalendarioDet c where c.calendario.tipo=? and c.calendario.ejercicio=? and c.calendario.comentario like ? and c.folio!=108"
				,[empleado.salario.periodicidad=='SEMANAL'?'SEMANA':'QUINCENA',cal.calendario.ejercicio,'%NOMINA%']) 
		println 'Calendario id: '+calendarioFinal.get(0)
		if(calendarioFinal.get(0) ==cal.folio){
			def ejercicio=cal.calendario.ejercicio
			def cierre=Vacaciones.executeQuery("from Vacaciones v where v.empleado=? and  year(v.solicitud)=? and cierreAnual=true",[empleado,ejercicio])
			cierre.each{ ci->
				ci.dias.each{ dia->
					def ffound=asistencia.partidas.find{dd->dd.fecha.format('dd/MM/yyyy')==dia.format('dd/MM/yyyy') }
					if(!ffound){
						def asistenciaDet=new AsistenciaDet(fecha:dia,ubicacion:empleado.perfil.ubicacion,tipo:'VACACIONES')
						asistencia.addToPartidas(asistenciaDet)
					}
					
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
	
	@NotTransactional
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
		asistencia.partidas.each{ it-> //Iteracion dia por dia
			
			it.retardoMenor=0
			it.retardoMayor=0
			it.retardoComida=0
			it.retardoMenorComida=0
			it.minutosNoLaborados=0
			def turnoDet=it.turnoDet
			
			def diaFestivo=DiaFestivo.findByFecha(it.fecha)
			
			
			if(it.entrada1) {
				LocalTime inicio=it.turnoDet.entrada1
				LocalTime entrada=LocalTime.fromDateFields(it.entrada1)
				
				def retraso=(((entrada.getHourOfDay()*60)+entrada.getMinuteOfHour())-((inicio.getHourOfDay()*60)+inicio.getMinuteOfHour()))
				
				if(retraso>0){
					
					if(retraso>0 && retraso<=10) {
						it.retardoMenor=retraso
					}
					if(retraso>10) {
						it.retardoMayor=retraso
					}
				}
			}
			
			//Evaluacion de retardo comida
			if(turnoDet.salida1 && turnoDet.entrada2) {
			
				if(it.salida1 && it.entrada2) {
					LocalTime salida=LocalTime.fromDateFields(it.salida1)
					LocalTime entrada=LocalTime.fromDateFields(it.entrada2)
					
					def tiempoDeComida=( ((entrada.getHourOfDay()*60)+entrada.getMinuteOfHour()) - ((salida.getHourOfDay()*60)+salida.getMinuteOfHour()) )
					def retardoComida=tiempoDeComida-60
					
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
			
			if(diaFestivo && diaFestivo.parcial){
				salidaOficial=LocalTime.fromDateFields(diaFestivo.salida)
			}
			
			if(salidaOficial){
				
				def salidaRegistrada=turnoDet.salida2?it.salida2:it.salida1
				
				if(salidaRegistrada==null)
					salidaRegistrada=it.salida1
					
				if(salidaRegistrada){
					
					LocalTime salida=LocalTime.fromDateFields(salidaRegistrada)
					def horas=salidaOficial.getHourOfDay()- salida.getHourOfDay()
					def minutos=salidaOficial.getMinuteOfHour() - salida.getMinuteOfHour()
					
					def salidaAnticipada=horas*60+minutos
					
					if(salidaAnticipada>0){
						it.minutosNoLaborados+=salidaAnticipada
					}
				}
			}
			
			if(diaFestivo && !diaFestivo.parcial){
				
				it.retardoMenor=0
				it.retardoMayor=0
				it.retardoComida=0
				it.retardoMenorComida=0
				it.minutosNoLaborados=0
				it.tipo='DIA_FESTIVO'
				it.comentario="DIA_FESTIVO"
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
