package com.luxsoft.sw4

import com.luxsoft.sec.User

/**
* Entidad basica en la jerarquia de autorizaciones del sistema
*
**/
class Autorizacion {

	User autorizo
	String descripcion
	String modulo
	String tipo

	Date dateCreated
	Date lastUpdated

    static constraints = {
    	modulo nullable:true
    }
	
	//static belongsTo = [vacaciones:Vacaciones]
}
