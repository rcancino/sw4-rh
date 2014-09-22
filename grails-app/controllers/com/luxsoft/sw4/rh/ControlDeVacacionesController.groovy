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

	def actualizar(ControlDeVacaciones control){
		log.info 'Actaulizando control de vacaciones'
		control=vacacionesService.actualizarControl(control)
		flash.message="Actualización exitosa "
		redirect action:'show',params:[id:control.id]
	}
	
	def show(ControlDeVacaciones controlDeVacacionesInstance){
		def percepcionesPorPrimas=NominaPorEmpleadoDet
			.findAll("from NominaPorEmpleadoDet n where n.parent.empleado=? "
				+" and n.concepto.clave=? and n.parent.nomina.ejercicio=?"
				,[controlDeVacacionesInstance.empleado,'P024',controlDeVacacionesInstance.ejercicio.toInteger()])
		def totalExcentoPrima=percepcionesPorPrimas.sum 0.0,{it.importeExcento}
		def totalGravadoPrima=percepcionesPorPrimas.sum 0.0,{it.importeGravado}
		[controlDeVacacionesInstance:controlDeVacacionesInstance
			,percepcionesPorPrimas:percepcionesPorPrimas
			,totalExcentoPrima:totalExcentoPrima
			,totalGravadoPrima:totalGravadoPrima]
	}
}
