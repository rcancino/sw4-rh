package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Direccion

class DatosPersonales {
	
	Empleado empleado
	Date fechaDeNacimiento
	String lugarDeNacimiento
	String telefono1
	String telefono2
	String email
	String tipoDeSangre
	String estadoCivil
	String conyugue
	String nombreDelPader
	String nombreDeLaMadre
	
	Direccion direccion
	
	static embedded = ['direccion']

    static constraints = {
		estadoCivil inList:['SOLTERO','CASADO']
    }
}
