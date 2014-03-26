package com.luxsoft.sw4



import spock.lang.Specification
import spock.lang.Shared
import spock.lang.Unroll
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin

@TestMixin(GrailsUnitTestMixin)
class PeriodoSpec extends Specification {

	@Shared
	def dateFormat

    def setup() {
    	dateFormat='dd/MM/yyyy'
    }

    void "Probar validacion"(){
    	when:'Fecha final  es nulo'
    	def periodo=new Periodo()
    	periodo.fechaFinal=null

    	then:'La validacion falla'
    	!periodo.validate()
    	periodo.hasErrors()
    	periodo.errors.getFieldError('fechaFinal').code=='nullable'
    	
    	
    	//Probar fecha final superior o igual a fecha inicial
    	
    	when:'Fecha final>fecha inicial'
    	periodo=new Periodo('01/01/2014','31/12/2013')

    	then:'La validacion falla'
    	!periodo.validate()
    	periodo.hasErrors()
    	periodo.errors.getFieldError('fechaFinal').code=='fechaFinal.anteriorAFechaInicial'
    	

    }
    
    @Unroll
    void "Periodo del #f1 al #f2 es valido:#res"(){
    	expect:
    	def periodo=new Periodo(f1,f2)
    	periodo.validate()==res

    	where:
    	f1|f2||res
    	'01/03/2014'|'17/03/2014'||true
    	'18/03/2014'|'31/03/2013'||false
    	'18/03/2014'|'19/03/2014'||true
    	'18/03/2014'|'17/03/2014'||false

    }

    
    @Unroll
    void "Creae un periodo  del #f1 al #f2 "(String f1,String f2) {
    	expect:
    	def periodo=new Periodo(f1,f2)
    	periodo.with{
    		fechaInicial==Date.parse(dateFormat,f1)
    		fechaFinal==Date.parse(dateFormat,f2)


    	}
    	//println periodo.toString()
    	//println periodo.dias()

    	where:
    	f1|f2
    	'01/03/2014'|'17/03/2014'
    	'18/03/2014'|'31/03/2014'

    }

    @Unroll
    void "Evaluar el numero de dias entre el #f1 al #f2"(){
    	expect:
    	def periodo=new Periodo(f1,f2)
    	periodo.dias()==res
    	//println 'Dias: '+periodo.dias()

    	where:
    	f1|f2||res
    	'01/03/2014'|'17/03/2014'||16
    	'18/03/2014'|'31/03/2014'||13


    }
}
