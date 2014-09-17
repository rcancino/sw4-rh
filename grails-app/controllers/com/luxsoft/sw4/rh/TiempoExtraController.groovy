package com.luxsoft.sw4.rh


import grails.validation.Validateable
import org.grails.databinding.BindingFormat
import grails.plugin.springsecurity.annotation.Secured

import com.luxsoft.sw4.Periodo


@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
class TiempoExtraController {
    static scaffold = true
	
	def tiempoExtraService
	
	def actualizar(ActualizarCmd cmd){
		log.info 'Actualizando tiempo exstra: '+cmd
		tiempoExtraService.actualizar(cmd.ejercicio,cmd.tipo,cmd.numero)
		redirect action:'index'
		
	}

	def actualizarTiempoExtra(TiempoExtra te){
		log.info 'Actualizando tiempo extra para: '+te
		tiempoExtraService.actualizarTiempoExtra(te.asistencia,te.tipo)
		redirect action:'show',params:[id:te.id]
	}
	
}

@Validateable
class ActualizarCmd{
	
	Integer ejercicio
	String tipo
	Integer numero
	
	String toString(){
		return "$ejercicio $tipo $numero"
	}
	
}
/*
@Validateable
class TiempoExtraCmd{
	
	Empleado empleado
	
	ConceptoDeNomina concepto
	
	@BindingFormat('dd/MM/yyyy')
	Date fechaInicial
	@BindingFormat('dd/MM/yyyy')
	
	Date fechaFinal
	
	String autorizo
	
	static constraints ={
		importFrom TiempoExtra
	}
}
*/
