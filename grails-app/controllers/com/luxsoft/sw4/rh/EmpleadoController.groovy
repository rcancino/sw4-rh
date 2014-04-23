package com.luxsoft.sw4.rh



import com.luxsoft.sw4.Empresa
import grails.plugin.springsecurity.annotation.Secured



@Secured(['ROLE_ADMIN','RH_USER'])
class EmpleadoController {
	
    def empleadoService
	
	def index(Long max) {
		params.max = Math.min(max ?: 10, 100)
		params.sort=params.sort?:'apellidoPaterno'
		params.order='asc'
		[empleadoInstanceList:Empleado.list(params),empleadoInstanceCount:Empleado.count()]
	}
	
	def show(Empleado empleadoInstance){
		redirect action:'generales',params:params
	}

	def generales(Empleado empleadoInstance){
		[empleadoInstance:empleadoInstance,edit:params.edit]
	}
	
	def create(){
		[empleadoInstance:new Empleado()]
	}

	def update(Empleado empleadoInstance){
		//def empleadoInstance=Empleado.get(id)
		log.info 'Salvando empleado: '+empleadoInstance
		log.info 'Datos de salario: '+empleadoInstance.salario
		log.info 'Pamaetros: '+params
		//bindData(empleadoInstance.salario, params)
		
		if(empleadoInstance==null){
			notFound()
			return
		}
		
		try{
			empleadoInstance=empleadoService.updateEmpleado(empleadoInstance)
			flash.message="Empleado ${empleadoInstance.clave} actualizado"
			render view:'generales',model:[empleadoInstance:empleadoInstance,edit:false]
		}catch(EmpleadoException ex){
			render view:'generales',model:[empleadoInstance:empleadoInstance,edit:true]
		}
		
	}

	def perfil(Empleado empleadoInstance){
		[empleadoInstance:empleadoInstance,edit:params.edit]
	}
	def salario(Empleado empleadoInstance){
		[empleadoInstance:empleadoInstance,edit:params.edit]
	}
	def seguridadSocial(Empleado empleadoInstance){
		[empleadoInstance:empleadoInstance,edit:params.edit]
	}
	
	def datosPersonales(Empleado empleadoInstance){
		[empleadoInstance:empleadoInstance,edit:params.edit]
	}

	protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message'
                	, args: [message(code: 'empleadoInstance.label', default: 'Empleado'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
	
	

}

