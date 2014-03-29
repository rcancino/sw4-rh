package com.luxsoft.sw4.cfdi

import com.luxsoft.sw4.Empresa;

class Folio {

	Empresa empresa
	String serie
	Long folio=0

    static constraints = {
    	empresa()
		serie blank:false,maxSize:30
		folio nullable:false
    }

    Long next(){
		folio++
		return folio
	}
	
	String toString(){
		return "${empresa.clave} - $serie - $folio"
	}
}
