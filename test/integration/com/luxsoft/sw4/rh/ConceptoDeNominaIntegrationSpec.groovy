package com.luxsoft.sw4.rh



import spock.lang.*

/**
 *
 */
class ConceptoDeNominaIntegrationSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "Salvar un concepto"() {
    	given:'Un concepto nuevo'
    	def concepto=new ConceptoDeNomina(clave:'CON1',descripcion:'SUELDO',tipo:'PERCEPCION',claveSat:10)

    	when:'Salvamos el concepto'
    	concepto.save()

    	then:' El concepto se persiste exitosamente en la base de datos'
    	concepto.errors.errorCount==0
    	concepto.id
    	ConceptoDeNomina.get(concepto.id).clave=='CON1'

    }
    void "Actualizar un concepto"(){
    	given:'Un concepto existente'
    	def concepto=ConceptoDeNomina.build().save(failOnError:true)

    	when:'Actualiamos una propiedad'
    	def found=ConceptoDeNomina.get(concepto.id)
    	assert found
    	found.descripcion='MODIFICADO'
    	found.save(failOnError:true)

    	then:'El cambio se persiste en la base de datos'
    	ConceptoDeNomina.get(found.id).descripcion=='MODIFICADO'
    }
    void "Eliminar un concepto"(){
    	given:'Un concepto existente'
    	def concepto=ConceptoDeNomina.build().save(failOnError:true)
    	

    	when:'Eliminamos el concepto'
    	def found=ConceptoDeNomina.get(concepto.id)
    	assert found
    	found.delete(flush:true)

    	then:'El concepto es eliminado de la base de datos'
    	!ConceptoDeNomina.exists(found.id)
    }
}
