package com.luxsoft.sw4.rh

import grails.converters.JSON
import grails.plugin.cache.Cacheable
import grails.plugin.springsecurity.annotation.Secured


@Secured(['ROLE_ADMIN','RH_USER'])
class EmpleadoRestController {
	
	def salarioService
	
	//@Cacheable('catalogoDeEmpleados')
	def getEmpleados() {
		def term=params.term.trim()+'%'
		def query=Empleado.where{status=='ALTA'}
		query=query.where{
			apellidoPaterno=~term || apellidoMaterno=~term || nombres=~term
		}
		def list=query.list(max:20, sort:"apellidoPaterno")
		//println query.count()
		println list.size()
		list=list.collect{ emp->
			def nombre="$emp.apellidoPaterno $emp.apellidoMaterno $emp.nombres"
			[id:emp.id
				,label:nombre
				,value:nombre
				,numeroDeTrabajador:emp.perfil.numeroDeTrabajador.trim()
			]
		}
		def res=list as JSON
		
		render res
	}

	def getEmpleadosConSalario() {
		def term=params.term.trim()+'%'
		def query=Empleado.where{status=='ALTA'}
		query=query.where{
			apellidoPaterno=~term || apellidoMaterno=~term || nombres=~term
		}
		def list=query.list(max:15, sort:"apellidoPaterno")
		list=list.collect{ emp->
			def nombre="$emp.apellidoPaterno $emp.apellidoMaterno $emp.nombres"
			[id:emp.id
				,label:nombre
				,value:nombre
				,numeroDeTrabajador:emp.perfil.numeroDeTrabajador.trim()
				,salarioDiario:emp.salario.salarioDiario
				,sdi:emp.salario.salarioDiarioIntegrado
			]
		}
		def res=list as JSON
		render res
	}
	
	def calcularSdi() {
		
		def empleadoId=params.empleadoId
		def salarioNuevo=params.salarioNuevo as BigDecimal
		def fecha=params.date('fecha','dd/MM/yyyy')
		Empleado e=Empleado.get(empleadoId)
		def sdi=0.0
		if(e) {
			sdi=salarioService.calcularSalarioDiarioIntegrado(e,fecha,salarioNuevo)
		}
		def res=[sdi:sdi.SDI_NVO] as JSON
		log.info 'res: '+res+ " Em.id"+empleadoId+ " params: "+params
		render res 
	}
	
	
	def calcularSdiNuevo() {
		
		def empleadoId=params.empleadoId
		def salarioNuevo=params.salarioNuevo as BigDecimal
		def fecha=new Date()
		Empleado e=Empleado.get(empleadoId)
		def sdi=0.0
		if(e) {
			sdi=salarioService.calcularSalarioDiarioIntegradoNuevo(e,salarioNuevo)
		}
		def res=[sdi:sdi.SDI_NVO] as JSON
		log.info 'res: '+res+ " Em.id"+empleadoId+ " params: "+params
		render res
	}

}
