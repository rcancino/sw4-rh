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
			//println 'Procesando inacapacidades para: '+it.fecha
			def found=Incapacidad.find("from Incapacidad i where i.empleado=? and ? between date(i.fechaInicial) and date(i.fechaFinal)"
				,[asistencia.empleado,it.fecha])
			if(found){
				it.comentario='INCAPACIDAD'
				if(found.tipo){
					
					switch(found.tipo.clave) {
						case 2:
							it.comentario+= ' EG'
							break
						case 3:
							it.comentario+= ' MAT'
							break
						case 1:
							if(found?.comentario?.toUpperCase().startsWith("TRAYECTO")){
								it.comentario+=' RTT'
							}else{
								it.comentario+=' RTE'
							}
					}
				}
				it.tipo='INCAPACIDAD'
				asistencia.incapacidades++
			}
		}
		
	}
	
	
}
