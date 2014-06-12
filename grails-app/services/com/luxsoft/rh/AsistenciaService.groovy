package com.luxsoft.rh

import java.sql.Time;

import org.apache.commons.io.FileUtils;

import grails.transaction.Transactional
import groovy.time.TimeCategory;
import groovy.time.TimeDuration;

import com.luxsoft.sw4.Periodo
import com.luxsoft.sw4.rh.Asistencia;
import com.luxsoft.sw4.rh.AsistenciaDet
import com.luxsoft.sw4.rh.Checado
import com.luxsoft.sw4.rh.Empleado
import com.luxsoft.sw4.rh.CalendarioDet
import org.apache.commons.lang.exception.ExceptionUtils


class AsistenciaService {

	def grailsApplication
	
	@Transactional
	def demo() {}

    def importarLecturas(Periodo periodo){
		log.info 'Importando lectoras para el periodo: '+periodo
    	for(date in periodo.fechaInicial..periodo.fechaFinal){
    		def sdate=date.format('yyyyMMdd')
    		
			def rawdata=grailsApplication.config.sw4.rh.asistencia.rawdata
			File file =new File(rawdata+"/"+sdate+".chk")
			println 'Importado lecturas con: '+file
			log.info 'Rawdata: '+file.path+' Exists: '+file.exists()
    		//File file=grailsApplication.mainContext.getResource("/WEB-INF/data/"+sdate+'.chk').file
    		if(file.exists()){
				//FileUtils.copyFileToDirectory(file,new File("c:/basura/rawdata"))
    			log.info 'Importando lecturas para: '+sdate
    			log.info 'Importando desde: '+file.name
    			int lector
    			def fecha
    			file.eachLine{line,row ->
    			  def fields=line.split(',')
    			  
    			  if(fields.length==2){
    				lector=fields[0].toInteger()
    				println 'Lector: '+lector
    			  }else if(fields.length==4){
    				fecha=Date.parse('yyyyMMdd',fields[2])
    				  println 'Fecha: '+fecha
    			  }else{
    				//
    				def hora=Date.parse('hhmmss',fields[0])
    				println "registrando evento empleado: ${fields[2]} hora:$hora  Fecha:$fecha"
    				//def r=Checado.findOrSaveWhere(lector:lector,fecha:fecha,hora:hora,numeroDeEmpleado:fields[2])
    				def r=new Checado(lector:lector,fecha:fecha,hora:hora,numeroDeEmpleado:fields[2])
    				r.save(failOnError:true)
    			  }
    			 
    			  
    			}
    		}
    	}
    	
    }

    
	
	def registrarAsistencias2(Periodo periodo,String tipo,CalendarioDet cal) {
		//numero magico
		
		def tolerancia1=(60*1000*10)
		
		def empleados=Empleado.findAll{salario.periodicidad==tipo && status=='ALTA'}
		
		
		
		empleados.each{empleado ->
			log.info "Actualizando asistencias ${empleado} ${periodo}"
			println "Actualizando asistencias ${empleado} ${periodo}"
			//Maestro de asistencia
			def asistencia =Asistencia
				.find("from Asistencia a where a.empleado=? and a.tipo=? and date(a.periodo.fechaInicial)=? and date(a.periodo.fechaFinal)=?"
					,[empleado,tipo,periodo.fechaInicial,periodo.fechaFinal])
			if(asistencia) {
				println 'Asistencia ya registrada actualizandola'
				if(!asistencia.calendarioDet)
					asistencia.calendarioDet=cal
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
			
			try {
				recalcularRetardos(asistencia)
				asistencia.save failOnError:true
			} catch (Exception e) {
				e.printStackTrace()
			}
		}
		
	}

/*****   LO NUEVO -----------------------------------*/
	
    def actualizarAsistencia(Asistencia asistencia){
    	def calendarioDet=asistencia.calendarioDet
    	def empleado=asistencia.empleado
    	assert(calendarioDet)
    	assert(empleado)
    	def tipo=calendarioDet.calendario.tipo=='SEMANA'?'SEMANAL':'QUINCENAL'
    	actualizarAsistencia(empleado,tipo,calendarioDet)
    }

	def actualizarAsistencia(CalendarioDet calendarioDet){
		assert(calendarioDet)
		def tipo=calendarioDet.calendario.tipo=='SEMANA'?'SEMANAL':'QUINCENAL'
		def empleados=Empleado.findAll{salario.periodicidad==tipo && activo==true}
		empleados.each{ empleado ->
			try {
				actualizarAsistencia(empleado,tipo,calendarioDet)
			} catch (Exception ex) {
				log.error ex
				def msg=ExceptionUtils.getRootCauseMessage(ex)
				println 'Error actualizando asistencia'
			}
		}
	}
	
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
		procesarIncapacidades(asistencia)
		asistencia.asistencias=asistencia.partidas.sum 0,{it.tipo=='ASISTENCIA'}
		asistencia.faltas=asistencia.partidas.sum 0,{it.tipo=='FALTA'}
		asistencia.incapacidades=asistencia.partidas.sum 0,{it.tipo=='INCAPACIDAD'}
		asistencia.incidencias=asistencia.partidas.sum 0,{it.tipo=='INCIDENCIA'}
		asistencia.vacaciones=asistencia.partidas.sum 0,{it.tipo=='VACACIONES'}
		asistencia.save failOnError:true
		return asistencia
		
	}

	
	
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
					if(it.entrada1 || it.salida1 ) {
						it.tipo='ASISTENCIA'
						it.comentario='ASISTENCIA'
						
					}else{
						it.tipo='FALTA'
						it.comentario='FALTA'
					}
					break
				default:
					if(it.entrada1 || it.salida1 || it.entrada2 || it.salida2) {
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

	
	
	def procesarIncapacidades(Asistencia asistencia){
		asistencia.partidas.each{
			println 'Procesando inacapacidades para: '+it.fecha
			def found=Incapacidad.find("from Incapacidad i where ? between date(i.fechaInicial) and date(i.fechaFinal)"
				,[it.fecha])
			if(found){
				it.comentario='INCAPACIDAD'
				it.tipo='INCAPACIDAD'
			}
		}

	}
	
    
}
