package com.luxsoft.sw4.rh

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes='concepto')
class NominaPorEmpleadoDet {

	ConceptoDeNomina concepto
	BigDecimal importeGravado=0.00
	BigDecimal importeExcento=0.00
	String comentario
	NominaPorEmpleado parent

	Date dateCreated
	Date lastUpdated

	static belongsTo = [parent: NominaPorEmpleado]	

    static constraints = {
    	
    }
	
	static transients = ['total']

    String toString(){
    	"$concepto Grabado: $importeGravado Excento: $importeExcento "
    }
	
	BigDecimal getTotal() {
		return importeExcento+importeGravado
	}
}
