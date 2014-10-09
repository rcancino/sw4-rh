package com.luxsoft.sw4.rh

import com.luxsoft.sw4.rh.sat.SatBanco;

class Salario implements Serializable{
	
	Empleado empleado
	BigDecimal salarioMensual  // Dato informatico
	BigDecimal salarioDiario
	BigDecimal salarioDiarioIntegrado
	BigDecimal salarioVariable=0.0
	String formaDePago
	
	String clabe
	String baseCotizacion
	String periodicidad
	String numeroDeCuenta
	SatBanco banco

    static constraints = {
		salarioDiario()
		salarioDiarioIntegrado()
		baseCotizacion nullable:true,maxSize:50
		formaDePago inList:['TRANSFERENCIA','CHEQUE','EFECTIVO']
		clabe nullable:true
		periodicidad inList:['SEMANAL','QUINCENAL','MENSUAL','CATORCENAL','BIMESTRAL','UNIDAD DE OBRA','COMISION','PRECIO ALZADO']
		banco nullable:true
		numeroDeCuenta nullable:true
    }
	
	static transients=['salarioMensual']
	
	BigDecimal getSalarioMensual(){
		return salarioDiario*30
	}
}
