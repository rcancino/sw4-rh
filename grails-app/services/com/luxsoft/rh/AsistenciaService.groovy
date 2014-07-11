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
import com.luxsoft.sw4.rh.Checado
import com.luxsoft.sw4.rh.Nomina;

import com.luxsoft.sw4.rh.Empleado
import com.luxsoft.sw4.rh.CalendarioDet

import org.apache.commons.lang.exception.ExceptionUtils
import org.apache.commons.lang.time.DateUtils;

import grails.events.Listener

@Transactional
class AsistenciaService {

	def grailsApplication
	
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
		
		//def empleados=Empleado.findAll(sort:"apellidoPaterno"){salario.periodicidad==tipo  }
		def empleados=Empleado.findAll(
			"from Empleado e where e.salario.periodicidad=? order by e.perfil.ubicacion.clave,e.apellidoPaterno asc",[tipo])
		empleados.each{ empleado ->
			try {
				if(empleado.controlDeAsistencia) {
					
					if(empleado.baja && empleado.status=='BAJA') {
						if(empleado.baja.fecha>=calendarioDet.asistencia.fechaInicial) {
							actualizarAsistencia(empleado,tipo,calendarioDet)
						}
					}else {
						actualizarAsistencia(empleado,tipo,calendarioDet)
					}
				}
			} catch (Exception ex) {
			   ex.printStackTrace()
				log.error ex
				def msg=ExceptionUtils.getRootCauseMessage(ex)
				println 'Error actualizando asistencia'+msg+ " ${empleado}"
			}
		}
	}
	
	@NotTransactional
	def actualizarAsistencia(Empleado empleado,String tipo,CalendarioDet cal) {
		
		//numero magico
		def tolerancia1=(60*1000*10)
		def periodo=cal.asistencia

		
		log.info "Actualizando asistencias ${empleado} ${periodo}"
		//Maestro de asistencia
		def asistencia =Asistencia
			.find("from Asistencia a where a.empleado=? and a.calendarioDet=?"
				,[empleado,cal])
		if(asistencia) {
			//println 'Asistencia ya registrada actualizandola'
			//asistencia.partidas.clear()
			//asistencia.save flush:true
		}else {
			//println 'Generando registro de asistencia para '+empleado+" Periodo: "+cal.asistencia
			asistencia=new Asistencia(empleado:empleado,tipo:tipo,periodo:periodo,calendarioDet:cal)
		}
		for(date in periodo.fechaInicial..periodo.fechaFinal){
			def lecturas=Checado.findAll(sort:"numeroDeEmpleado"){numeroDeEmpleado==empleado?.perfil?.numeroDeTrabajador && fecha==date}
			lecturas.sort(){ c->
				c.hora
			}
			
			def valid =[]
			def last=null
			lecturas.each{ reg->
				if(last==null){
					last=reg.hora
					valid.add(reg)
				}
				def dif=reg.hora.time-last.time
				if(dif>tolerancia1 ){
					//println "$date  Lectura valida  $reg.hora"
					last=reg.hora
					valid.add(reg)
				}
			}
			def asistenciaDet=asistencia.partidas.find(){det->
				
				DateUtils.isSameDay(det.fecha, date)
			}
			if(!asistenciaDet){
				log.debug 'Asistencia nueva dando de alta'
				asistenciaDet=new AsistenciaDet(fecha:date,ubicacion:empleado.perfil.ubicacion)
			}else{
				log.debug 'Asistencia ya generada tipo:  '+asistenciaDet.manual
			}
			
			if(!asistenciaDet.manual){
				
				asistenciaDet.entrada1=null
				asistenciaDet.salida1=null
				asistenciaDet.entrada2=null
				asistenciaDet.salida2=null
				
				for(def i=0;i<valid.size;i++) {
					def checado=valid[i]
					def time=new Time(checado.hora.time)
					
					switch(i) {
						case 0:
							asistenciaDet.entrada1=time
							break
						case 1:
							asistenciaDet.salida1=time
							break
						case 2:
							asistenciaDet.entrada2=time
							break
						case 3:
							asistenciaDet.salida2=time
							break
						default:
							break
					}
				}
			}
			if(!asistenciaDet.asistencia)
				asistencia.addToPartidas(asistenciaDet)
		}
		
		recalcularRetardos(asistencia)
		
		
		incapacidadService.procesar(asistencia)
		vacacionesService.procesar(asistencia)
		incidenciaService.procesar(asistencia)
		asistencia.asistencias=asistencia.partidas.sum 0,{it.tipo=='ASISTENCIA'?1:0}
		asistencia.faltas=asistencia.partidas.sum 0,{it.tipo=='FALTA'?1:0}
		//Calcular horas trabajadas
		asistencia.horasTrabajadas=0
		asistencia.partidas.each{
			
			if(it.tipo=='ASISTENCIA'){
				def dia=it.fecha.toCalendar().get(Calendar.DAY_OF_WEEK)
				switch (dia){
					case Calendar.SUNDAY:
						it.horasTrabajadas=0.0
						break
					case Calendar.SATURDAY:
						it.horasTrabajadas=5.0
						break
					default:
					it.horasTrabajadas=9.5
						break
				}
				asistencia.horasTrabajadas+=it.horasTrabajadas
			}
			if(it.tipo!='ASISTENCIA'){
				it.horasTrabajadas=0.0
			}
			
		}
		
		
		asistencia.save failOnError:true
		return asistencia
		
	}

	
	@NotTransactional
	def recalcularRetardos(Asistencia asistencia) {
		log.info 'Recalculando retardos para: '+asistencia.empleado+"  Periodo: "+asistencia.periodo
		def retardoMenor=0
		asistencia.faltas=0
		asistencia.partidas.each{
			
			it.retardoMenor=0
			it.retardoMayor=0
			it.retardoComida=0
			it.minutosNoLaborados=0
			
			if(it.entrada1) {
				def entrada=Time.valueOf('09:00:00')
				TimeDuration duration=TimeCategory.minus(it.entrada1,entrada)
				def retraso=duration.getMinutes()
				if(retraso>0 && retraso<=10) {
					//println 'Entrada: '+it.entrada1+ "  Retraso: "+duration.getMinutes()
					it.retardoMenor=retraso
				}
				if(retraso>10) {
					it.retardoMayor=retraso
				}
			}
			if(it.salida1 && it.entrada2) {
				TimeDuration comida=TimeCategory.minus(it.entrada2,it.salida1)
				def retComida=( (comida.getHours()-1)*60 + comida.getMinutes() )
				
				if(retComida>0) {
					it.retardoComida=retComida
				}
				
			}
			
			
			def dia=it.fecha.toCalendar().get(Calendar.DAY_OF_WEEK)
			
			def salidaOficial=Time.valueOf('18:30:00')
			
			
			switch (dia){
				case Calendar.SUNDAY:
					it.comentario='DESCANSO'
					it.tipo='DESCANSO'
					break
				case Calendar.SATURDAY:
					if(it.entrada1 || it.salida1 ) {
						it.tipo='ASISTENCIA'
						it.comentario='ASISTENCIA'
						salidaOficial=Time.valueOf('14:00:00')
					}else{
						it.tipo='FALTA'
						it.comentario='FALTA'
					}
					break
				default:
					if(it.entrada1 || it.salida1 || it.entrada2 || it.salida2) {
						it.comentario='ASISTENCIA'
						it.tipo='ASISTENCIA'
					}else if( !it.entrada1 && !it.salida1 &&  !it.entrada2 && !it.salida2){
						it.comentario='FALTA'
						it.tipo='FALTA'
					}
					break
			}
			
			//Calcular los minutos no laborados
			
			if(it.salida2){
				def ultimaSalida=DateUtils.truncate(it.salida2, Calendar.MINUTE)
				 
				//TimeDuration duration=TimeCategory.minus(salidaOficial,it.salida2)
				def salidaAnticipada=(salidaOficial.getTime()-ultimaSalida.getTime())/1000/60	
				
				if(salidaAnticipada>0){
					//println 'Salida anticipada: '+salidaAnticipada
					it.minutosNoLaborados+=salidaAnticipada
				}
			}
			it.minutosNoLaborados+=it.retardoMayor
			
		}
		
		asistencia.retardoMenor=asistencia.partidas.sum 0,{it.retardoMenor}
		asistencia.retardoMayor=asistencia.partidas.sum 0,{it.retardoMayor}
		asistencia.retardoComida=asistencia.partidas.sum 0,{it.retardoComida}
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
	
    
}
