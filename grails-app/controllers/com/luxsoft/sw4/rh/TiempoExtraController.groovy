package com.luxsoft.sw4.rh


import grails.validation.Validateable;

import org.grails.databinding.BindingFormat;

import com.luxsoft.sw4.Periodo



class TiempoExtraController {
    static scaffold = true
	
	def create() {
		
		//respond new TiempoExtraCmd(params)
		[tiempoExtraInstance:new TiempoExtra()]
	}
	
}

@Validateable
class TiempoExtraCmd{
	
	Empleado empleado
	
	ConceptoDeNomina concepto
	
	@BindingFormat('dd/MM/yyyy')
	Date fechaInicial
	@BindingFormat('dd/MM/yyyy')
	
	Date fechaFinal
	
	String autorizo
	
	static constraints ={
		importFrom TiempoExtra
	}
}
