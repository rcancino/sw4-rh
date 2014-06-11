package com.luxsoft.sw4.rh


import java.util.Set;

import com.luxsoft.sw4.rh.sat.SatIncapacidad

import org.grails.databinding.BindingFormat
import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes='empleado,referenciaImms')
class Incapacidad {

	static searchable = true
	
	Empleado empleado
	
	String referenciaImms
	
	String comentario
	
	SatIncapacidad tipo
	
	@BindingFormat("dd/MM/yyyy")
	Date fechaInicial

	@BindingFormat("dd/MM/yyyy")
	Date fechaFinal

	Date dateCreated
	
	Date lastUpdated

    static constraints = {
    	
    	comentario nullable:true,maxSize:250
    }

	static hasMany = [dias:Date]
	
	static mapping = {
		
	}

    String toString(){
    	return "$empleado $tipo  "
    }

   

}
