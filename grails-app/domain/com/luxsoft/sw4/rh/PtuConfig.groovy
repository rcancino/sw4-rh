package com.luxsoft.sw4.rh

class PtuConfig {

	Integer ejercicio
	BigDecimal monto=0.0

    static constraints = {
    	ejercicio range:(2014..2018)
    }
}
