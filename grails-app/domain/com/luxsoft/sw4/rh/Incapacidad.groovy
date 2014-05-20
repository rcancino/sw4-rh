package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Periodo
import com.luxsoft.sw4.rh.sat.SatIncapacidad
import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes='empleado,referenciaImms')
class Incapacidad {

	static searchable = true
	
	Empleado empleado
	
	String referenciaImms
	
	String comentario
	
	SatIncapacidad tipo
	
	Integer dias

	Date dateCreated
	
	Date lastUpdated

    static constraints = {
    	
    	comentario nullable:true,maxSize:250
    }
	
	static hasMany = [partidas:IncapacidadDet]

    static transients = ['dias']
	
	static mapping = {
		partidas cascade: "all-delete-orphan"
	}

    String toString(){
    	return "$empleado $tipo  "
    }

   

}
