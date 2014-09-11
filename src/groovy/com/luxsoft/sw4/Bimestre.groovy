package com.luxsoft.sw4

class Bimestre {
	
	static Integer getCurrentBimestre(){
		return getBimestre(new Date())
	}
	
	static Integer getBimestre(Date dia){
		int mes=Periodo.obtenerMes(dia)
		switch(mes){
			case 0:
			case 1:
				return 1
			case 2:
			case 3:
				return 2
			case 4:
			case 5:
				return 3
			case 6:
			case 7:
				return 4
			case 8:
			case 9:
				return 5
			case 10:
			case 11:
				return 6
		}
		
	}
	
	
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
