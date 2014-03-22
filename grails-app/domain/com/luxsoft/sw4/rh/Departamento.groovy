package com.luxsoft.sw4.rh

import groovy.transform.EqualsAndHashCode;

@EqualsAndHashCode(includes=['clave'])
class Departamento implements Serializable{

	String clave
	String descripcion

    static constraints = {
    	clave blank:false,maxSize:20,unique:true
    	descripcion nullable:false,size:1..300

    }

    String toString(){
    	return "$descripcion ($clave)"
    }

    
}
