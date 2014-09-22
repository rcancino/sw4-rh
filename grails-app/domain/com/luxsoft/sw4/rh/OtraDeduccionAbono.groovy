package com.luxsoft.sw4.rh


import groovy.transform.EqualsAndHashCode


@EqualsAndHashCode(includes='nominaPorEmpleadoDet,fecha,importe')
class OtraDeduccionAbono {
	
	Date fecha
	BigDecimal importe=0.0
	String comentario
	NominaPorEmpleadoDet nominaPorEmpleadoDet
	
	Date dateCreated
	Date lastUpdated

	static constraints = {
		comentario nullable:true
		nominaPorEmpleadoDet nullable:true
		
	}

	static belongsTo = [otraDeduccion: OtraDeduccion]
}
