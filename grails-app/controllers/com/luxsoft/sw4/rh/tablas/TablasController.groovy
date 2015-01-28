package com.luxsoft.sw4.rh.tablas


import org.springframework.security.access.annotation.Secured;


@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
class TablasController {

    def index() { }
	
	def factorDeIntegracion(){
		[tabla:'factorDeIntegracion',factorDeIntegracionInstanceList:FactorDeIntegracion.list()]
	}
	
	def tarifaIsr(){
		
		[tabla:'tarifaIsr',tarifaIsrInstanceList:TarifaIsr.list()]
	}
	
	def subsidio(){
		[tabla:'subsidio',subsidioEmpleoInstanceList:SubsidioEmpleo.list()]
	}
	
	
}
