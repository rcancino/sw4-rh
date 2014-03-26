package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Periodo
import com.luxsoft.sw4.rh.sat.SatIncapacidad

class Incapacidad {

	ConceptoDeNomina concepto
	Empleado empleado
	Periodo periodo
	NominaPorEmpleado nominaPorEmpleado
	String referenciaImms
	String comentario
	SatIncapacidad tipo
	Integer dias
	BigDecimal salarioBase
	BigDecimal importe

	Date dateCreated
	Date lastUpdated

    static constraints = {
    	nominaPorEmpleado nullable:true
    	comentario nullable:true,maxSize:250
    }

    static transients = ['dias']
	
	static embedded = ['periodo']

    String toString(){
    	return " $tipo.descripcion  $concepto.clave $empleado Dias:$dias Importe:$importe"
    }

    Incapacidad actualizar(){
    	this.importe=dias*salarioBase
    	return this
    }


}
