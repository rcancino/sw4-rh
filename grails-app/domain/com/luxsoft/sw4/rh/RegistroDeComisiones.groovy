package com.luxsoft.sw4.rh

class RegistroDeComisiones {
	
	Empleado empleado
	CalendarioDet calendarioDet
	BigDecimal importe
	String comentario
	NominaPorEmpleadoDet nominaPorEmpleadoDet
	
	Date dateCreated
	Date lastUpdated
	

    static constraints = {
		nominaPorEmpleadoDet nullable:true
    }
	
	String toString(){
		return "$empleado $calendarioDet?.calendario?.tipo - $calendarioDet?.folio"
	}
}
