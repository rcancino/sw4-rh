package com.luxsoft.sw4.rh

class Empleado {

	String apellidoPaterno
	String apellidoMaterno
	String nombres
	String curp
	String rfc
    Date alta

    Date dateCreated
    Date lastUpdated


    static constraints = {
    	apellidoPaterno size:1..150
    	apellidoMaterno size:1..150
    	nombres size:1..300
    	curp size:1..25
    	rfc  minSize:12,maxSize:13

    }

    String toString(){
    	return "$nombres $apellidoPaterno $apellidoMaterno"
    }
}
