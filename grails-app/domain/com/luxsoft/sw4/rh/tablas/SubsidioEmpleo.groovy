package com.luxsoft.sw4.rh.tablas



class SubsidioEmpleo {
	
	BigDecimal desde
	BigDecimal hasta
	BigDecimal subsidio
	
	Date lastUpdated
	Date dateCreated

    static constraints = {
    }
	
	
	static void cargaInicial(){
		SubsidioEmpleo.findOrSaveWhere(desde:0.01,hasta:1768.96,subsidio:407.02)
		SubsidioEmpleo.findOrSaveWhere(desde:1768.97,hasta:2653.38,subsidio:406.83)
		SubsidioEmpleo.findOrSaveWhere(desde:2653.39,hasta:3472.84,subsidio:406.62)
		SubsidioEmpleo.findOrSaveWhere(desde:3472.85,hasta:3537.87,subsidio:392.77)
		SubsidioEmpleo.findOrSaveWhere(desde:3537.88,hasta:4446.15,subsidio:382.46)
		SubsidioEmpleo.findOrSaveWhere(desde:4446.16,hasta:4717.18,subsidio:354.23)
		SubsidioEmpleo.findOrSaveWhere(desde:4717.19,hasta:5335.42,subsidio:324.87)
		SubsidioEmpleo.findOrSaveWhere(desde:5335.43,hasta:6224.67,subsidio:294.63)
		SubsidioEmpleo.findOrSaveWhere(desde:6224.68,hasta:7113.9,subsidio:253.54)
		SubsidioEmpleo.findOrSaveWhere(desde:7113.91,hasta:7382.33,subsidio:217.61)
		SubsidioEmpleo.findOrSaveWhere(desde:7382.34,hasta:250000000.00,subsidio:0.0)
		
	}
}
