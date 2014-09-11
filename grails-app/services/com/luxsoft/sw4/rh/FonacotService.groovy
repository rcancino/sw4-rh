package com.luxsoft.sw4.rh

import grails.transaction.Transactional

@Transactional
class FonacotService {

    def salvar(Fonacot fonacot) {
    	log.info 'Salvando credito fonacot'
    	def empleado =fonacot.empleado
    	def tipo=empleado.salario.periodicidad
    	if(tipo=='SEMENAL'){
    		def r=fonacot.retencionMensual/4
    		fonacot.retencionMensual=r
		}else if(tipo=='QUINCENAL'){
			def r=fonacot.retencionMensual/2
    		fonacot.retencionMensual=r
		}
		fonacot.save failOnError:true

    }
}
