package com.luxsoft.sw4.rh

import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes='clave')
class ConceptoDeNomina {
	
	String clave
	String descripcion
	String tipo
	Integer claveSat
	Boolean general=false
	String clase  
	Boolean importeExcento=false
	
	Date dateCreated
	Date lastUpdated

    static constraints = {
		clave nullable:false,maxSize:15,unique:true
		descripcion size:1..100
		tipo inList:['PERCEPCION','DEDUCCION']
		//clase inList:['COMPENSACIONES','','','','']
		clase maxSize:30
		importeExcento nullable:true
    }

    String toString(){
    	return "$tipo $clave $descripcion ${general?'(General)':''}"
    }
}
