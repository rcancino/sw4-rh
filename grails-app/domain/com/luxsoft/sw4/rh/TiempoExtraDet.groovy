package com.luxsoft.sw4.rh

class TiempoExtraDet {

	Date fecha
	Integer horas
	BigDecimal importe
	String comentario

    static constraints = {
    	comentario nullable:true
    }

    static belongsTo = [parent: TiempoExtra]
}
