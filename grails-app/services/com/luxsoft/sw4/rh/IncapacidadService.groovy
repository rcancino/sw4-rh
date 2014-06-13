package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import grails.transaction.NotTransactional

@Transactional
class IncapacidadService {

    def salvar(Incapacidad incapacidad) {
		incapacidad.save failOnError:true
		return incapacidad
    }
	
	def eliminar(Incapacidad incapacidad) {
		incapacidad.delete flush:true
		return true
	}

	@NotTransactional
	def procesar(Asistencia asistencia){
		asistencia.incapacidades=0
		asistencia.partidas.each{
			println 'Procesando inacapacidades para: '+it.fecha
			def found=Incapacidad.find("from Incapacidad i where i.empleado=? and ? between date(i.fechaInicial) and date(i.fechaFinal)"
				,[asistencia.empleado,it.fecha])
			if(found){
				it.comentario='INCAPACIDAD'
				it.tipo='INCAPACIDAD'
				asistencia.incapacidades++
			}
		}
		
	}
	
	
}
