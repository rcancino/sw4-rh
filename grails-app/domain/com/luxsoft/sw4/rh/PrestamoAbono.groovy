package com.luxsoft.sw4.rh

class PrestamoAbono {

	Date fecha
	BigDecimal importe=0.0
	String comentario
	
	BigDecimal saldoAnterior=0.0
	
	BigDecimal saldo=0.0

	Date dateCreated
	Date lastUpdated

    static constraints = {
    	comentario nullable:true
		
    }

    static belongsTo = [prestamo: Prestamo]
}
