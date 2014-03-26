package com.luxsoft.sw4.rh

class SeguridadSocial {
	
	Empleado empleado
	String numero
	Date alta
	String turno
	String unidadMedica
	String comentario

    static constraints = {
		turno inList:['MATUTINO','NOCTURNO','MIXTO']
		unidadMedica nullable:true
		comentario nullable:true,maxSize:300
    }
}
