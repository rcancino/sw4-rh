package com.luxsoft.sw4.rh

import java.util.Date;

class PensionAlimenticia {

    Empleado empleado
	
	BigDecimal porcentaje
	
	Boolean neto=true
	
	String beneficiario
	
	String comentario
	
	Date dateCreated
	
	Date lastUpdated    
	
	static constraints = {
		comentario nullable:true
	}
	
}
