package com.luxsoft.sw4.rh.tablas

class FactorDeIntegracion {
	
	Integer tipo
	Integer yearDe
	Integer yearHasta
	Integer diasDe
	Integer diasHasta
	Integer vacDias
	BigDecimal vacPrima
	Integer semDias
	BigDecimal semFactor
	Integer cobDias
	BigDecimal cobFactor
	Integer qnaDias
	BigDecimal  qnaFactor
	
	Date dateCreated
	Date lastUpdated

    static constraints = {
		semFactor scale: 4, maxSize:16
		cobFactor scale: 4, maxSize:16
		qnaFactor scale: 4, maxSize:16
    }
	
	/*
	static mapping = {
		semFactor type: 'double' 
		cobFactor type: 'double'
		qnaFactor type: 'double'
	}
	*/
}
