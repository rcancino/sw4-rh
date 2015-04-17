package com.luxsoft.sw4.rh

import groovy.transform.EqualsAndHashCode
import org.grails.databinding.BindingFormat

@EqualsAndHashCode(includes='ejercicio,empleado')
class Ptu {
    
	Integer ejercicio

	Empleado empleado

	@BindingFormat('dd/MM/yyyy')
	Date fechaInicial

	@BindingFormat('dd/MM/yyyy')
	Date fechaFinal

	NominaPorEmpleado nominaPorEmpleado

	//Base para el calculo
	BigDecimal salario =0.0//Percepcion
	BigDecimal vacaciones=0.0 //Pecepcion
	BigDecimal comisiones=0.0 //Percepcion
	BigDecimal retardos=0.0  //Deduccion
	BigDecimal total=0.0
	BigDecimal topeAnual=0.0
	
	Integer antiguedad=0
	Integer diasDelEjercicio=0

	Boolean noAsignado
	String noAsignadoComentario
	
	Date dateCreated
	Date lastUpdated


    static constraints = {
		empleado unique:['ejercicio']
		nominaPorEmpleado nullable:true
		noAsignadoComentario nullable:true,maxSize:100
		noAsignadoComentario nullable:true
		
    }

    static mapping = {
		fechaInicial type:'date'
		fechaFinal type:'date'
	}

	static transients = ['diasDelEjercicio','antiguedad']

    String toString(){
    	return "PTU $ejercicio  $empleado "
    }

    public Integer getDiasDelEjercicio(){
    	if(!diasDelEjercicio){
    		use(TimeCategory){
    			def duration= fechaFinal-fechaInicial+1.day
    			diasDelEjercicio=duration.days
    		}
    	}
    	return diasDelEjercicio
		
	}

	public Integer getAntiguedad(){
    	if(!antiguedad && empleado){
			
			def fecha=fechaFinal
			if(empleado.baja && (empleado.alta<empleado.baja.fecha)){
				if(fechaFinal>empleado.baja.fecha)
					fecha=empleado.baja.fecha
			}
			return (fecha-empleado.alta)+1
    	}
    	return antiguedad
		
	}

	


}

