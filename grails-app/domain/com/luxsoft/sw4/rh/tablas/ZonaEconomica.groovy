package com.luxsoft.sw4.rh.tablas



class ZonaEconomica {
	
	String clave
	BigDecimal salario=0
	
	String toString(){
		return "$clave: $salario"
	}

    static constraints = {
    }
	
}
