package com.luxsoft.sw4.rh

class BajaDeEmpleado {
	
	Empleado empleado
	Date fecha
	String motivo
	String comentario
	
	Date dateCreated
	Date lastUpdated

    static constraints = {
		motivo inList:['RENUNCIA','ABANDONO TRABAJO','PENSION','OTROS']
		comentario nullable:true
    }
}
