package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import grails.transaction.NotTransactional

@Transactional
class IncidenciaService {

    @NotTransactional
	def procesar(Asistencia asistencia){
		asistencia.incidencias=0
		def pagadas=0
		def noPagadas=0
		asistencia.partidas.each{
			def found=Incidencia.find("from Incidencia i where i.empleado=? and ? between date(i.fechaInicial) and date(i.fechaFinal)"
				,[asistencia.empleado,it.fecha])
			if(found){
				if(found.pagado){
					it.comentario='INCIDENCIA '+found.tipo
					it.tipo='INCIDENCIA'
					pagadas++
				}else{
					it.comentario='INCIDENCIA '+found.tipo
					it.tipo='INCIDENCIA_F'
					noPagadas++
				}
				
			}
		}
		def res=asistencia.faltas-pagadas
		asistencia.faltas=res>0?res:0
		asistencia.incidencias=noPagadas
	}
}
