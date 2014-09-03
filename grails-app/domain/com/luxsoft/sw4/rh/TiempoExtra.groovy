package com.luxsoft.sw4.rh


import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode

import org.grails.databinding.BindingFormat

@ToString(includes='empleado,comentario',includeNames=true,includePackage=false)
//@EqualsAndHashCode(includes="empleado,ejercicio,id")
class TiempoExtra {
	
	
	Empleado empleado
	
	Integer ejercicio
	
	@BindingFormat("dd/MM/yyyy")
	Date fecha
	
	String comentario

	Date dateCreated
	
	Date lastUpdated

	static constraints = {
		comentario nullable:true
	}
	
	static hasMany = [partidas:TiempoExtraDet]
	
	static mapping = {
		fecha type:'date'
		partidas cascade: "all-delete-orphan"
	}

    
}
