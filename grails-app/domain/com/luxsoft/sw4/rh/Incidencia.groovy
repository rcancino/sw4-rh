package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Autorizacion
import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode

import org.grails.databinding.BindingFormat

@ToString(includes='empleado,fecha,tipo,autorizacion',includeNames=true,includePackage=false)
@EqualsAndHashCode(includes="empleado,fecha,tipo")
class Incidencia {

	static searchable = true
	
	Empleado empleado
	
	@BindingFormat("dd/MM/yyyy")
	Date fecha
	
	Autorizacion autorizacion
	
	String tipo
	
	String comentario
	
	

	Date dateCreated
	Date lastUpdated

	static constraints = {
		comentario nullable:true,maxSize:250
		autorizacion nullable:true
		tipo inList:['FALTA','RETARDO','PERMISO']
	}
	
	//static hasMany = [dias:Date]
	
	
	
	static mapping = {
		
		fecha type:'date'
	}
    
}
