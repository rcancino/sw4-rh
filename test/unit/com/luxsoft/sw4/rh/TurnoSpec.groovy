package com.luxsoft.sw4.rh

import grails.test.mixin.TestFor

import spock.lang.Specification
import spock.lang.Unroll
import grails.buildtestdata.mixin.Build

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Turno)
@Build(Turno)
class TurnoSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    @Unroll
    void "Turno debe tener la propiedad #property"() {
    	given:'Un turno nuevo'
    }
}
