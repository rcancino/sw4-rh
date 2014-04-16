package com.luxsoft.sw4.rh

import grails.validation.Validateable;
import groovy.transform.ToString;

import java.util.Date;

import org.grails.databinding.BindingFormat

import com.luxsoft.sw4.Empresa





class EmpleadoController {
	
    static scaffold = true
	
    static navigationScope="catalogos"
	
	def index(Long max) {
		params.max = Math.min(max ?: 10, 100)
		params.sort=params.sort?:'apellidoPaterno'
		params.order='asc'
		[empleadoInstanceList:Empleado.list(params),empleadoInstanceCount:Empleado.count()]
	}
	
	def show(Empleado empleadoInstance){
		[empleadoInstance:empleadoInstance]
	}
	
	def create(){
		[empleadoInstance:new Empleado()]
	}
	
	
	
//	def altaDeEmpleadoFlow={
//		initialize {
//			action{
//				log.info 'Iniciando wizard para alta de empleado'
//				println 'Inicializando wizard...'
//				// Cargamos la empresa en el flow
//				def empresa=Empresa.first()
//				assert empresa,"Debe existir la empresa a la que pertenecera el empleado"
//				
//				[empleado:new Empleado(),empresa:empresa]
//			}
//			on("success").to "datosGenerales"
//		}
//		
//		datosGenerales{
//			on("siguiente"){ 
//				log.info "Validando datos generales: "+params
//				println 'Validando datos generales: '+params
//				def empleado=flow.empleado
//				bindData(empleado,params)
//				
//				if(!empleado.validate()){
//					flow.empleado=empleado
//					return error()
//				}
//				//Preparamso el perfil para el siguiente paso
//				def perfil=new PerfilDeEmpleado(empleado:empleado,empresa:flow.empresa)
//				[empleado:empleado,perfil:perfil]
//				
//			}.to "perfilDePuesto"
//			on("cancelar").to "terminar"
//		}
//		
//		perfilDePuesto{
//			on("siguiente"){
//				println 'Validando perfil de empleado: '+params
//				def perfil=flow.perfil//new PerfilDeEmpleado(params)
//				bindData(perfil,params)
//				if(!perfil.validate()){
//					flow.perfil=perfil
//					return error()
//				}
//				[perfil:perfil]
//			}.to "datosPersonales"
//			on("anterior").to "datosGenerales"
//			on("cancelar").to "terminar"
//			
//		}
//		
//		datosPersonales{
//			on("siguiente").to "direcciion"
//			on("anterior").to "datosGenerales"
//			on("cancelar").to "terminar"
//			on("salvar").to "salvar"
//		}
//		
//		salvar{
//			action{
//				def empleado=flow.empleado
//				assert empleado ,"Debe existir un empleado en el flow"
//				def perfil=flow.perfil
//				println 'Salvando el empleado: '+empleado+  " Perfil: "+perfil
//				empleado.save()
//			}
//			on("success").to "terminar"
//			
//		}
//		
//		terminar{
//			
//			redirect action:'index'
//		}
//		
//		
//	}

}
/**
@Validateable
@ToString(includeNames=true,includePackage=false)
class EmpleadoCmd implements Serializable{
	 
	String apellidoPaterno
	String apellidoMaterno
	String nombres
	String curp
	String rfc
	@BindingFormat("dd/MM/yyyy")
	Date alta
	String sexo 
	String status="ALTA"
	String tipo="SINDICALIZADO"
	
	static constraints={
		importFrom Empleado
	}
	def nombreCompleto(){
		"$apellidoPaterno $apellidoMaterno $nombres"
	}
}

@Validateable
@ToString(includeNames=true,includePackage=false)
class PerfilCmd implements Serializable{
	
	Empleado empleado
	Integer numeroDeTrabajador
	Puesto puesto
	Departamento departamento
	Ubicacion ubicacion
	
	static constraints={
		importFrom PerfilDeEmpleado
	}
}
**/