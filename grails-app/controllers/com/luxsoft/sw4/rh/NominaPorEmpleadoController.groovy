package com.luxsoft.sw4.rh

import grails.transaction.Transactional;
import grails.plugin.springsecurity.annotation.Secured

//@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
@Secured(['ROLE_ADMIN'])
class NominaPorEmpleadoController {
	
	def nominaPorEmpleadoService

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
	def update(NominaPorEmpleado ne){
		log.info 'Actualizando ne '+ne
		//def ne=nominaPorEmpleadoService.actualizarNominaPorEmpleado(id)
		ne.save(failOnError:true)
		ne=nominaPorEmpleadoService.actualizarNominaPorEmpleado(ne.id)
		redirect action:'edit' ,params:[id:ne.id]
		//render view:'edit',model:[nominaPorEmpleadoInstance:ne]
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
				log.info 'Agrgndo concepto: '+ne
				NominaPorEmpleadoDet det=new NominaPorEmpleadoDet(params)
				
				if(det?.concepto?.importeExcento){
					det.importeExcento=det.importeGravado
					det.importeGravado=0.0
				}
				ne.addToConceptos(det)
				//ne.save(failOnError:true)
				ne=nominaPorEmpleadoService.actualizarNominaPorEmpleado(ne)
				redirect action:'edit',params:[id:ne.id]
			}    	
    	}
    	
    	
    }
	
	@Transactional
	def eliminarConcepto(Long id){
		def ne=nominaPorEmpleadoService.eliminarConcepto(id)
		redirect action:'edit',params:[id:ne.id]
	}
	
	def actualizarNominaPorEmpleado(Long id){
		def ne=nominaPorEmpleadoService.actualizarNominaPorEmpleado(id)
		redirect action:'edit',params:[id:ne.id]
	}
	
	protected void notFound() {
		request.withFormat {
			form multipartForm {
				flash.message = message(code: 'default.not.found.message'
					, args: [message(code: 'nominaPorEmpleadoInstance.label', default: 'Nómina por Empleado'), params.id])
				redirect action: "index", method: "GET"
			}
			'*'{ render status: NOT_FOUND }
		}
	}

    
}
