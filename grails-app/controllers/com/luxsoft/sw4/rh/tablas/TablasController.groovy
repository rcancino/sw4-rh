package com.luxsoft.sw4.rh.tablas

import org.springframework.security.access.annotation.Secured;

@Secured(['ROLE_ADMIN','RH_MANAGER'])
class TablasController {

    def index() { }
	
	def tarifaIsr(){
		
		[tabla:'tarifaIsr',tarifaIsrInstanceList:TarifaIsr.list()]
	}
	
	def subsidio(){
		[tabla:'subsidio',subsidioEmpleoInstanceList:SubsidioEmpleo.list()]
	}
	
	def factorDeIntegracion(){
		[tabla:'factorDeIntegracion',factorDeIntegracionInstanceList:FactorDeIntegracion.list()]
	}
}
