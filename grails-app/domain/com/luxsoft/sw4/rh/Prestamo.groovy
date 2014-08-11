package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Empresa

class Prestamo {
	
	
	Empleado empleado
	Date alta
	String autorizo
	Date fechaDeAutorizacion
	String comentario
	String tipo
	BigDecimal tasaDescuento=0.3

	BigDecimal importe=0
	BigDecimal saldo=0
	BigDecimal totalAbonos
	
	List abonos


	Date dateCreated
	Date lastUpdated
	

    static constraints = {
    	comentario nullable:true
    	tipo inList:['DESCUENTO_POR_NOMINA']
    }

    static hasMany = [abonos: PrestamoAbono]

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
		"Prestamo:${id} ${empleado} ${saldo}"
	}
}
