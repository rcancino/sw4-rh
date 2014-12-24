package com.luxsoft.sw4.rh.imss

import java.math.RoundingMode

class TarifaAnualIsr {
	
	BigDecimal limiteInferior
	BigDecimal limiteSuperior
	BigDecimal cuotaFija
	BigDecimal porcentaje
	
	static def valores=[
		
			new TarifaAnualIsr(limiteInferior:0.01,limiteSuperior:5952.84,cuotaFija:0,porcentaje:1.92),
			new TarifaAnualIsr(limiteInferior:5952.85,limiteSuperior:50524.92,cuotaFija:114.29,porcentaje:6.4),
			new TarifaAnualIsr(limiteInferior:50524.93,limiteSuperior:88793.04,cuotaFija:2966.91,porcentaje:10.88),
			new TarifaAnualIsr(limiteInferior:88793.05,limiteSuperior:103218,cuotaFija:7130.48,porcentaje:16),
			new TarifaAnualIsr(limiteInferior:103218.01,limiteSuperior:123580.2,cuotaFija:9438.47,porcentaje:17.92),
			new TarifaAnualIsr(limiteInferior:123580.21,limiteSuperior:249243.48,cuotaFija:13087.37,porcentaje:21.36),
			new TarifaAnualIsr(limiteInferior:249243.49,limiteSuperior:392841.96,cuotaFija:39029.05,porcentaje:23.52),
			new TarifaAnualIsr(limiteInferior:392841.97,limiteSuperior:750000,cuotaFija:73703.41,porcentaje:30),
			new TarifaAnualIsr(limiteInferior:750000.01,limiteSuperior:1000000,cuotaFija:180850.82,porcentaje:32),
			new TarifaAnualIsr(limiteInferior:1000000.01,limiteSuperior:3000000,cuotaFija:260850.81,porcentaje:34),
			new TarifaAnualIsr(limiteInferior:3000000.01,limiteSuperior:9999999.99,cuotaFija:940850.81,porcentaje:35)


	]
	
	static diasMap=[:]
	
	
	
	static TarifaAnualIsr buscar(def valor){
		return valores.find(){ it ->
			(valor>it.limiteInferior && valor<=it.limiteSuperior)
		}
	}
	
	String toString(){
		return "$limiteInferior $limiteSuperior $cuotaFija $porcentaje"
	}

}
