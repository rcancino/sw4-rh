package com.luxsoft.sw4.rh

import org.grails.databinding.BindingFormat

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
	
	@BindingFormat('dd/MM/yyyy')
	Date suspension
	
	@BindingFormat('dd/MM/yyyy')
	Date reinicio
	
	@BindingFormat('dd/MM/yyyy')
	Date modificacionTipo
	
	@BindingFormat('dd/MM/yyyy')
	Date modificacionValor
	
	@BindingFormat('dd/MM/yyyy')
	Date modificacionNumero





	static hasMany = [partidas:InfonavitDet]

    static constraints = {
		tipo inList:['CUOTA_FIJA','VSM','PORCENTAJE']
		cuotaFija sacle:6
		suspension nullable:true
		reinicio nullable:true
		modificacionTipo nullable:true
		modificacionValor nullable:true
		modificacionNumero nullable:true
    }
	
	static mapping = {
		alta type:'date'
		partidas cascade: "all-delete-orphan"
		suspension type:'date'
		reinicio type:'date'
		modificacionTipo type:'date'
		modificacionValor type:'date'
		modificacionNumero type:'date'
	}
	
}

