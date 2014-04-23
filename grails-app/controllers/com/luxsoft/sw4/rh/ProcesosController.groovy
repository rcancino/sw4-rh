package com.luxsoft.sw4.rh

import grails.plugin.springsecurity.annotation.Secured;
import com.luxsoft.sw4.Periodo

@Secured(['ROLE_ADMIN'])
class ProcesosController {
	
	def empleadoService

    def index() { }
	
	def empleados(Long max){
		params.max = Math.min(max ?: 20, 100)
		params.sort=params.sort?:'perfil.ubicacion'
		params.order='asc'
		
		def res=Empleado.findAllByStatus('ALTA',params)
		def total=Empleado.where{status=='ALTA'}.count()
		
		//[empleadoInstanceList:Empleado.list(params),empleadoInstanceCount:Empleado.count()]
		[empleadoInstanceList:res,empleadoInstanceCount:total]
	}
	
	def salarioDiarioIntegrado(){
		params.periodo=new Periodo('01/01/2014','02/03/2014')
		[rows:[],periodo:params.periodo]
	}
	
	def calcularSalarioDiarioIntegrado(){
		def periodo=new Periodo('01/01/2014','02/03/2014')
		def rows=empleadoService.calcularSalarioDiarioIntegrado(periodo)		
		render view:'salarioDiarioIntegrado',model:[rows:rows,periodo:periodo]
	}
	
	def aplicarSalarioDiarioIntegrado(){
		def periodo=new Periodo('01/01/2014','02/03/2014')
		empleadoService.actualizarSalarioDiarioIntegrado(periodo)
		redirect action:'empleados'
	}
	
	
}
