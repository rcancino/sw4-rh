package com.luxsoft.sw4.rh

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
@Transactional(readOnly = true)
class TurnoController {
    
    //static scaffold = true
    
    static allowedMethods = [save: "POST", update: "PUT"]

    def index(Long max){
    	params.max = Math.min(max ?: 15, 100)
    	[turnoInstanceList:Turno.list(params),turnoInstanceCount:Turno.count()]
    }

    def create(){
    	def turnoInstance=new Turno()
    	['DOMINGO','LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO'].each{
    		turnoInstance.addToDias(dia:it)
    	}
    	
    	[turnoInstance:turnoInstance]
    }


    @Transactional
    def save(Turno turno){
    	if(!turno){
    		notFound()
    		return
    	}
    	turno.validate()
    	if(turno.hasErrors()){
    		flash.message="Turno invalido"
    		render view:'create',model:[turnoInstance:turno]
    	}
    	turno.save(failOnError:true)
		flash.message="Turno generado: "+turno.id
		redirect action:'index'
    }

    def show(Long id){
    	def turnoInstance=Turno.get(id)
    	[turnoInstance:turnoInstance]
    }

    def edit(Long id){
    	def turnoInstance=Turno.get(id)
    	if(!turnoInstance){
    		notFound()
    		return
    	}
    	[turnoInstance:turnoInstance]
    }

    @Transactional
    def update(Turno turnoInstance){
    	if(!turnoInstance){
    		notFound()
    		return
    	}
    	turnoInstance.validate()
    	if(turnoInstance.hasErrors()){
    		flash.message="Turno invalido"
    		render view:'edit',[turnoInstance:turnoInstance]

    	}
    	turnoInstance.save failOnError:true
    	redirect action:'show',params:[id:turnoInstance.id]

    }

    @Transactional
    def delete(Long id){
    	def turnoInstance=Turno.get(id)
    	if(!turnoInstance){
    		notFound()
    		return
    	}
    	turnoInstance.delete flush:true
    	flash.mesage="Turno ${turnoInstance.id} eliminado"
    	redirect action:'index'
    }

    protected void notFound() {
		request.withFormat {
			form multipartForm {
				flash.message = "No existe el turno ${params.id}"
				redirect action: "index", method: "GET"
			}
			'*'{ render status: NOT_FOUND }
		}
	}
}
