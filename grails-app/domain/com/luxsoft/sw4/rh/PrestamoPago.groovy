package com.luxsoft.sw4.rh

class PrestamoPago {

	NominaPorEmpleado nominaPorEmpleado
	BigDecimal importe

    static constraints = {
    }

    static belongsTo = [prestamo: Prestamo]
}
