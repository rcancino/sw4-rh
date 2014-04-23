package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Periodo;

class CalendarioDeNominas {
	
	String tipoDeNomina
	Periodo periodo
	Date corte
	Date pago
	Integer folio
	String comentario
	String status
	
	static embedded = ['periodo']

    static constraints = {
		tipoDeNomina inList:['GENERAL','ESPECIAL','AGINALDO','UTILIDADES']
		status inList:['','GENERADA']
    }
}
