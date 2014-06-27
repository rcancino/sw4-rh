package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Autorizacion
import org.grails.databinding.BindingFormat

class Incentivo {
	
	Empleado empleado
	
	String tipo
	
	Ubicacion ubicacion
	
	CalendarioDet calendarioInicial
	
	CalendarioDet calendarioFinal
	
	Boolean otorgado=true
	
	Integer faltas
	
	Integer retardoMenor
	Integer retardoMayor
	Integer retardoComida
	Integer retardoTotal
	
	double tasaBono1=0
	double tasaBono2=0
	
	BigDecimal ingresoBase=0.0
	BigDecimal incentivo=0.0
	
	String comentario
	
	Date dateCreated
	Date lastUpdated

    static constraints = {
		tipo inList:['QUINCENAL','MENSUAL']
		comentario nullable:true
    }
}
