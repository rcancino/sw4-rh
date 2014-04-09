package com.luxsoft.sw4.rh

import grails.transaction.Transactional;

class NominaPorEmpleadoController {

    def index() { }

    def create(Long id){
    	def nomina=Nomina.get(id)
    	def ne=new NominaPorEmpleado(params)
    	[nominaInstance:nomina,nominaPorEmpleadoInstance:ne]
    }

    def edit(Long id){
    	def ne=NominaPorEmpleado.get(id)
    	[nominaPorEmpleadoInstance:ne]
    }

	@Transactional
    def agregarConcepto(Long id,String tipo){
    	
    	request.withFormat{
    		html {
				def conceptos=ConceptoDeNomina.findAll{tipo==tipo}
    			render (template:'agregarConceptoform'
    			,model:[nominaEmpleadoId:id,
    				nominaPorEmpleadoDetInstance:new NominaPorEmpleadoDet(),
    				conceptosList:conceptos
    				])
    		}
			form {
				
				NominaPorEmpleado ne=NominaPorEmpleado.get(id)
				println 'Agrgndo concepto: '+ne
				NominaPorEmpleadoDet det=new NominaPorEmpleadoDet(params)
				ne.addToConceptos(det)
				ne.save(failOnError:true)
				
				redirect action:'edit',params:[id:ne.id]
			}    	
    	}
    	
    	
    }

    
}
