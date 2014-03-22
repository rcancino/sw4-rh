package com.luxsoft.sw4.rh

class SeguridadSocial {
	
	String tipoDeEmpleado
	String numero
	String tipoDeContrato
	Date alta
	String turno
	String unidadMedica
	

    static constraints = {
		tipoDeEmpleado inList:['SINDICALIZADO','CONFIANZA']
		tipoDeContrato inList:['EVENTUAL','MEDIO TIEMPO','TIEMPO COMPLETO']
		turno inList:['MATUTINO','NOCTURNO','MIXTO']
    }
}
