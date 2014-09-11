package com.luxsoft.sw4.rh

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes='nominaPorEmpleadoDet,fecha,importe')
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
