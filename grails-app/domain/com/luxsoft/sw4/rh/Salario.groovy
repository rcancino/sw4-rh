package com.luxsoft.sw4.rh

class Salario {
	
	Empleado empleado
	BigDecimal salarioDiario
	BigDecimal salarioDiarioIntegrado
	BigDecimal ultimoSalarioIntegrado
	String formaDePago
	String banco
	String cuentaCLABE
	String baseCotizacion

    static constraints = {
		salarioDiario()
		baseCotizacion maxSize:50
		
    }
}
