package com.luxsoft.sw4.rh

class Infonavit {
	
	Empleado empleado
	
	Date alta=new Date()
	
	BigDecimal cuotaFija

	BigDecimal cuotaDiaria
	
	Boolean activo=true
	
	String numeroDeCredito
	
	String tipo
	
	String comentario
	
	Date dateCreated
	
	Date lastUpdated

	static hasMany = [partidas:InfonavitDet]

    static constraints = {
		tipo inList:['CUOTA_FIJA','VSM','PORCENTAJE']
    }
	
	static mapping = {
		alta type:'date'
		partidas cascade: "all-delete-orphan"
	}
	
}
