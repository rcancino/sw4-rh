package com.luxsoft.sw4.rh

import groovy.transform.ToString

@ToString(includePackage=false,includeNames=true,excludes='dateCreated,lastUpdated')
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
