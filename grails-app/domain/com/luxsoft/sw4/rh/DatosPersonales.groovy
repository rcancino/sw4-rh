package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Direccion

class DatosPersonales implements Serializable{
	
	Empleado empleado
	String lugarDeNacimiento
	String telefono1
	String telefono2
	String email
	String tipoDeSangre
	String estadoCivil
	String conyuge
	String nombreDelPader
	String nombreDeLaMadre
	
	Direccion direccion
	
	static embedded = ['direccion']

    static constraints = {
		estadoCivil inList:['SOLTERO','CASADO','UNION_LIBRE']
		conyuge nullable:true
		nombreDelPader nullable:true
		nombreDeLaMadre nullable:true
		tipoDeSangre nullable:true
		email nullable:true
		telefono1 nullable:true
		telefono2 nullable:true
		lugarDeNacimiento nullable:true
    }
}
