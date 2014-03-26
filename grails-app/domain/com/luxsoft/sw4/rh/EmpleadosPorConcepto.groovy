package com.luxsoft.sw4.rh

class EmpleadosPorConcepto {

	ConceptoDeNomina concepto



	Date dateCreated
	Date lastUpdated

    static constraints = {
    }

    static hasMany = [empleados: Empleado]
}
