package com.luxsoft.sw4.rh

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes='concepto')
class NominaPorEmpleadoDet {

	ConceptoDeNomina concepto
	BigDecimal importeGrabado
	BigDecimal importeExcento
	String comentario

	Date dateCreated
	Date lastUpdated

	static belongsTo = [parent: NominaPorEmpleado]	

    static constraints = {
    	
    }

    String toString(){
    	"$tipo Grabado: $importeGrabado Excento: $importeExcento "
    }
}
