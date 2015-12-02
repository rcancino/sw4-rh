package com.luxsoft.sw4.rh

import grails.transaction.Transactional

@Transactional
class AsistenciaImssService {

    def generar(CalendarioDet cal) {
    	log.info 'Generando asistencia de calendario: '+cal

    	def query=Asistencia.where {
    	    //calendarioDet==cal &&(partidas{tipo=='INCAPACIDAD' || tipo=='FALTA' || (tipo=='INCIDENCIA' && comentario=='INCIDENCIA PERMISO') })
            calendarioDet==cal &&(partidas{tipo=='INCAPACIDAD' || tipo=='FALTA' || (tipo=='INCIDENCIA_F') })
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
    	log.info 'Actualizando asistencia imss: '+asistencia
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
    	
    	asistencia.asistencia.partidas.each{
    		if(it.tipo=='INCAPACIDAD'){
    			asistencia.addToPartidas(fecha:it.fecha,tipo:'INCAPACIDAD',subTipo:it.comentario,cambio:it.fecha)
			}else if(it.tipo=='FALTA'  ) {
                def det=new AsistenciaImssDet(fecha:it.fecha,tipo:'FALTA',subTipo:it.comentario)
                if(disponibles)
                    det.cambio=disponibles.pop()
				asistencia.addToPartidas(det)
			//}else if(it.tipo=='INCIDENCIA' && it.comentario=='INCIDENCIA PERMISO'){
            }else if(it.tipo=='INCIDENCIA_F'){
                def det=new AsistenciaImssDet(fecha:it.fecha,tipo:'FALTA',subTipo:it.comentario)
                if(disponibles)
                    det.cambio=disponibles.pop()
                asistencia.addToPartidas(det)
			}
    	}
        asistencia.partidas.each{
            if(!it.validate())
                println 'Validando partida:'+ it.errors
        }
        actualizarExcluir(asistencia)
    	asistencia.save failOnError:true,flush:true
    }



    def actualizarExcluir(AsistenciaImss asistencia){
        asistencia.partidas.each{
            if(it.cambio){
                if( !it.fecha.isSameMonth(it.cambio) ) {
                    it.excluir = it.fecha<it.cambio
                }
            }
                
        }
    }

    def eliminar(CalendarioDet calendario){
    	def asistencias=AsistenciaImss.findAllByCalendarioDet(calendario)
    	asistencias.each{
    		it.delete(flush:true)
    	}

    }

    
}
