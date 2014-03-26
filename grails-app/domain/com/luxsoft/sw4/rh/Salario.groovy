package com.luxsoft.sw4.rh

class Salario {
	
	Empleado empleado
	BigDecimal salarioMensual  // Dato informatico
	BigDecimal salarioDiario
	BigDecimal salarioDiarioIntegrado
	String formaDePago
	String banco
	String clabe
	String baseCotizacion
	String periodicidad

    static constraints = {
		salarioDiario()
		salarioDiarioIntegrado()
		baseCotizacion nullable:true,maxSize:50
		formaDePago inList:['TRANSFERENCIA','CHEQUE','EFECTIVO']
		clabe nullable:true
		periodicidad inList:['SEMANAL','QUINCENAL','MENSUAL']
    }
	
	static transients=['salarioMensual']
	
	BigDecimal getSalarioMensual(){
		return salarioDiario*30
	}
}
