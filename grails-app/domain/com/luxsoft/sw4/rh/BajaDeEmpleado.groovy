package com.luxsoft.sw4.rh

class BajaDeEmpleado implements Serializable{
	
	Empleado empleado
	Date fecha
	String comentario
	MotivoDeBaja motivo
	MotivoDeSeparacion causa
	Date dateCreated
	Date lastUpdated

    static constraints = {
		comentario nullable:true
		
    }
}
