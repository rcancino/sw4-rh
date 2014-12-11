package com.luxsoft.sw4.rh

import grails.plugin.springsecurity.annotation.Secured

@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
class OperacionGenericaController {
    static scaffold = true

    def index(){
		params.max?:100
		params.sort='lastUpdated'
		params.order='desc'
		[operacionGenericaInstanceList:OperacionGenerica.list(params),operacionGenericaInstanceCount:OperacionGenerica.count()]
	}

	def delete(OperacionGenerica operacion){
		if(operacion==null){
			flash.message="No encontro operacion para eliminar"
			redirect action:'index'
			return
		}
		operacion.delete flush:true
		flash.message="Operación eliminada: "+operacion.id
		redirect action:'index'
	}
}
