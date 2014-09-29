package com.luxsoft.sw4.rh

import groovy.transform.EqualsAndHashCode
import org.grails.databinding.BindingFormat
import groovy.time.TimeCategory


@EqualsAndHashCode(includes='ejercicio,empleado')
class Aguinaldo {

	Integer ejercicio

	Empleado empleado

	@BindingFormat('dd/MM/yyyy')
	Date fechaInicial

	@BindingFormat('dd/MM/yyyy')
	Date fechaFinal

	BigDecimal totalAguinaldo=0.0
	BigDecimal totalBono=0.0

	NominaPorEmpleado nominaPorEmpleado

	Integer diasDelEjercicio

	Date dateCreated
	Date lastUpdated


    static constraints = {
		empleado unique:['ejercicio']
		nominaPorEmpleado nullable:true
    }

    static mapping = {
		fechaInicial type:'date'
		fechaFinal type:'date'
	}

	static transients = ['diasDelEjercicio']

    String toString(){
    	return "$empleado - $ejercicio"
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
}
