package com.luxsoft.sw4.rh

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode(includes='empleado')
@ToString(includePackage=false,includeNames=true,excludes='dateCreated,lastUpdated')
class NominaPorEmpleado {

	
	Empleado empleado
	BigDecimal salarioDiarioBase
	BigDecimal salarioDiarioIntegrado
	BigDecimal total
	BigDecimal totalGravado
	BigDecimal totalExcento
	
	/* Tiempo extra ?? */
	String comentario

	Date dateCreated
	Date lastUpdated

    static constraints = {
    	comentario nullable:true,maxSize:200
    }
	
	static transients=['antiguedad']

    static belongsTo = [nomina: Nomina]

    static hasMany = [conceptos: NominaPorEmpleadoDet]
	
	Integer getAntiguedad(){
		return (int)Math.floor((nomina?.pago-empleado?.alta)/7)
	}
}
