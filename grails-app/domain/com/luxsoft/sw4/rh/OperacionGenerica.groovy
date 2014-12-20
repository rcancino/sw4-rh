package com.luxsoft.sw4.rh

import java.util.Date;

class OperacionGenerica {
	
	Empleado empleado
	String tipo
	ConceptoDeNomina concepto
	
	BigDecimal importeGravado=0.00
	BigDecimal importeExcento=0.00
	
	CalendarioDet calendarioDet
	
	NominaPorEmpleadoDet nominaPorEmpleadoDet
	
	String comentario
	
	Date dateCreated
	Date lastUpdated

    static constraints = {
		tipo inList:['PERCEPCION','DEDUCCION']
		nominaPorEmpleadoDet nullable:true
		comentario nullable:true,maxSize:250
    }
}
