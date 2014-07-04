package com.luxsoft.sw4.rh.acu

import com.luxsoft.sw4.rh.ConceptoDeNomina;
import com.luxsoft.sw4.rh.Empleado

class AcumuladoPorConcepto {
	
	Integer ejercicio
	
	ConceptoDeNomina concepto
	
	Empleado empleado
	
	BigDecimal acumuladoExcento
	
	BigDecimal acumuladoGravado
	
	Date dateCreated
	
	Date lastUpdated

    static constraints = {
    }
}
