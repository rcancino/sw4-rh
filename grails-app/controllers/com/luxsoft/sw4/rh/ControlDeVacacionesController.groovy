package com.luxsoft.sw4.rh


import grails.plugin.springsecurity.annotation.Secured;

@Secured(['ROLE_ADMIN','RH_USER'])
//@Transactional(readOnly = true)
class ControlDeVacacionesController {
    static scaffold = true
	
	def vacacionesService
	
	def index() {
		
		params.sort=params.sort?:'lastUpdated'
		params.order='desc'
		def ejercicio=session.ejercicio
		def tipo='SEMANA'
		//def query=ControlDeVacaciones.where{ejercicio==ejercicio}
		def list=ControlDeVacaciones.findAll{ejercicio==ejercicio}
		def partidasMap=list.groupBy([{it.empleado.perfil.ubicacion.clave}])
		[partidasMap:partidasMap,ejercicio:ejercicio,tipo:tipo]
		
	}
	
	def generar(Integer id){
		
		vacacionesService.generarControl(id)
		redirect action:'index'
	}
}
