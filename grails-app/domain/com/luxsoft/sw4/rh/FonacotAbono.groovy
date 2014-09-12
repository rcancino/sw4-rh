package com.luxsoft.sw4.rh

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode(includes='nominaPorEmpleadoDet,fecha,importe')
@ToString(includeNames=true,includePackage=false)
class FonacotAbono {

    Date fecha
	
	BigDecimal importe=0.0
	
	NominaPorEmpleadoDet nominaPorEmpleadoDet

	String comentario

	Date dateCreated
	Date lastUpdated

    static constraints = {
    	comentario nullable:true
		nominaPorEmpleadoDet nullable:true
		
    }

    static belongsTo = [prestamo: Fonacot]
}
