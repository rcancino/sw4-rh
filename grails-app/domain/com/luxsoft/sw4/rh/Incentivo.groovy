package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Autorizacion
import org.grails.databinding.BindingFormat

class Incentivo {
	
	Empleado empleado
	
	@BindingFormat("dd/MM/yyyy")
	Date fecha
	
	Autorizacion autorizacion
	
	CalendarioDet calendarioDet
	
	String tipo
	
	BigDecimal sueldoBase=0
	
	BigDecimal bono1=0.5
	BigDecimal importeBono1=0.0
	
	BigDecimal bono2=0.5
	BigDecimal importeBono2=0.0
	
	String comentario
	
	Date dateCreated
	Date lastUpdated

    static constraints = {
		calendarioDet nullable:true
		autorizacion nullable:true
		tipo inList:['PUNTUALIDAD','INVENTARIO_FISICO','OTRO']
		comentario nullable:true
    }
}
