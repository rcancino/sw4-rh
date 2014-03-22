package com.luxsoft.sw4.rh

class Puesto implements Serializable{

	String clave
	String descripcion
	

	Date dateCreated
	Date lastUpdated

    static constraints = {
    	clave blank:false,maxSize:20,unique:true
    	descripcion size:1..300
    }

    String toString(){
    	return "$descripcion ($clave)"
    }
}
