package com.luxsoft.sw4.rh


class OtraDeduccion {
	
	Empleado empleado
	String tipo
	String comentario
	
	BigDecimal importeGravado=0.00
	BigDecimal importeExcento=0.00
	
	BigDecimal importe=0
	BigDecimal saldo=0
	BigDecimal totalAbonos
	
	List abonos


	Date dateCreated
	Date lastUpdated

    static constraints = {
		comentario nullable:true
		tipo inList:['OTROS']
    }
	
	static hasMany = [abonos:OtraDeduccionAbono]
	
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
		"Otra deduccion:${id} ${empleado}  Importe: $importe  Saldo:$saldo"
	}
}
