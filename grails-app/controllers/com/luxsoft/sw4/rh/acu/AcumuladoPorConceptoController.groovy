package com.luxsoft.sw4.rh.acu

import grails.plugin.springsecurity.annotation.Secured

@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
class AcumuladoPorConceptoController {

    def index(Long max) { 
		params.max=Math.min(max?:15, 100)
		params.sort=params.sort?:'concepto.clave'
		params.order='asc'
		[acumuladoPorConceptoInstanceList:AcumuladoPorConcepto.list(params),acumuladoPorConceptoCount:AcumuladoPorConcepto.count()]
	}
}
