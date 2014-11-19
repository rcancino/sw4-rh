package com.luxsoft.sw4.rh

import grails.validation.Validateable

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
	
	
	def cambiarBimestre(CalculoBimestralCommand command){
		def bimestre=command.bimestre
		if(!session.bimestre)
			session.bimestre=command.bimestre
		def ejercicio=command.ejercicio
		render view:'salarioDiarioIntegrado',model:[rows:[],bimestre:bimestre,ejercicio:ejercicio]
		
	}
	
	def salarioDiarioIntegrado(){
		[rows:[],bimestre:session.bimestre,ejercicio:session.ejercicio]
	}
	
	def calcularSalarioDiarioIntegrado(CalculoBimestralCommand command){
		//def ejercicio=params.ejercicio
		//def bimestre=params.bimestre
		session.bimestre=command.bimestre
		def rows=salarioService.calcularSalarioDiarioOld(command.ejercicio,command.bimestre)		
		render view:'salarioDiarioIntegrado',model:[rows:rows,bimestre:command.bimestre,ejercicio:command.ejercicio]
	}
	
	def aplicarSalarioDiarioIntegrado(){
		//def rows=salarioService.aplicarCalculoDeSalarioDiario(session.ejercicio,session.bimestre)
		//def periodo=new Periodo('01/01/2014','02/03/2014')
		//empleadoService.actualizarSalarioDiarioIntegrado(periodo)
		//redirect action:'empleados'
		render view:'salarioDiarioIntegrado',model:[rows:rows,bimestre:session.bimestre,ejercicio:session.ejercicio]
	}
	
	
}

@Validateable
class CalculoBimestralCommand{
	
	Integer ejercicio
	Integer bimestre
	
	static constraints = {
		ejercicio inList:2014..2018
		bimestre inList:1..6
		
    }
	
	
}
