package com.luxsoft.sw4.rh


import grails.transaction.Transactional
import grails.plugin.springsecurity.annotation.Secured

@Transactional(readOnly = true)
@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
class PensionAlimenticiaController {

    def index() { }
	
	def edit(Long id){
		Empleado emp=Empleado.get(id)
		def pension=PensionAlimenticia.findByEmpleado(emp)
		[empleadoInstance:emp,pensionInstance:pension]
	}
	
	def create(Long id){
		Empleado emp=Empleado.get(id)
		def pension=new PensionAlimenticia(empleado:emp)
		[empleadoInstance:emp,pensionInstance:pension]
	}
	
	@Transactional
	def salvar(){
		PensionAlimenticia pension=new PensionAlimenticia(params)
		pension.validate()
		if(pension.hasErrors()){
			flash.message="Errores de validacion"
			render view:'create',model:[empleadoInstance:pension.empleado,pensionInstance:pension]
			return
		}
		pension.save failOnErrir:true
		flash.message="Pensión generada $pension.id"
		redirect action:'edit', params:[id:pension.empleado.id]
	}
	
	
	@Transactional
	def update(PensionAlimenticia pension){
		pension.validate()
		if(pension.hasErrors()){
			flash.message="Errores de validacion"
			render view:'edit',model:[empleadoInstance:pension.empleado,pensionInstance:pension]
			return
		}
		pension.save failOnErrir:true
		flash.message="Pensión actualizada $pension.id"
		redirect action:'edit', params:[id:pension.empleado.id]
	}
	
	@Transactional
	def eliminar(PensionAlimenticia pension){
		pension.delete()
		flash.message="Pension eliminada: $pension.id"
		redirect action:'edit',params:[id:pension.empleado.id]
	}
}
