package com.luxsoft.sw4.rh

import grails.converters.JSON
import grails.plugin.cache.Cacheable
import grails.plugin.springsecurity.annotation.Secured


@Secured(['ROLE_ADMIN','RH_USER'])
class EmpleadoRestController {
	
	
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
		println res
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
		println res
		render res
	}

}