package com.luxsoft.sw4.rh.acu

import com.luxsoft.sw4.rh.*

class IsptMensual {
	
	Empleado empleado
	Integer ejercicio
	String mes
	NominaPorEmpleado nominaPorEmpleado
	
	BigDecimal baseGravable
	
	BigDecimal limiteInferior
	BigDecimal limiteSuperior
	BigDecimal cuotaFija
	BigDecimal tarifa
	
	
	
	BigDecimal impuestoMensual
	BigDecimal subsidioMensual
	
	BigDecimal impuestoFinal
	BigDecimal subsidioFinal
	
	BigDecimal impuestoAcumulado
	BigDecimal subsidioAcumulado
	
	BigDecimal impuestoAcumuladoFinal
	BigDecimal subsidioAcumuladoFinal
	
	BigDecimal resultadoImpuesto
	BigDecimal resultadoSubsidio
	
	
	Date dateCreated
	Date lastUpdated
	
	

    static constraints = {
    }
}
