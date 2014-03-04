package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Direccion

class Ubicacion {

    String clave
	String descripcion
	Direccion direccion


	Date dateCreated
	Date lastUpdated

    static constraints = {
    	clave blank:false,maxSize:20,unique:true
    	descripcion size:1..300

    }

    static embedded = ['direccion']

    String toString(){
    	return "$descripcion ($clave)"
    }
}
