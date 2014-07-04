package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import grails.transaction.NotTransactional

@Transactional
class VacacionesService {

    @NotTransactional
	def procesar(Asistencia asistencia){
		asistencia.vacaciones=0
		asistencia.partidas.each{
			def found=Vacaciones.find("from Vacaciones v where v.empleado=? and ? in elements(v.dias) ",[asistencia.empleado,it.fecha])
			if(found){
				if(found.pg){
					asistencia.vacacionesp++
				}else{
					it.comentario='VACACIONES'
					it.tipo='VACACIONES'
					asistencia.vacaciones++
				}
				
			}
		}
		def res=asistencia.faltas-asistencia.vacaciones
		asistencia.faltas=res>0?res:0
		
	}
}
