package com.luxsoft.sw4.rh.imss

import java.math.RoundingMode

class Subsidio {
	
	BigDecimal desde
	BigDecimal hasta
	BigDecimal subsidio
	
	static def valores=[
		new Subsidio(desde:0.01,hasta:1768.96,subsidio:407.02),
		new Subsidio(desde:1768.97,hasta:2653.38,subsidio:406.83),
		new Subsidio(desde:2653.39,hasta:3472.84,subsidio:406.62),
		new Subsidio(desde:3472.85,hasta:3537.87,subsidio:392.77),
		new Subsidio(desde:3537.88,hasta:4446.15,subsidio:382.46),
		new Subsidio(desde:4446.16,hasta:4717.18,subsidio:354.23),
		new Subsidio(desde:4717.19,hasta:5335.42,subsidio:324.87),
		new Subsidio(desde:5335.43,hasta:6224.67,subsidio:294.63),
		new Subsidio(desde:6224.68,hasta:7113.9,subsidio:253.54),
		new Subsidio(desde:7113.91,hasta:7382.33,subsidio:217.61),
		new Subsidio(desde:7382.34,hasta:250000000,subsidio:0)
		]
	
	static def obtenerTabla(def diasDelPeriodo){
		def diasMes=30.4
		def res=[] //diasMap[diasDelPeriodo]
		valores.each{
			def val=new Subsidio(
				desde:it.desde>0.01? ((it.desde/diasMes)*diasDelPeriodo).setScale(2,RoundingMode.HALF_EVEN):0.01
				,hasta:((it.hasta/diasMes)*diasDelPeriodo).setScale(2,RoundingMode.HALF_EVEN)
				,subsidio:((it.subsidio/diasMes)*diasDelPeriodo).setScale(2,RoundingMode.HALF_EVEN)
				)
			res.add(val)
		}
		return res
	}

}
