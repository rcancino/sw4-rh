package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Autorizacion
import org.grails.databinding.BindingFormat

class Incentivo {
	
	Empleado empleado
	
	Integer ejercicio
	
	String tipo
	
	Ubicacion ubicacion
	
	CalendarioDet calendarioIni
	
	CalendarioDet calendarioFin
	
	Boolean otorgado=true
	
	Integer faltas=0
	
	Integer minutosNoLaborados=0
	
	Integer retardoMenor=0
	
	Integer retardoMayor=0
	
	Integer retardoComida=0
	
	Integer retardoTotal=0
	
	double tasaBono1=0
	
	double tasaBono2=0
	
	BigDecimal ingresoBase=0.0
	
	BigDecimal incentivo=0.0
	
	String status='PENDIENTE'
	
	String comentario	
	
	Date dateCreated
	Date lastUpdated

    static constraints = {
		tipo inList:['QUINCENAL','SEMANAL']
		status inList:['PENDIENTE','CERRADO']
		comentario nullable:true
    }
	
	String toString() {
		"${empleado} ${tipo} ${calendarioIni.folio} al ${calendarioFin.folio}"
	}
}
