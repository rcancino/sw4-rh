package com.luxsoft.sw4.rh



import spock.lang.*
import com.luxsoft.sw4.Empresa

/**
 *
 */
class NominaIntegrationSpec extends Specification {

	def empresa

    def setup() {
    	empresa=Empresa.build(clave:'PAPEL')
    }

    def cleanup() {
    }

    void "Salvar una nomina nueva"() {
    	given:'Una nomina nueva'
    	def nomina=Nomina.buildWithoutSave(empresa:empresa,folio:1)

    	when:'Salvamos la entidad'
    	nomina.save()

    	then:'La nomina persiste exitosamente en la base de datos'
    	nomina.errors.errorCount==0
    	nomina.id
    	Nomina.get(nomina.id).folio==1
    	println nomina

    }
}
