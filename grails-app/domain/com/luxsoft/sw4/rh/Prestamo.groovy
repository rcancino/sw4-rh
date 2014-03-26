package com.luxsoft.sw4.rh

class Prestamo {

	ConceptoDeNomina concepto
	Empleado empleado
	
	Date alta
	String autorizo
	String comentario

	BigDecimal total
	BigDecimal pagos
	BigDecimal saldo

	BigDecimal descuento

	Date dateCreated
	Date lastUpdated
	

    static constraints = {
    	comentario nullable:true
    }

    static hasMany = [partidas: PrestamoPago]
}
