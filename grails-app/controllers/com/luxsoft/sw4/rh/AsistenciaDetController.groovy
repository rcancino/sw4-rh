package com.luxsoft.sw4.rh

import java.sql.Time;

import grails.plugin.springsecurity.annotation.Secured


@Secured(['ROLE_ADMIN'])
class AsistenciaDetController {
	
	static scaffold=true
	def asistenciaService
	
	def update(Long id){
		println 'Actualizando asistencia det: '+params
		def det=AsistenciaDet.get(id)
		
		det.entrada1=getDateTime(params.entrada1,det.fecha)
		det.salida1=getDateTime(params.salida1,det.fecha)
		det.entrada2=getDateTime(params.entrada2,det.fecha)
		det.salida2=getDateTime(params.salida2,det.fecha)
		//det.manual=params.manual
		//det.pagarTiempoExtra=params.pagarTiempoExtra
		bindData(det,params,[include:['pagarTiempoExtra','manual','excentarChecadas']])
		det.save(flush:true)
		
		//asistenciaService.actualizarAsistencia(det.asistencia)
		//println  det.entrada1
		//println 'Actualizando asistenciaDet: '+params
		redirect controller:'asistencia', action:'show', params:[id:det.asistencia.id]
		
	}
	
	private getDateTime(String val,Date date){
		log.debug "Calculando Time para : ${val} de fecha: ${date}"
		def fecha=date.format('dd/MM/yyyy')
		if(val){
			try{
				def time=fecha+" "+val
				def res=Date.parse("dd/MM/yyyy HH:mm",time)
				def newTime= new Time(res.getTime())
				log.debug 'Time calculado: '+newTime
				return newTime
			}catch(Exception ex){
				log.error ex
				return null
			}
			
			
		}
		return null
		
	}
   
}


