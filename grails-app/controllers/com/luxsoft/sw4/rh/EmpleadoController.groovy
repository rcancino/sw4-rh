package com.luxsoft.sw4.rh



import com.luxsoft.sw4.Empresa
import grails.plugin.springsecurity.annotation.Secured



@Secured(['ROLE_ADMIN','RH_USER'])
class EmpleadoController {
	
    def empleadoService
	
	def index(Long max) {
		//params.max = Math.min(max ?: 50, 100)
		params.sort=params.sort?:'apellidoPaterno'
		params.order='asc'
		def tipo=params.tipo?:'QUINCENAL'
		def query=Empleado.where{
			activo==true && salario.periodicidad==tipo
		}
		def list=query.list(params)
		[empleadoInstanceList:list,
			empleadoInstanceCount:query.count(),
			tipo:tipo]
	}
	
	def show(Empleado empleadoInstance){
		redirect action:'generales',params:params
	}
	
	def search(String apellidoPaterno,String apellidoMaterno){
		//println 'Buscando empleados: '+apellidoPaterno
		params.max = 20
		params.sort='apellidoPaterno'
		params.order='asc'
		def query=Empleado.where{apellidoPaterno=~apellidoPaterno+"%"}
		//println 'Encontrados: '+query.list(params).size()
		def model=[empleadoInstanceList:query.list(params),empleadoInstanceCount:query.count()]
		render view:'index',model:model
	}
	
	def searchAndShow(String apellidoPaterno,String apellidoMaterno) {
		def query=Empleado.where{apellidoPaterno==~apellidoPaterno }
		//query=query.where{apellidoMaterno==~apellidoMaterno }
		def found=query.list()
		
		if(found.size()==1) {
			params.id=found[0].id
			redirect action:'generales',params:params
		}else {
			render view:'index',model:[empleadoInstanceList:found,empleadoInstanceCount:found.size()]
		}
	}

	def generales(Empleado empleadoInstance){
		def baja=BajaDeEmpleado.findByEmpleado(empleadoInstance)?:new BajaDeEmpleado()
		[empleadoInstance:empleadoInstance,edit:params.edit]
	}
	
	def contactos(Empleado empleadoInstance){
		[empleadoInstance:empleadoInstance,edit:params.edit]
	}
	
	def create(){
		[empleadoInstance:new Empleado(status:'ALTA')]
	}
	
	def save(Empleado empleado) {
		log.info 'Salvando empleado nuevo: '+empleado
		try {
			def res=empleadoService.save empleado
			
			render view:'generales',model:[empleadoInstance:res,edit:true]
		}catch(EmpleadoException ex) {
			ex.printStackTrace()
			flash.message=ex.message
			println 'Errores: '+ex.empleado.errors
			render view:'create' ,model:[empleadoInstance:ex.empleado,edit:true]
		}
	}

	def update(Empleado empleadoInstance){
		//def empleadoInstance=Empleado.get(id)
		log.info 'Salvando empleado: '+empleadoInstance
		log.info 'Datos de salario: '+empleadoInstance.salario
		println 'Salvando: '+empleadoInstance.perfil
		def v=params.view?:'generales'
		//log.info 'Pamaetros: '+params
		//bindData(empleadoInstance.salario, params)
		
		if(empleadoInstance==null){
			notFound()
			return
		}
		
		try{
			empleadoInstance=empleadoService.updateEmpleado(empleadoInstance)
			flash.message="Empleado ${empleadoInstance.clave} actualizado"
			render view:v,model:[empleadoInstance:empleadoInstance,edit:false]
		}catch(EmpleadoException ex){
			render view:v,model:[empleadoInstance:ex.empleado,edit:true]
		}
		
	}
	
	def updateSalario(Empleado empleadoInstance){
		
		def v=params.view?:'salario'
		log.info "Actualizando salario para $empleadoInstance salario: $empleadoInstance.salario.salarioDiario SDI: $empleadoInstance.salario.salarioDiarioIntegrado "
		
		try{
			empleadoInstance=empleadoService.updateSalario(empleadoInstance)
			flash.message="Salario actualizado  ${empleadoInstance.clave}  SDI: ${empleadoInstance.salario.salarioDiarioIntegrado}"
			render view:v,model:[empleadoInstance:empleadoInstance,edit:false]
		}catch(EmpleadoException ex){
			render view:v,model:[empleadoInstance:ex.empleado,edit:true]
		}
		
	}

	def perfil(Empleado empleadoInstance){
		[empleadoInstance:empleadoInstance,edit:params.edit]
	}
	
	def updatePerfile(){
		
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

