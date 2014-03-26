package com.luxsoft.sw4.rh

class DiasFestivos {

	Date dia
	String descripcion

	static belongsTo = [calendario: Calendario]

    static constraints = {
    	descripcion blank:false
    }
}
