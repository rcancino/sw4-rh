package com.luxsoft.sw4.rh

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
class RegistroDeComisionesController {
    static scaffold = true
	
	def index(Long max){
		//params.max = Math.min(max ?: 1000, 10000)
		//params.sort=params.sort?:'lastUpdated'
		//params.order='desc'
		[registroDeComisionesInstanceList:RegistroDeComisiones.list(params)]
		
	}
	
	def getEmpleados() {
		def term=params.term.trim()+'%'
		def query=Empleado.where{status=='ALTA'}
		query=query.where{
			apellidoPaterno=~term || apellidoMaterno=~term || nombres=~term
		}
		//def list=query.list(max:20, sort:"apellidoPaterno")
		//ist=list.filter{it.perfil.puesto.clave=='COBRADOR'}
		def list=Empleado.findAll("from Empleado e where e.perfil.puesto.clave=?",['COBRADOR'])
		
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
	
	
}
