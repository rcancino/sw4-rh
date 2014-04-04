package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Direccion
import com.luxsoft.sw4.Empresa


class Ubicacion implements Serializable{

    String clave
	String descripcion
	Direccion direccion
    Empresa empresa

    static constraints = {
        empresa nullable:false
    	clave blank:false,maxSize:20,unique:true
    	descripcion size:1..300
		direccion nullable:true

    }

    static embedded = ['direccion']

    String toString(){
    	return "$clave (${empresa?.clave})"
    }
}
