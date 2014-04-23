package com.luxsoft.sw4.rh


import com.luxsoft.sw4.Empresa;

import grails.transaction.Transactional
import grails.validation.Validateable
import groovy.transform.ToString
import grails.plugin.springsecurity.annotation.Secured


//@Transactional(readOnly = true)
@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
class NominaController {

	def nominaService
    //static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    
    def index(Integer max) {
        params.max = Math.min(max ?: 60, 100)
		params.periodicidad=params.periodicidad?:'QUINCENAL'
		params.sort=params.sort?:'folio'
		params.order='asc'
		
		def query=Nomina.where{periodicidad==params.periodicidad}
		[nominaInstanceList:query.list(params),nominaInstanceListTotal:query.count(params),periodicidad:params.periodicidad]
    }

    def show(Long id) {
		def nominaInstance=Nomina.get(id)
		
		[nominaInstance:nominaInstance]
		        
    }

    def agregar(){
		def nomina=Nomina.get(params.id)
		params.periodicidad=nomina.periodicidad
		params.folio=nomina.folio
        redirect view:'agregarPartida', params:params
    }
	
	def generar(String periodicidad){
		nominaService.generarNominas('GENERAL',periodicidad)
		redirect action:'index' ,params:params
	}

    def delete(Nomina nominaInstance){
    	if(nominaInstance==null){
    		notFound()
    		return
    	}
    	nominaService.eliminarNomina(nominaInstance.id)
    	flash.message="Nomina ${nominaInstance.id} eliminada"
    	redirect action:'index',params:[periodicidad:nominaInstance.periodicidad]
    }

    private List findEmpleadosDisponibles(Long nominaId,String periodicidad){
        def res=Empleado.findAll(
            "from Empleado e where e.status ='ALTA' and e.salario.periodicidad=? and e.id not in(select ne.empleado.id from NominaPorEmpleado ne where ne.nomina.id=?) "
            ,[periodicidad,nominaId])
    }

    protected void notFound() {
		request.withFormat {
			form multipartForm {
				flash.message = message(code: 'default.not.found.message'
					, args: [message(code: 'nomina.label', default: 'Nómina'), params.id])
				redirect action: "index", method: "GET"
			}
			'*'{ render status: NOT_FOUND }
		}
	}
    
}

