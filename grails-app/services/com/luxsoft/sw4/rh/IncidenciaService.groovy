package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import grails.transaction.NotTransactional

@Transactional
class IncidenciaService {

    @NotTransactional
	def procesar(Asistencia asistencia){
		asistencia.incidencias=0
		asistencia.partidas.each{
			println 'Procesando incidencias para: '+it.fecha
			def found=Incidencia.find("from Incidencia i where i.empleado=? and ? between date(i.fechaInicial) and date(i.fechaFinal)"
				,[asistencia.empleado,it.fecha])
			if(found){
				it.comentario='INCIDENCIA'
				it.tipo='INCIDENCIA'
				asistencia.incidencias++
			}
		}
		
	}
}
