package com.luxsoft.sw4

class Dias {
	
	static def NOMBRES=['DOMINGO','LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO']
	
	static toCalendarIndex(Date dia) {
		return dia.getAt(Calendar.DAY_OF_WEEK)
	}
	
	static String toNombre(Date dia) {
		Integer index=toCalendarIndex(dia)
		def res=NOMBRES[index-1]
		//println  " Dia: "+dia.format('EEEE')+"  Res: "+res
	}

}
