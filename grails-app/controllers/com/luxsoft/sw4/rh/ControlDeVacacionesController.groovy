package com.luxsoft.sw4.rh


import grails.plugin.springsecurity.annotation.Secured;

@Secured(['ROLE_ADMIN','RH_USER'])
//@Transactional(readOnly = true)
class ControlDeVacacionesController {
    static scaffold = true
	
	def vacacionesService
	
	def index() {
		
		//params.sort=params.sort?:'em'
		params.order='desc'
		def ejercicio=session.ejercicio
		def tipo='SEMANA'
		//def query=ControlDeVacaciones.where{ejercicio==ejercicio}
		def list=ControlDeVacaciones.findAll{ejercicio==ejercicio}
		list=list.sort{a,b ->
			a.empleado.perfil.ubicacion.clave<=>b.empleado.perfil.ubicacion.clave?:a.empleado.nombre<=>b.empleado.nombre
		}
		//def partidasMap=list.groupBy([{it.empleado.perfil.ubicacion.clave}])
		[partidasList:list,ejercicio:ejercicio,tipo:tipo]
		
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
		
		def nominas=NominaPorEmpleado
			.findAll("from NominaPorEmpleado ne where ne.empleado=? and ne.nomina.ejercicio=? and (ne.vacaciones>0 or ne.asistencia.vacacionesp>0)"
			,[controlDeVacacionesInstance.empleado,controlDeVacacionesInstance.ejercicio.toInteger()])
		//def totalDias=percepcionesPorPrimas.sum 0.0,{it.vacaciones}
		[controlDeVacacionesInstance:controlDeVacacionesInstance
			,percepcionesPorPrimas:percepcionesPorPrimas
			,totalExcentoPrima:totalExcentoPrima
			,totalGravadoPrima:totalGravadoPrima,nominas:nominas]
	}
}
