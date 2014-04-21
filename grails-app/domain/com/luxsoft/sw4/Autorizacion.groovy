package com.luxsoft.sw4

import com.luxsoft.sec.User
/**
* Entidad basica en la jerarquia de autorizaciones del sistema
*
**/
class Autorizacion {

	User autorizo
	String descripcion
	String sello  //?

	Date dateCreated
	Date lastUpdated

    static constraints = {
    	sello nullable:true
    }
}
