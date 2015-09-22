package com.luxsoft.sw4.rh

import grails.transaction.Transactional

@Transactional
class AsistenciaImssService {

    def generar(CalendarioDet cal) {
    	log.info 'Generando asistencia de calendario: '+cal

    	def query=Asistencia.where {
    	    calendarioDet==cal &&(partidas{tipo=='INCAPACIDAD' || tipo=='FALTA'})
    	}
    	query.list().each{
    		
    		def asistencia=AsistenciaImss.findByAsistencia(it)
    		if(!asistencia){

    			asistencia=new AsistenciaImss(
    				asistencia:it,
    				calendarioDet:cal,
    				empleado:it.empleado,
    				inicioAsistencia:cal.asistencia.fechaInicial,
    				finAsistencia:cal.asistencia.fechaFinal,
    				inicioNomina:cal.inicio,
    				finNomina:cal.fin
    			)
    			//asistencia.save failOnError:true,flush:true
    			log.debug 'Asistencia generada: '+asistencia
    		}
    		actualizar(asistencia)
    	}
    }

    def actualizar(AsistenciaImss asistencia){
    	log.debug 'Actualizando asistencia imss: '+asistencia
    	if(asistencia.partidas)
    		asistencia.partidas.clear()
    	def fechas=new ArrayList((asistencia.calendarioDet.inicio..asistencia.calendarioDet.fin) )
    	fechas=fechas.sort().reverse()

    	def map=[:]

    	asistencia.asistencia.partidas.sort{it.fecha}.each{
    	  if(fechas){
    	      map[it]=fechas.pop()
    	  }
    	}

    	def disponibles=[]
    	map.each{k,v->

    		if(k.tipo!='INCAPACIDAD'){
    			def calendar=Calendar.instance
    			calendar.setTime(v)
    			if(calendar.get(Calendar.DAY_OF_WEEK)!=1){
    				def festivo=DiaFestivo.findByFechaAndParcial(v,false)
    				if(!festivo)
    					disponibles.add(v)
    			}
    		}else if(k.tipo=='INCAPACIDAD'){
    			def calendar=Calendar.instance
    			calendar.setTime(v)
    			
    			if(calendar.get(Calendar.DAY_OF_WEEK)!=1){
    				disponibles.add(v)
    			}
    			//disponibles.add(v)
    			disponibles.remove(k.fecha)
    		}
    	}
    	disponibles=disponibles.sort().reverse()
    	//println 'Disponibles:'+disponibles
    	asistencia.asistencia.partidas.each{
    		if(it.tipo=='INCAPACIDAD'){
    			asistencia.addToPartidas(fecha:it.fecha,tipo:it.tipo,subTipo:'PENDIENTE',cambio:it.fecha)
			}else if(it.tipo=='FALTA'){
				//def fecha=map[it]?:null
				def fecha=disponibles?disponibles.pop():null

				asistencia.addToPartidas(fecha:it.fecha,tipo:it.tipo,subTipo:'PENDIENTE',cambio:fecha)
			}else{
				//asistencia.addToPartidas(fecha:it.fecha,tipo:it.tipo,subTipo:'PENDIENTE',cambio:it.fecha)
			}
    	}
    	asistencia.save failOnError:true,flush:true
    }

    def eliminar(CalendarioDet calendario){
    	def asistencias=AsistenciaImss.findAllByCalendarioDet(calendario)
    	asistencias.each{
    		it.delete(flush:true)
    	}

    }

    
}
