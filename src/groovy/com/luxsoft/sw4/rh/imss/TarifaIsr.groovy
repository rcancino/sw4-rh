package com.luxsoft.sw4.rh.imss

import java.math.RoundingMode

class TarifaIsr {
	
	BigDecimal limiteInferior
	BigDecimal limiteSuperior
	BigDecimal cuotaFija
	BigDecimal porcentaje
	
	static def valores=[
		
		new TarifaIsr(limiteInferior:0.01,limiteSuperior:496.07,cuotaFija:0,porcentaje:1.92),
		new TarifaIsr(limiteInferior:496.08,limiteSuperior:4210.41,cuotaFija:9.52,porcentaje:6.4),
		new TarifaIsr(limiteInferior:4210.42,limiteSuperior:7399.42,cuotaFija:247.24,porcentaje:10.88),
		new TarifaIsr(limiteInferior:7399.43,limiteSuperior:8601.5,cuotaFija:594.21,porcentaje:16),
		new TarifaIsr(limiteInferior:8601.51,limiteSuperior:10298.35,cuotaFija:786.54,porcentaje:17.92),
		new TarifaIsr(limiteInferior:10298.36,limiteSuperior:20770.29,cuotaFija:1090.61,porcentaje:21.36),
		new TarifaIsr(limiteInferior:20770.3,limiteSuperior:32736.83,cuotaFija:3327.42,porcentaje:23.52),
		new TarifaIsr(limiteInferior:32736.84,limiteSuperior:62500,cuotaFija:6141.95,porcentaje:30),
		new TarifaIsr(limiteInferior:62500.01,limiteSuperior:83333.33,cuotaFija:15070.9,porcentaje:32),
		new TarifaIsr(limiteInferior:83333.34,limiteSuperior:250000.00,cuotaFija:21737.57,porcentaje:34),
		new TarifaIsr(limiteInferior:250000.01,limiteSuperior:250000000.00,cuotaFija:78404.23,porcentaje:35)

	]
	
	static diasMap=[:]
	
	static def obtenerTabla(def diasDelPeriodo){
		def diasMes=30.4
		def res=[] //diasMap[diasDelPeriodo]
		
		valores.each{
			def val=new TarifaIsr(
				limiteInferior:it.limiteInferior>0.01? ((it.limiteInferior/diasMes)*diasDelPeriodo).setScale(2,RoundingMode.HALF_EVEN):0.01
				,limiteSuperior:((it.limiteSuperior/diasMes)*diasDelPeriodo).setScale(2,RoundingMode.HALF_EVEN)
				,cuotaFija:((it.cuotaFija/diasMes)*diasDelPeriodo).setScale(2,RoundingMode.HALF_EVEN)
				,porcentaje:it.porcentaje
				)
			res.add(val)
		}
		return res
	}
	
	static TarifaIsr buscar(def valor){
		return valores.find(){ it ->
			(valor>it.limiteInferior && valor<=it.limiteSuperior)
		}
	}
	
	String toString(){
		return "$limiteInferior $limiteSuperior $cuotaFija $porcentaje"
	}

}
