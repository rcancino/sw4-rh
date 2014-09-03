package com.luxsoft.sw4.rh

import grails.plugin.springsecurity.annotation.Secured;
import com.luxsoft.sw4.Periodo

@Secured(['ROLE_ADMIN'])
class ProcesosController {
	
	def empleadoService
	
	def salarioService

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
		def cal=session.calendarioSemana
		def bimestre=4
		def ejercicio=session.ejercicio
		//def res=CalendarioDet.executeQuery("select min(d.inicio),max(d.fin) from CalendarioDet d where d.bimestre=?",[bimestre])
		
		//def inicio=res.get(0)[0]
		//def fin=res.get(0)[1]
		//def periodo=new Periodo(inicio,fin)
		[rows:[],bimestre:bimestre]
	}
	
	def calcularSalarioDiarioIntegrado(){
		//def periodo=new Periodo('01/01/2014','02/03/2014')
		//def rows=empleadoService.calcularSalarioDiarioIntegrado(periodo)
		def rows=salarioService.calcularSalarioDiario(2014,4)		
		render view:'salarioDiarioIntegrado',model:[rows:rows]
	}
	
	def aplicarSalarioDiarioIntegrado(){
		def periodo=new Periodo('01/01/2014','02/03/2014')
		empleadoService.actualizarSalarioDiarioIntegrado(periodo)
		redirect action:'empleados'
	}
	
	
}
