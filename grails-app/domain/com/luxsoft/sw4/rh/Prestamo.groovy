package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Empresa

class Prestamo {
	
	Empresa empresa
	Empleado empleado
	Date alta
	String autorizo
	Date fechaDeAutorizacion
	String comentario
	String tipo
	BigDecimal tasaDescuento=0.3

	BigDecimal importe=0
	
	BigDecimal saldo=0
	List abonosPorNomina
	List abonosEspeciales



	Date dateCreated
	Date lastUpdated
	

    static constraints = {
    	comentario nullable:true
    	tipo inList:['DESCUENTO_POR_NOMINA']
    }

    static hasMany = [abonosPorNomina: PrestamoAbono,abonosEspeciales:PrestamoAbonoEspecial]

    static transients = ['abonos']

    BigDecimal getAbonos(){
    	def porNomina=abonosPorNomina?.sum 0.0,{
    		it.importe
    	}
    	def especiales=abonosEspeciales?.sum 0.0,{
    		it.importe
    	}
    }

    def actualizarSaldo(){
    	importe=importe?:0
    	def abonos=getAbonos()?:0
    	saldo=importe-abonos
    }

    def beforeInsert() {
    	actualizarSaldo()
    }

    def beforeUpdate() {
    	actualizarSaldo()
    }
}
