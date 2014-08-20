package com.luxsoft.sw4.rh

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes='empleado')
class Incentivo {
	
	Empleado empleado
	
	Integer ejercicio
	
	String tipo

	BigDecimal tasaBono1=0.0
	
	BigDecimal tasaBono2=0.0
	
	Boolean otorgado=true
	
	Asistencia asistencia
	
	BigDecimal ingresoBase=0.0
	
	BigDecimal incentivo=0.0
	
	String status='PENDIENTE'

	NominaPorEmpleadoDet nominaPorEmpleadoDet
	
	String comentario	
	
	Date dateCreated
	Date lastUpdated

    static constraints = {
		tipo inList:['SEMANAL','QUINCENAL','MENSUAL']
		status inList:['PENDIENTE','CERRADO']
		comentario nullable:true
    }
	
	String toString() {
		"${empleado} ${tipo} "
	}
}
