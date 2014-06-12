package com.luxsoft.sw4.rh

import grails.test.mixin.TestFor
import spock.lang.Specification
import com.luxsoft.sw4.Periodo

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Incapacidad)
class IncapacidadSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "Numero de dias"() {
    	given:' Una periodo especifico'
    	def periodo=new Periodo('01/04/2014','30/04/2014')
    	and: 'Una incapacidad nueva'
    	def incapacidad=new Incapacidad()
    	incapacidad.fechaInicial=periodo.fechaInicial
    	incapacidad.fechaFinal=periodo.fechaFinal

    	when:'Calcualmos los dias'
    	def dias=incapacidad.dias

    	then:'El los dias de incapacidad'
    	dias==30
    }
}
