package com.luxsoft.sw4.rh

class Compensacion {

	Empleado empleado
	ConceptoDeNomina concepto
	Date fecha
	NominaPorEmpleado nominaPorEmpleado
	BigDecimal importe
	String comentario

	Date dateCreated
	Date lastUpdated

    static constraints = {
    	nominaPorEmpleado nullable:true
    	comentario nullable:true
    }
}
