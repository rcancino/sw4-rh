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

import com.luxsoft.sw4.rh.Empleado
import com.luxsoft.sw4.rh.CalendarioDet

import org.apache.commons.lang.exception.ExceptionUtils


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
		def empleados=Empleado.findAll{salario.periodicidad==tipo  }
		empleados.each{ empleado ->
			try {
				if(empleado.controlDeAsistencia) {
					if(empleado.baja) {
						if(empleado.baja.fecha<calendarioDet.asistencia.fechaInicial) {
							actualizarAsistencia(empleado,tipo,calendarioDet)
						}
					}else {
						actualizarAsistencia(empleado,tipo,calendarioDet)
					}
				}
			} catch (Exception ex) {
				log.error ex
				def msg=ExceptionUtils.getRootCauseMessage(ex)
				println 'Error actualizando asistencia'
			}
		}
	}
	
	@NotTransactional
	def actualizarAsistencia(Empleado empleado,String tipo,CalendarioDet cal) {
		
		//numero magico
		def tolerancia1=(60*1000*10)
		def periodo=cal.asistencia

		
		log.info "Actualizando asistencias ${empleado} ${periodo}"
		println "Actualizando asistencias ${empleado} ${periodo}"
		//Maestro de asistencia
		def asistencia =Asistencia
			.find("from Asistencia a where a.empleado=? and a.calendarioDet=?"
				,[empleado,cal])
		if(asistencia) {
			println 'Asistencia ya registrada actualizandola'
			asistencia.partidas.clear()
		}else {
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
			
			def asistenciaDet=new AsistenciaDet(fecha:date)
			
			
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
			asistencia.addToPartidas(asistenciaDet)
		}
		
		recalcularRetardos(asistencia)
		
		asistencia.asistencias=asistencia.partidas.sum 0,{it.tipo=='ASISTENCIA'?1:0}
		asistencia.faltas=asistencia.partidas.sum 0,{it.tipo=='FALTA'?1:0}
		incapacidadService.procesar(asistencia)
		incidenciaService.procesar(asistencia)
		vacacionesService.procesar(asistencia)
		/*asistencia.incapacidades=asistencia.partidas.sum 0,{it.tipo=='INCAPACIDAD'}
		asistencia.incidencias=asistencia.partidas.sum 0,{it.tipo=='INCIDENCIA'}
		asistencia.vacaciones=asistencia.partidas.sum 0,{it.tipo=='VACACIONES'}
		*/asistencia.save failOnError:true
		return asistencia
		
	}

	
	@NotTransactional
	def recalcularRetardos(Asistencia asistencia) {
		println 'Recalculando retardos para: '+asistencia.empleado+"  Periodo: "+asistencia.periodo
		def retardoMenor=0
		asistencia.faltas=0
		asistencia.partidas.each{
			
			it.retardoMenor=0
			it.retardoMayor=0
			it.retardoComida=0
			
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
			
			switch (dia){
				case Calendar.SUNDAY:
					it.comentario='DESCANSO'
					it.tipo='DESCANSO'
					break
				case Calendar.SATURDAY:
					if(it.entrada1 && it.salida1 ) {
						it.tipo='ASISTENCIA'
						it.comentario='ASISTENCIA'
						
					}else{
						it.tipo='FALTA'
						it.comentario='FALTA'
					}
					break
				default:
					if(it.entrada1 && it.salida1 && it.entrada2 && it.salida2) {
						it.comentario='ASISTENCIA'
						it.tipo='ASISTENCIA'
					}else{
						it.comentario='FALTA'
						it.tipo='FALTA'
					}
					break
			}
			
		}
		
		asistencia.retardoMenor=asistencia.partidas.sum 0,{it.retardoMenor}
		asistencia.retardoMayor=asistencia.partidas.sum 0,{it.retardoMayor}
		asistencia.retardoComida=asistencia.partidas.sum 0,{it.retardoComida}
		
		return asistencia
	}

	
	
	
    
}
