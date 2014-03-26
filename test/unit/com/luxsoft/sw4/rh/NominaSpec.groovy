package com.luxsoft.sw4.rh


import spock.lang.Specification
import spock.lang.Unroll
import spock.lang.Shared
import grails.test.mixin.TestFor
import grails.test.mixin.Mock

import com.luxsoft.sw4.*
import grails.buildtestdata.mixin.Build


/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Nomina)
@Build([Nomina,Empresa])
@Mock([Nomina,Empresa])
class NominaSpec extends Specification {

	//@Shared
	def empresa

    def setup() {
    	empresa=Empresa.build(clave:'PAPEL')
    }

    def cleanup() {
    }

    @Unroll
    void "Dias pagados del #f1 al #f2 = #dias "() {
    	expect:
    	def nomina=new Nomina(periodo:new Periodo(f1,f2))
    	nomina.diasPagados==dias

    	where:
    	f1|f2||dias
    	'01/01/2014'|'15/01/2014'||15
    	'01/01/2014'|'07/01/2014'||7

    }

    void "Validacion basica"(){
    	given:'Una nomina '
    	def nomina=Nomina.build(empresa:empresa,periodicidad:'QUINCENAL')

    	when:'Se  valida'
    	nomina.validate()
    	then:'No hay errores'
    	!nomina.hasErrors()
    	
    }
}
