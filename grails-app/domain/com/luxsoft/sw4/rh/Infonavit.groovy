package com.luxsoft.sw4.rh

import java.math.BigDecimal;

class Infonavit {
	
	Empleado empleado
	
	Date alta=new Date()
	
	BigDecimal cuotaFija

	BigDecimal cuotaDiaria
	
	BigDecimal ultimaDiferencia=0.0
	
	Boolean activo=true
	
	String numeroDeCredito
	
	Integer bimestreActual
	
	String tipo
	
	BigDecimal salarioMinimoGeneral //Salario minimo general
	
	BigDecimal salarioDiarioIntegrado
	
	Integer diasDelBimestre
	
	BigDecimal importeBimestral
	
	BigDecimal seguroDeVivienda
	
	String comentario
	
	Date dateCreated
	
	Date lastUpdated

	static hasMany = [partidas:InfonavitDet]

    static constraints = {
		tipo inList:['CUOTA_FIJA','VSM','PORCENTAJE']
		cuotaFija sacle:6
    }
	
	static mapping = {
		alta type:'date'
		partidas cascade: "all-delete-orphan"
	}
	
}
