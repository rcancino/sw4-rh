package com.luxsoft.sw4.rh

class EmpleadoControllerBak {
	
    static scaffold = true
	
    static navigationScope="catalogos"
	
	
	def create(){
		//[empleadoInstance:new Empleado()]
		redirect action:'altaDeEmpleado'
	}
	
	
	def altaDeEmpleadoFlow={
		initialize {
			action{
				log.debug 'Iniciando wizard de empleado'
				[empleadoInstance:new Empleado(apellidoPaterno:'CANCINO')]
			}
			on("success").to "datosGenerales"
		}
		
		datosGenerales{
			on("siguiente"){
				//flow.empleadoInstance=new Empleado(params)
				//!flow.empleadoInstance.validate()?error():success()
				def empleado=new Empleado(params)
				if(empleado.hasErrors()){
					flow.empleadoInstance=empleado
					return error()
				}
				[empleadoInstance:empleado,pefil:empleado.perfil?:new PerfilDeEmpleado(empleado:empleado)]
			}.to "perfilDePuesto"
			on("salvar").to "salvar"
			on("cancelar").to "terminar"
		}
		
		perfilDePuesto{
			on("siguiente"){
				def empleado=flow.empleadoInstance
				def perfil=new PerfilDeEmpleado(params)
				perfil.empleado=empleado
				perfil.empresa=perfil?.ubicacion?.empresa
				empleado.perfil=perfil
				//println "Validando perfil: $perfil?.numeroDeEmpleado $perfil?.empleado"
				!perfil.validate()?error():success()
			}.to "datosPersonales"
			on("anterior").to "datosGenerales"
			on("cancelar").to "terminar"
			on("salvar").to "salvar"
		}
		
		datosPersonales{
			on("siguiente").to "direcciion"
			on("anterior").to "datosGenerales"
			on("cancelar").to "terminar"
			on("salvar").to "salvar"
		}
		salvar{
			action{
				println 'Salvando el empleado: '+flow.empleadoInstance+  " Perfil: "+flow.empleadoInstance?.perfil
				flow.empleadoInstance.save(flush:true)
			}
			on("success").to "terminar"
			
		}
		
		terminar{
			redirect action:'index'
		}
		
		
	}

}
