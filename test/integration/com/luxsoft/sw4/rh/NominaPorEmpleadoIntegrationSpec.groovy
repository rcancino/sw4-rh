package com.luxsoft.sw4.rh



import spock.lang.*
import com.luxsoft.sw4.Empresa

/**
 *
 */
class NominaPorEmpleadoIntegrationSpec extends Specification {

	
    def setup() {
    	
    }

    def cleanup() {
    }

    void "Salvar partidas de una nomina"() {
    	given:'Una nomina nueva'
    	def nomina=Nomina.build(folio:1).save(failOnError:true)

    	when:'Creamos una nomina por empleado'
    	def det=NominaPorEmpleado.build(comentario:'comentario 1',nomina:nomina)

    	then: 'la nomina por empleado es persistida exitosamenteo'
    	det.id
    	det.nomina==nomina
    	nomina.partidas.find{it.comentario=='comentario 1'}
    	println det

    }
}
