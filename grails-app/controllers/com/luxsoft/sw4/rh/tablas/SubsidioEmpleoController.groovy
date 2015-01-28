package com.luxsoft.sw4.rh.tablas

import grails.plugin.springsecurity.annotation.Secured


@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
class SubsidioEmpleoController {
    static scaffold = true

    def index(){
    	params.max = 20
		def list=SubsidioEmpleo.findAll("from SubsidioEmpleo t order by t.ejercicio desc,t.desde asc",[],params)
    	[subsidioEmpleoInstanceList:list,subsidioEmpleoInstanceCount:SubsidioEmpleo.count()]
    }

    def delete(SubsidioEmpleo subsidio){
    	log.info 'Eliminando subsidio: '+subsidio
    	subsidio.delete flush:true
    	flash.message="Subsidio eliminado "+subsidio.id
    	redirect action:'index'
    }
}
