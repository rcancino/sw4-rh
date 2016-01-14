package com.luxsoft.sw4.rh


import java.util.Set;

import com.luxsoft.sw4.rh.sat.SatIncapacidad

import org.grails.databinding.BindingFormat
import groovy.transform.EqualsAndHashCode
import groovy.time.TimeCategory

@EqualsAndHashCode(includes='empleado,referenciaImms')
class Incapacidad {

	static searchable = true
	
	Empleado empleado
	
	String referenciaImms
	
	String comentario
	
	SatIncapacidad tipo
	
	@BindingFormat("dd/MM/yyyy")
	Date fechaInicial

	@BindingFormat("dd/MM/yyyy")
	Date fechaFinal

	Integer dias

	Date dateCreated
	
	Date lastUpdated

	Integer porcentaje
	String tipoRiesgo
	String secuela
	String control


    static constraints = {
    	
    	comentario nullable:true,maxSize:250
    	porcentaje 	nullable:true
    	tipoRiesgo	nullable:true,inList:['ACCIDENTE DE TRABAJO','ACCIDENTE TRAYECTO','ENFERMEDAD PROFESIONAL']
    	secuela		nullable:true,inList:['NINGUNA','INCAPACIDAD TEMPORAL','VALUACION INICIAL PROVISIONAL','VALUACION INICIAL DEFINITIVA','DEFUNCION','RECAIDA','VALUACION POST ALTA']
    	control		nullable:true,inList:['UNICA','INICIAL','SUBSECUENTE','ALTA O ST-2','PRENATAL','ENLACE','POSTNATAL']
    }

	static transients=['dias']
	
	static mapping = {
		fechaInicial type:'date'
		fechaFinal type:'date'
	}

	public Integer getDias(){
		use(TimeCategory){
			def duration= fechaFinal-fechaInicial+1.day
			return duration.days
		}
	}

    String toString(){
    	return "$empleado $tipo  "
    }

   

}
