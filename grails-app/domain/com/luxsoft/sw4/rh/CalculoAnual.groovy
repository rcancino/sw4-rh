package com.luxsoft.sw4.rh

import groovy.transform.EqualsAndHashCode
import org.grails.databinding.BindingFormat
import groovy.time.TimeCategory

@EqualsAndHashCode(includes='ejercicio,empleado')
class CalculoAnual {
    
	Integer ejercicio

	Empleado empleado

	@BindingFormat('dd/MM/yyyy')
	Date fechaInicial

	@BindingFormat('dd/MM/yyyy')
	Date fechaFinal

	//CalendarioDet calendarioDet

	NominaPorEmpleado nominaPorEmpleado
	
	Integer faltas=0
	
	Integer incapacidades=0

	Integer permisoEspecial=0

	Integer diasDelEjercicio=0

	Integer antiguedad=0

	BigDecimal salario=0.0
	
	BigDecimal resultado
	
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

	static transients = ['diasDelEjercicio','antiguedad']

    String toString(){
    	return "Calaculo andual $empleado - $ejercicio"
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

