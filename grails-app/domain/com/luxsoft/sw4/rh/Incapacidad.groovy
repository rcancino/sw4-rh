package com.luxsoft.sw4.rh


import java.util.Set;

import com.luxsoft.sw4.rh.sat.SatIncapacidad

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes='empleado,referenciaImms')
class Incapacidad {

	static searchable = true
	
	Empleado empleado
	
	String referenciaImms
	
	String comentario
	
	SatIncapacidad tipo
	
	Set dias=new HashSet()

	Date dateCreated
	
	Date lastUpdated

    static constraints = {
    	
    	comentario nullable:true,maxSize:250
    }

	static hasMany = [dias:Date]
	
	static mapping = {
		hasMany joinTable: [name: 'incapacidad_dias',
			key: 'incapacidad_id',
			column: 'fecha',
			type: "date"]
	}

    String toString(){
    	return "$empleado $tipo  "
    }

   

}
