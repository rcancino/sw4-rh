package com.luxsoft.sw4.rh

import grails.transaction.Transactional


@Transactional(readOnly = true)
class ConceptoDeNominaController {

	static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index() { }

    def percepciones(Integer max){
    	params.max = Math.min(max ?: 15, 100)
    	def conceptosList=ConceptoDeNomina.findAllByTipo('PERCEPCION')
    	[conceptosList:conceptosList]
    }

    

    def deducciones(){}

    def create(){
    	//println 'Alta de concepto'+params
    	def conceptoInstance=new ConceptoDeNomina(params)
    	[conceptoInstance:conceptoInstance]
    }

    
    @Transactional
    def save(ConceptoDeNomina conceptoInstance){
    	if(!conceptoInstance){
    		println 'No se puede generar concepto nulo: '+params
    		return
    	}
    	if(conceptoInstance.hasErrors()){
    		//respond conceptoInstance.errors, view:'create'
			println 'Concepto con errores..	'
			render view:'create',model:[conceptoInstance:conceptoInstance]
            return
    	}
    	conceptoInstance.save flush:true
    	flash.message = message(code: 'default.created.message', args: [message(code: 'conceptoInstance.label', default: 'ConceptoDeNomina'), conceptoInstance.clave])
		
        redirect action:conceptoInstance.tipo=='PERCEPCION'?'percepciones':'deducciones'

    }

    def edit(ConceptoDeNomina conceptoInstance){
    	render view:'create',model:[conceptoInstance:conceptoInstance,tipoDeForma:'edit',action:'update',method:'PUT']
    }

    @Transactional
    def update(ConceptoDeNomina conceptoInstance){
    	if(!conceptoInstance){
    		notFound()
    		return
    	}
    	if(conceptoInstance.hasErrors()){
			render view:'edit',model:[conceptoInstance:conceptoInstance,tipoDeForma:'edit',action:'update',method:'PUT']
            return
    	}
    	conceptoInstance.save flush:true
    	flash.message = message(code: 'default.updated.message', args: [message(code: 'ConceptoDeNomina.label', default: 'ConceptoDeNomina'), conceptoInstance.clave])
		
        redirect action:conceptoInstance.tipo=='PERCEPCION'?'percepciones':'deducciones'

    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message'
                	, args: [message(code: 'ConceptoDeNomina.label', default: 'ConceptoDeNomina'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
