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

	NominaPorEmpleado nominaPorEmpleado

	Integer diasDelEjercicio=0
	Integer diasDeAguinaldo=15
	Integer diasDeBono=30
	Integer faltas=0
	Integer incapacidades=0
	Integer permisoEspecial=0

	Integer diasParaAguinaldo=0
	Integer diasParaBono=0
	Integer antiguedad=0

	BigDecimal salario=0.0
	BigDecimal aguinaldo=0.0
	
	BigDecimal bonoPreliminar=0.0
	BigDecimal porcentajeBono=1.00
	BigDecimal bono=0.0 // (30/365)*diasParaBono*salario  Es la parte proporcianal de 30 dias de salario 
	
	
	BigDecimal aguinaldoGravado=0.0
	BigDecimal aguinaldoExcento=0.0
	
	BigDecimal totalGravable=0.0

	//Datos para la retencion
	BigDecimal promedioGravable=0.0 // (Aguinaldo gravado + bono)*30.4
	BigDecimal sueldoMensual=0.0 // salario*31 en semana y salario*32 en quincena
	BigDecimal proporcionPromedioMensual=0.0
	BigDecimal isrMensual=0.0  // datos de tablas
	BigDecimal isrPromedio=0.0 // dato de tabla
	BigDecimal difIsrMensualPromedio=0.0 //isrPromedio-isrMensual
	BigDecimal tasa=0.0 //si difIsrMensualPrmedio<=0?0:(difIsrMensualPromedio/promedioGravable)
	BigDecimal isrPorRetener=0.0 //tasa*(aguinaldoGravado+bono)
	
	BigDecimal subsidio=0.0
	BigDecimal isrOSubsidio=0.0

	BigDecimal resultadoIsrSubsidio=0.0
	
	BigDecimal tablaNormalIsrSub=0.0 
	BigDecimal beneficioPerjuicio=0.0
	
	BigDecimal isrEjerAnt=0.0
	BigDecimal subTotal=0.0
	BigDecimal pensionA=0.0
	BigDecimal otrasDed=0.0
	BigDecimal prestamo=0.0
	BigDecimal netoPagado=0.0

	Date dateCreated
	Date lastUpdated


    static constraints = {
		empleado unique:['ejercicio']
		nominaPorEmpleado nullable:true
		tasa scale:4
    }

    static mapping = {
		fechaInicial type:'date'
		fechaFinal type:'date'
	}

	static transients = ['diasDelEjercicio','antiguedad']

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
