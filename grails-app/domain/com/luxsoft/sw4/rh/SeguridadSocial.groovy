package com.luxsoft.sw4.rh

/**
 * 
 * @author RUBEN
 * @todo Quitar la propiedad de alta, no parece ser requerida
 *
 */
class SeguridadSocial implements Serializable{
	
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
		numero maxSize:15
		alta nullable:true
    }
}
