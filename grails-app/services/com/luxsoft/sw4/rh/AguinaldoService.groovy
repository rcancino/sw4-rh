package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import grails.transaction.NotTransactional
import com.luxsoft.sw4.Periodo

@Transactional
class AguinaldoService {

	@NotTransactional
	def calcular(Integer ejercicio){
		
		def ids=NominaPorEmpleado.executeQuery(
			"select distinct(n.empleado.id) from NominaPorEmpleado n where n.nomina.ejercicio=?",[ejercicio])
		log.info "Calculando aguinaldo para $ids.size empleados del ejercicio $ejercicio"
		ids.each{ 
			def empleado=Empleado.get(it)
			try {
				def aguinaldo=Aguinaldo.findOrCreateWhere(ejercicio: ejercicio,empleado:empleado)
				if(aguinaldo.id==null){
					def periodo=Periodo.getPeriodoAnual(ejercicio)
					aguinaldo.fechaInicial=periodo.fechaInicial
					aguinaldo.fechaFinal=periodo.fechaFinal
				}

				calcular(aguinaldo)
			}
			catch(Exception e) {
				log.error "Error calculando aguinaldo de $empleado ($ejercicio)",e
			}
			
		}
	}


    def calcular(Aguinaldo aguinaldo) {
    	log.info "Calculando aguinaldo: "+aguinaldo
    	aguinaldo.save failOnError:true
    }
}
