package com.luxsoft.sw4

class Bimestre {
	
	
	static Periodo getBimestre(Integer ejercicio,numero){
		
		switch (numero){
			case 1:
				def mes1=Periodo.getPeriodoEnUnMes(0,ejercicio)
				def mes2=Periodo.getPeriodoEnUnMes(1,ejercicio)
				return new Periodo(mes1.fechaInicial,mes2.fechaFinal)
			case 2:
				def mes1=Periodo.getPeriodoEnUnMes(2,ejercicio)
				def mes2=Periodo.getPeriodoEnUnMes(3,ejercicio)
				return new Periodo(mes1.fechaInicial,mes2.fechaFinal)
			case 3:
				def mes1=Periodo.getPeriodoEnUnMes(4,ejercicio)
				def mes2=Periodo.getPeriodoEnUnMes(5,ejercicio)
				return new Periodo(mes1.fechaInicial,mes2.fechaFinal)
			case 4:
				def mes1=Periodo.getPeriodoEnUnMes(6,ejercicio)
				def mes2=Periodo.getPeriodoEnUnMes(7,ejercicio)
				return new Periodo(mes1.fechaInicial,mes2.fechaFinal)
			case 5:
				def mes1=Periodo.getPeriodoEnUnMes(8,ejercicio)
				def mes2=Periodo.getPeriodoEnUnMes(9,ejercicio)
				return new Periodo(mes1.fechaInicial,mes2.fechaFinal)
			
			case 6:
				def mes1=Periodo.getPeriodoEnUnMes(10,ejercicio)
				def mes2=Periodo.getPeriodoEnUnMes(11,ejercicio)
				return new Periodo(mes1.fechaInicial,mes2.fechaFinal)
			
		}
	}
	

}
