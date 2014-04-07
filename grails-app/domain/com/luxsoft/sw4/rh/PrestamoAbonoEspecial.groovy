package com.luxsoft.sw4.rh

class PrestamoAbonoEspecial {

	BigDecimal importe
	String comentario

	Date dateCreated
	Date lastUpdated

    static constraints = {
    	comentario nullable:true
    }

    static belongsTo = [prestamo: Prestamo]
}
