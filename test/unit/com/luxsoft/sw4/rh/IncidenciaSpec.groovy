package com.luxsoft.sw4.rh

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll;

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Incidencia)
class IncidenciaSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }
	/*
    @Unroll
    void "Descuento de #horas hrs salario de #salario jornada: #jornada hrs en un periodo de #dias dias es de #res"() {
    	expect:
    	def det=new IncidenciaDet(horas:horas,horasJornada:jornada)
    	det.actualizar(salario,dias).importe==res

    	where:
    	horas|jornada|salario|dias||res
    	1.5|8|100|7||18.75
    }

    @Unroll
    void """Descuento proporcinal de #horas hrs  salario:#salario jornada: 
         #jornada hrs periodo de #dias dias desacanso de #descanso dias es de #res"""() {
    	expect:
    	def det=new IncidenciaDet(horas:horas,horasJornada:jornada)
    	Math.round(det.actualizarImporteProporcional(salario,dias,descanso).importeProporcional)==Math.round(res)

    	where:
    	horas|jornada|salario|dias|descanso||res
    	1.5|8|100|7|1||3.13
    }
    */
}
