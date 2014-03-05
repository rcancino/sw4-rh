package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Direccion
import com.luxsoft.sw4.Empresa

class Ubicacion {

    String clave
	String descripcion
	Direccion direccion
    Empresa empresa


	Date dateCreated
	Date lastUpdated

    static constraints = {
        empresa nullable:false
    	clave blank:false,maxSize:20,unique:true
    	descripcion size:1..300

    }

    static embedded = ['direccion']

    String toString(){
    	return "$clave ($empresa.clave)"
    }
}
