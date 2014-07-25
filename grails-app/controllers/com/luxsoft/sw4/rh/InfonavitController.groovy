package com.luxsoft.sw4.rh

import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN'])
class InfonavitController {
    static scaffold = true
	
	def index(Long max){
		params.max = Math.min(max ?: 60, 100)
		def tipo=params.tipo?:'SEMANAL'
		params.sort=params.sort?:'id'
		params.order='desc'
		
		def query=Infonavit.where{empleado.salario.periodicidad==tipo}
		[infonavitInstanceList:query.list(params),infonavitInstanceListTotal:query.count(),tipo:tipo]
	}
}
