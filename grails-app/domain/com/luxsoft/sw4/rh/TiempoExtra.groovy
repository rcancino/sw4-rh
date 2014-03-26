package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Periodo

class TiempoExtra {

	ConceptoDeNomina concepto
	Empleado empleado
	NominaPorEmpleado nominaPorEmpleado
	Periodo periodo
	BigDecimal totalDoble
	BigDecimal totalTriple
	String autorizo


	Date dateCreated
	Date lastUpdated
	
	static embedded = ['periodo']

    static constraints = {
    	nominaPorEmpleado nullable:true
    	autorizo blank:false,minSize:5
    }

    static hasMany = [partidas: TiempoExtraDet]
}
