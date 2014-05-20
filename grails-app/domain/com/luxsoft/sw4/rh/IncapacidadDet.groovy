package com.luxsoft.sw4.rh

import groovy.transform.EqualsAndHashCode

import org.grails.databinding.BindingFormat

@EqualsAndHashCode(includes="fecha")
class IncapacidadDet {
	
	@BindingFormat("dd/MM/yyyy")
	Date fecha
	
	String comentario
	
	static belongsTo = [incapacidad: Incapacidad]

    static constraints = {
		comentario nullable:true 
    }
	
	static mapping = {
		fecha type:'date'
	}
	
	String toString() {
		return " $fecha $comentario"
	}
}
