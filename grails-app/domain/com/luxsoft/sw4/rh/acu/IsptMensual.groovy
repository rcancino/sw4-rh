package com.luxsoft.sw4.rh.acu

import com.luxsoft.sw4.rh.*

class IsptMensual {
	
	Empleado empleado
	Integer ejercicio
	String mes
	NominaPorEmpleado nominaPorEmpleado
	
	BigDecimal baseGravable
	BigDecimal permisoRetardoAcu
	
	BigDecimal limiteInferior
	BigDecimal limiteSuperior
	BigDecimal cuotaFija
	BigDecimal tarifa
	
	
	
	BigDecimal impuestoMensual
	BigDecimal subsidioMensual //Segun tabla de tarifa
	
	
	
	BigDecimal impuestoFinal
	BigDecimal subsidioFinal
	
	BigDecimal impuestoAcumulado
	BigDecimal subsidioAcumulado
	BigDecimal subsidioAplicado
	
	BigDecimal impuestoAcumuladoFinal
	BigDecimal subsidioAcumuladoFinal
	
	BigDecimal resultadoImpuesto
	BigDecimal resultadoSubsidio
	BigDecimal resultadoSubsidioAplicado
	
	
	Date dateCreated
	Date lastUpdated
	
	

    static constraints = {
		
    }
}
