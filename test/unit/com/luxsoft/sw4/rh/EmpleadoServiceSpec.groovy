package com.luxsoft.sw4.rh

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.buildtestdata.mixin.Build

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(EmpleadoService)
@Build(Empleado)
class EmpleadoServiceSpec extends Specification {

    def setup() {}

    def cleanup() {}

    void "Empleados validos son persistidos a la base de datos"() {
    	given:'Un empleado nuevo'
    	def empleado=Empleado.build(clave:'TEST').save(failOnError:true)

    	when:'Modificamos una propiedad'
    	empleado.apellidoPaterno='NUEVO APELLIDO'
    	def found=service.updateEmpleado(empleado)
    	

    	then:'El empleado es actualizado sarisfactoriamente'
    	found.errors.errorCount==0
    	Empleado.findByClave('TEST').apellidoPaterno=='NUEVO APELLIDO'
    	println found
    }

    void "Empleados invalidos generan error"(){
    	given:'Un empleado nuevo'
    	def empleado=Empleado.build(clave:'TEST').save(failOnError:true)

    	when:'una modificacion invalida es tratada de persistir'
    	empleado.alta=null
    	def found=service.updateEmpleado(empleado)

    	then:'Una exception es generada y el cambio no es persistido'
    	thrown(EmpleadoException)


    }
}
