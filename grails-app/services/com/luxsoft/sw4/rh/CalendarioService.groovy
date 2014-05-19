package com.luxsoft.sw4.rh

import org.apache.commons.lang.exception.ExceptionUtils;

import com.luxsoft.sw4.Periodo

import grails.transaction.Transactional

@Transactional
class CalendarioService {

    def generarPeriodos(Calendario calendario) {
		assert !calendario.partidas,"El calendario ya esta generado debe eliminar los periodos para volver a generarlo"
		try {
			if(calendario.tipo=='SEMANA'){
				return generarPeriodosSemanales(calendario)
			}else{
				return calendario
			}
			
		} catch (Exception e) {
			e.printStackTrace()
			
		}
		
		
    }
	
	private Calendario generarPeriodosSemanales(Calendario c){
		
		def periodos=Periodo.getPeriodosDelYear(c.ejercicio)
		
		for(Periodo periodo:periodos){
			
			log.info 'Generando periodo: '+periodo
			
			def inicio=periodo.fechaInicial
			def fin=periodo.fechaFinal
			def inicioPeriodo
			def folio=1
			for(Date dia:inicio..fin){
				
				if(!inicioPeriodo) inicioPeriodo=dia
				
				if(dia.day==0){
					def per=new Periodo(inicioPeriodo,dia)
					inicioPeriodo=null
					def partida=new CalendarioDet(
						folio:folio++,
						inicio:per.fechaInicial,
						fin:per.fechaFinal,
						asistencia:new Periodo(per.fechaInicial-7,per.fechaFinal-7)
						)
					c.addToPartidas(partida)
					println 'Segmento: '+per
					
				}
				if(dia==fin){
					def per=new Periodo(inicioPeriodo,fin)
					inicioPeriodo=null
					def partida=new CalendarioDet(
						folio:folio++,
						inicio:per.fechaInicial,
						fin:per.fechaFinal,
						asistencia:new Periodo(per.fechaInicial-7,per.fechaFinal-7)
						)
					c.addToPartidas(partida)
					println 'Segmento: '+per
				}
			}
		}
		
		c.save failOnError:true
		return c
	}
	
}
