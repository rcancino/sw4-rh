package com.luxsoft.sw4.rh


import java.util.Set;

import com.luxsoft.sw4.rh.sat.SatIncapacidad

import org.grails.databinding.BindingFormat
import groovy.transform.EqualsAndHashCode
import groovy.time.TimeCategory

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

	Integer dias

	Date dateCreated
	
	Date lastUpdated

    static constraints = {
    	
    	comentario nullable:true,maxSize:250
    }

	static transients=['dias']
	
	static mapping = {
		fechaInicial type:'date'
		fechaFinal type:'date'
	}

	public Integer getDias(){
		use(TimeCategory){
			def duration= fechaFinal-fechaInicial+1.day
			return duration.days
		}
	}

    String toString(){
    	return "$empleado $tipo  "
    }

   

}
