package com.luxsoft.sw4.rh

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
class ModificacionSalarialController {
    
    def index(Long max){
    	params.max = Math.min(max ?: 15, 100)
		params.sort=params.sort?:'id'
		params.order='desc'
		[modificacionInstanceList:ModificacionSalarial.list(params)
		,modificacionInstanceListTotal:ModificacionSalarial.count()]
    }

    def create(){
    	[modificacionInstance:new ModificacionSalarial(tipo:'AUMENTO')]
    }

    @Transactional
    def save(ModificacionSalarial modificacionInstance){
    	//modificacionInstance.sdiNuevo=0.0
    	modificacionInstance.validate()
    	if(modificacionInstance.hasErrors()){
    		flash.message="Errores de validacion en modificacion salarial"
    		render view:'create',model:[modificacionInstance:modificacionInstance]
    	}
		
    	modificacionInstance.save failOnError:true
    	redirect action:'show',params:[id:modificacionInstance.id]

    }
    
    @Transactional
    def delete(Long id){
        def modificacion=ModificacionSalarial.get(id)
        modificacion.delete(flush:true)
        flash.message="Modificacion salarial ${id} eliminada"
        redirect action:'index'
    }

    def show(Long id){
    	def modificacionInstance=ModificacionSalarial.get(id)
    	[modificacionInstance:modificacionInstance]
    }

}
