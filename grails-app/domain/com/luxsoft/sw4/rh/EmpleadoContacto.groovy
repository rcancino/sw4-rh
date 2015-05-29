package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Direccion;

class EmpleadoContacto implements Serializable{
	
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
		parentesco inList:['PADRE','MADRE','CONYUGE','HIJO','HERMANO','CONOCIDO','ABUELO','ABUELA','TIO','TIA','SUEGRO','SUEGRA','FAMILIAR','AMIGA','AMIGO']
		parentesco maxSize:50
		comentario nullable:true
		direccion nullable:true
    }
}
