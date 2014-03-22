package com.luxsoft.sw4.rh



import spock.lang.*

/**
 *
 */
class PuestoIntegrationSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "Salvar un puesto nuevo"() {
    	given:'Un puesto nuevo'
    	def puesto=new Puesto(clave:'PUESTO 1',descripcion:'PUESTO DE TRABAJO')

    	when:' Salvamos la entidad'
    	puesto.save()

    	then:' El puesto es persistido exitosamente en la base de datos'
    	puesto.errors.errorCount==0
    	puesto.id
    	Puesto.get(puesto.id).clave=='PUESTO 1'
    }

    void "Actualizar un puesto existente"(){
    	given:'Un puesto existente'
    	def puesto=Puesto.build().save(failOnError:true)

    	when:'El actualizamo alguan propiedad'
    	def found=Puesto.get(puesto.id)
    	assert found
    	found.clave="PUESTO P1"
    	found.save(failOnError:true)

    	then:' Los cambios son persistidos en la base de datos'
    	Puesto.get(found.id).clave=='PUESTO P1'


    }
}
