package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Direccion;

class EmpleadoContacto {
	
	Empleado empleado
	String nombre
	Direccion direccion
	String parentesco
	String telefono1
	String telefono2
	String comentario
	
	static embedded = ['direccion']
	
	

    static constraints = {
		telefono1 nullable:true
		telefono2 nullable:true
		//parentesco inList:['CONYUGE','HIJO','PADRE','HERMANO','FAMILIAR','AMIGO','CONOCIDO']
		parentesco maxSize:50
		comentario nullable:true
		direccion nullable:true
    }
}
