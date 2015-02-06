package com.luxsoft.sw4.rh.tablas

import java.math.RoundingMode

class TarifaIsr {
	
	Integer ejercicio
	String tipo="MENSUAL"
	BigDecimal limiteInferior
	BigDecimal limiteSuperior
	BigDecimal cuotaFija
	BigDecimal porcentaje
	

    static constraints = {
    	ejercicio inList:[2015,2016,2017,2018]
    	tipo inList:['MENSUAL','ANUAL']
    }
	
	
	static void cargaInicial(def ejercicio,def tipo){
		TarifaIsr.findOrSaveWhere(ejercicio:ejercicio,tipo:tipo,limiteInferior:0.01,limiteSuperior:496.07,cuotaFija:0.0,porcentaje:1.92)
		TarifaIsr.findOrSaveWhere(ejercicio:ejercicio,tipo:tipo,limiteInferior:496.08,limiteSuperior:4210.41,cuotaFija:9.52,porcentaje:6.4)
		TarifaIsr.findOrSaveWhere(ejercicio:ejercicio,tipo:tipo,limiteInferior:4210.42,limiteSuperior:7399.42,cuotaFija:247.24,porcentaje:10.88)
		TarifaIsr.findOrSaveWhere(ejercicio:ejercicio,tipo:tipo,limiteInferior:7399.43,limiteSuperior:8601.5,cuotaFija:594.21,porcentaje:16.00)
		TarifaIsr.findOrSaveWhere(ejercicio:ejercicio,tipo:tipo,limiteInferior:8601.51,limiteSuperior:10298.35,cuotaFija:786.54,porcentaje:17.92)
		TarifaIsr.findOrSaveWhere(ejercicio:ejercicio,tipo:tipo,limiteInferior:10298.36,limiteSuperior:20770.29,cuotaFija:1090.61,porcentaje:21.36)
		TarifaIsr.findOrSaveWhere(ejercicio:ejercicio,tipo:tipo,limiteInferior:20770.3,limiteSuperior:32736.83,cuotaFija:3327.42,porcentaje:23.52)
		TarifaIsr.findOrSaveWhere(ejercicio:ejercicio,tipo:tipo,limiteInferior:32736.84,limiteSuperior:62500.00,cuotaFija:6141.95,porcentaje:30.00)
		TarifaIsr.findOrSaveWhere(ejercicio:ejercicio,tipo:tipo,limiteInferior:62500.01,limiteSuperior:83333.33,cuotaFija:15070.9,porcentaje:32.00)
		TarifaIsr.findOrSaveWhere(ejercicio:ejercicio,tipo:tipo,limiteInferior:83333.34,limiteSuperior:250000.00,cuotaFija:21737.57,porcentaje:34.00)
		TarifaIsr.findOrSaveWhere(ejercicio:ejercicio,tipo:tipo,limiteInferior:250000.01,limiteSuperior:250000000.00,cuotaFija:78404.23,porcentaje:35.00)
	}
	

	

	static def obtenerTabla(def ejercicio,def tipo,def diasDelPeriodo,def diasMes){
		//def diasMes=30.4
		def res=[] //diasMap[diasDelPeriodo]

		def valores=TarifaIsr.findAllByEjercicioAndTipo(ejercicio,tipo)
		
		valores.each{
			def val=new TarifaIsr(
				limiteInferior:it.limiteInferior>0.01? ((it.limiteInferior/diasMes)*diasDelPeriodo).setScale(2,RoundingMode.HALF_EVEN):0.01
				,limiteSuperior:((it.limiteSuperior/diasMes)*diasDelPeriodo).setScale(2,RoundingMode.HALF_EVEN)
				,cuotaFija:((it.cuotaFija/diasMes)*diasDelPeriodo).setScale(2,RoundingMode.HALF_EVEN)
				,porcentaje:it.porcentaje
				)
			res.add(val)
		}
		res.each{
			println it
		}
		return res
	}
	
	static TarifaIsr buscar(def ejercicio,def tipo,def valor){
		def valores=TarifaIsr.findAllByEjercicioAndTipo(ejercicio,tipo)
		return valores.find(){ it ->
			(valor>it.limiteInferior && valor<=it.limiteSuperior)
		}
	}
	
	String toString(){
		return "($ejercicio - $tipo) $limiteInferior $limiteSuperior $cuotaFija $porcentaje"
	}
}
