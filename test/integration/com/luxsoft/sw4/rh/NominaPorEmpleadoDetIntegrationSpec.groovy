package com.luxsoft.sw4.rh



import spock.lang.*

/**
 *
 */
class NominaPorEmpleadoDetIntegrationSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "Salvar una instancia nueva"() {
    	given:'Una Nomina por empleado'
    	def nominaPorEmpleado=NominaPorEmpleado.build()
    	and: 'Un concepto de nomina por empleado nuevo'
    	def det=NominaPorEmpleadoDet.build(parent:nominaPorEmpleado,comentario:'DEMO').save(failOnError:true)
    	when:'Salvamos la nominaPorEmpleado'
    	nominaPorEmpleado.save(flush:true)
    	
    	then:'El detalle es persistido en la base de datos'
    	det.id
    	NominaPorEmpleadoDet.get(det.id).comentario=='DEMO'


    }
}
