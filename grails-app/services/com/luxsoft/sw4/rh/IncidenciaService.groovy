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
		def paternidad=0
		asistencia.partidas.each{
			def found=Incidencia.find("from Incidencia i where i.empleado=? and ? between date(i.fechaInicial) and date(i.fechaFinal)"
				,[asistencia.empleado,it.fecha])
			if(found){
				println 'Incedencia localizada: '+found
				if(found.pagado && (found.tipo=='PERMISO' || found.tipo=='PERMISO_P')){
					it.comentario='INCIDENCIA '+found.tipo
					it.tipo='INCIDENCIA'
					pagadas++
				}
				if(!found.pagado && (found.tipo=='PERMISO' || found.tipo=='PERMISO_P'))	{
					it.comentario='INCIDENCIA '+found.tipo
					it.tipo='INCIDENCIA_F'
					noPagadas++
				}
				
				if(found.tipo=='PATERNIDAD'){
					it.comentario='INCIDENCIA '+found.tipo
					it.tipo='INCIDENCIA'
					paternidad++
				}
				
			}
			
			
		}
		//println' Incidencias pagadas:'+pagadas+ ' paternidad: '+paternidad+' no pagadas'+noPagadas
		def res=asistencia.faltas
		asistencia.faltas=res>0?res:0
		asistencia.incidencias=noPagadas
		asistencia.paternidad=paternidad
	}
}
