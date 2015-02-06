package com.luxsoft.sw4.rh.tablas

import java.math.RoundingMode

class SubsidioEmpleo {
	
	BigDecimal desde
	BigDecimal hasta
	BigDecimal subsidio
	Integer ejercicio
	
	
	Date lastUpdated
	Date dateCreated

    static constraints = {
    	ejercicio inList:[2015,2016,2017,2018]
    }
	
	
	static void cargaInicial(def ejercicio){
		SubsidioEmpleo.findOrSaveWhere(ejercicio:ejercicio,desde:0.01,hasta:1768.96,subsidio:407.02)
		SubsidioEmpleo.findOrSaveWhere(ejercicio:ejercicio,desde:1768.97,hasta:2653.38,subsidio:406.83)
		SubsidioEmpleo.findOrSaveWhere(ejercicio:ejercicio,desde:2653.39,hasta:3472.84,subsidio:406.62)
		SubsidioEmpleo.findOrSaveWhere(ejercicio:ejercicio,desde:3472.85,hasta:3537.87,subsidio:392.77)
		SubsidioEmpleo.findOrSaveWhere(ejercicio:ejercicio,desde:3537.88,hasta:4446.15,subsidio:382.46)
		SubsidioEmpleo.findOrSaveWhere(ejercicio:ejercicio,desde:4446.16,hasta:4717.18,subsidio:354.23)
		SubsidioEmpleo.findOrSaveWhere(ejercicio:ejercicio,desde:4717.19,hasta:5335.42,subsidio:324.87)
		SubsidioEmpleo.findOrSaveWhere(ejercicio:ejercicio,desde:5335.43,hasta:6224.67,subsidio:294.63)
		SubsidioEmpleo.findOrSaveWhere(ejercicio:ejercicio,desde:6224.68,hasta:7113.9,subsidio:253.54)
		SubsidioEmpleo.findOrSaveWhere(ejercicio:ejercicio,desde:7113.91,hasta:7382.33,subsidio:217.61)
		SubsidioEmpleo.findOrSaveWhere(ejercicio:ejercicio,desde:7382.34,hasta:250000000.00,subsidio:0.0)
		
	}


	static def obtenerTabla(def diasDelPeriodo,def ejercicio,def diasMes){
		//def diasMes=30.4
		def res=[] 
		def valores=SubsidioEmpleo.findAllByEjercicio(ejercicio)
		valores.each{
			def val=new SubsidioEmpleo(
				desde:it.desde>0.01? ((it.desde/diasMes)*diasDelPeriodo).setScale(2,RoundingMode.HALF_EVEN):0.01
				,hasta:((it.hasta/diasMes)*diasDelPeriodo).setScale(2,RoundingMode.HALF_EVEN)
				,subsidio:((it.subsidio/diasMes)*diasDelPeriodo).setScale(2,RoundingMode.HALF_EVEN)
				)
			res.add(val)
		}
		res.each{
			println it
		}
		return res
	}
	
	static SubsidioEmpleo buscar(def ejercicio,def valor){
		def valores=SubsidioEmpleo.findAllByEjercicio(ejercicio)
		return valores.find(){ it ->
			(valor>it.desde && valor<=it.hasta)
		}
	}
	
	String toString(){
		return "$desde - $hasta - Sub:$subsidio"
	}
}
