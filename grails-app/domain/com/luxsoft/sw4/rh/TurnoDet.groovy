package com.luxsoft.sw4.rh


import org.joda.time.LocalTime
import org.jadira.usertype.dateandtime.joda.*

class TurnoDet {

	String dia

	LocalTime entrada1
	LocalTime salida1
	
	LocalTime entrada2
	LocalTime salida2

	static belongsTo = [turno: Turno]

    static constraints = {
    	//dia inList:['LUNES','MARTES','MIERCOLES','JUEVES','SABADO','DOMINGO','LUNES']
        entrada1 nullable:true
        salida1 nullable:true
        entrada2 nullable:true
        salida2 nullable:true
    }

    static mapping = {
    	entrada1 type: PersistentLocalTime
    	salida1 type: PersistentLocalTime
    	entrada2 type: PersistentLocalTime
    	salida2 type: PersistentLocalTime
    }
}
