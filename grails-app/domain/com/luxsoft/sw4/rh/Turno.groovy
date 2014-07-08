package com.luxsoft.sw4.rh

import org.joda.time.LocalTime
import org.jadira.usertype.dateandtime.joda.*

class Turno {

	String descripcion
	Map dias
	LocalTime inicioDeDia
	
	LocalTime horaLimiteDeTrabajo
	boolean horaLimiteSiguienteDia
	LocalTime inicioDeTiempoExtra
	

	Date dateCreated
	Date lastUpdated

	Map diasDeDescanso

	static hasMany = [dias: TurnoDet]

    static constraints = {
    	descripcion blank:false
    }

    static mapping = {
    	dias cascade: "all-delete-orphan"
    	//inicioDeDia type: PersistentLocalTime
    }
}
