package com.luxsoft.sw4.rh

class Fonacot {

    Empleado empleado

    String numeroDeFonacot

    String numeroDeCredito

    BigDecimal importe=0

    BigDecimal retencionMensual

    BigDecimal retencionDiaria

	BigDecimal saldo=0

	BigDecimal totalAbonos

    Boolean activo=true
	
	List abonos

	Date dateCreated
	Date lastUpdated
	

    static constraints = {
    }

    static hasMany = [abonos: FonacotAbono]

    static transients=['totalAbonos']
	
	static mapping = {
		abonos cascade: "all-delete-orphan"
	}

    BigDecimal getTotalAbonos(){
    	def totalAbono=abonos?.sum 0.0,{
    		it.importe
    	}
    	return totalAbono;
    }

    def actualizarSaldo(){
    	importe=importe?:0.0
    	def abonos=getTotalAbonos()?:0.0
    	saldo=importe-abonos
    }

    def afterInsert() {
    	actualizarSaldo()
    }

    def afterUpdate() {
    	actualizarSaldo()
    }
	String toString() {
		"Prestamo FONACOT:${id} ${empleado} ${saldo}"
	}
}
