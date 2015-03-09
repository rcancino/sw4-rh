package com.luxsoft.sw4.rh

import grails.transaction.Transactional

@Transactional
class FonacotService {

    def salvar(Fonacot fonacot) {
    	log.info 'Salvando credito fonacot'
    	def empleado =fonacot.empleado
    	def tipo=empleado.salario.periodicidad
    	if(tipo=='SEMANAL'){
    		def r=(fonacot.retencionMensual/4)/7
    		fonacot.retencionDiaria=r
		}else if(tipo=='QUINCENAL'){
			def r=(fonacot.retencionMensual/2)/15
    		fonacot.retencionDiaria=r
		}else{
			log.info 'No eixste regla para tipo: '+tipo
		}
		fonacot.save failOnError:true

    }
}
