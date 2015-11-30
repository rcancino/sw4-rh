  package com.luxsoft.sw4.rh

import org.grails.databinding.BindingFormat

class CalculoSdi {
	
	/*
	Long empleadoId
	String clave
	String nombres
	String apellidoPaterno
	String apellidoMaterno
	Date alta
	String status
	Date fechaBaja
	String periodicidad
	*/
	
	Empleado empleado
	
	@BindingFormat('dd/MM/yyyy')
	Date fechaIni
	
	@BindingFormat('dd/MM/yyyy')
	Date fechaFin
	
	BigDecimal sdb=0.0
	
	BigDecimal sdiAnterior=0.0
	
	BigDecimal smg=0.0
	
	BigDecimal years=0.0
	
	BigDecimal topeSmg=0.0
	
	BigDecimal dias=0.0
	
	Integer vacDias=0
	
	BigDecimal vacPrima=0.0
	
	Integer agndoDias=0
	
	BigDecimal factor=0.0
	
	BigDecimal sdiF=0.0
	
	BigDecimal diasBim=0.0
	
	BigDecimal faltas=0.0
	
	BigDecimal incapacidades=0.0
	
	BigDecimal diasLabBim=0.0
	
	BigDecimal compensacion=0.0
	BigDecimal incentivo=0.0
	BigDecimal bonoPorDesemp=0.0
	BigDecimal hrsExtrasDobles=0.0
	BigDecimal hrsExtrasTriples=0.0
	BigDecimal comisiones=0.0
	BigDecimal primaDom=0.0
	BigDecimal vacacionesP=0.0
	
	BigDecimal variable=0.0
	BigDecimal varDia=0.0
	BigDecimal sdiCalc=0.0
	BigDecimal sdiNvo=0.0
	BigDecimal sdiInf=0.0
	BigDecimal sdbAnterior=0.0
	
	String tipo
	
	Integer ejercicio
	
	Integer bimestre
	
	String status='REVISION'

	//Date fecha

    static constraints = {
		//empleado unique:['ejercicio','bimestre','tipo']
		tipo inList:['CALCULO_SDI','AUMENTO','ALTA']
		status inList:['REVISION','APLICADO']
		factor sacle:4
		//fecha nullable:true
    }
	
	static mapping = {
		fechaIni type:'date'
		fechaFin type:'date'
	}
}
