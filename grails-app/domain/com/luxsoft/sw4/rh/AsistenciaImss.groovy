package com.luxsoft.sw4.rh

import groovy.transform.ToString
import org.grails.databinding.BindingFormat

@ToString(includeNames=true,includePackage=false)
class AsistenciaImss {

	Empleado empleado

	Asistencia asistencia

	CalendarioDet calendarioDet

	@BindingFormat('dd/MM/yyyy')
	Date inicioAsistencia

	@BindingFormat('dd/MM/yyyy')
	Date finAsistencia

	@BindingFormat('dd/MM/yyyy')
	Date inicioNomina
	
	@BindingFormat('dd/MM/yyyy')
	Date finNomina
	
	Date dateCreated
	
	Date lastUpdated

    static constraints = {
    }

    static hasMany = [partidas:AsistenciaImssDet]

    static mapping = {
		inicioAsistencia type:'date'
		finAsistencia type:'date'
		inicioNomina type:'date'
		finNomina type:'date'
		partidas cascade: "all-delete-orphan"
	}


}
