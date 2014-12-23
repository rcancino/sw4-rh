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
	
	BigDecimal resultado=0.0
	
	BigDecimal proyectado=0.0
	
	BigDecimal sueldo=0.0
	
	BigDecimal comisiones=0.0
	
	Integer vacaciones=0
	
	BigDecimal vacacionesPagadas=0.0
	
	BigDecimal primaVacacionalExenta=0.0
	
	BigDecimal primaVacacionalGravada=0.0
	
	BigDecimal incentivo=0.0
	
	BigDecimal aguinaldoExento=0.0
	
	BigDecimal aguinaldoGravable=0.0
	
	BigDecimal indemnizacionExenta=0.0
	
	BigDecimal indemnizacionGravada=0.0
	
	BigDecimal primaDeAntiguedadExenta=0.0
	
	BigDecimal primaDeAntiguedadGravada=0.0
	
	BigDecimal compensacion=0.0
	
	BigDecimal ptuExenta=0.0
	
	BigDecimal ptuGravada=0.0
	
	BigDecimal bonoPorDesempeno=0.0
	
	BigDecimal bonoDeProductividad=0.0
	
	BigDecimal devISPTAnt=0.0
	
	BigDecimal primaDominicalExenta=0.0
	
	BigDecimal primaDominicalGravada=0.0
	
	BigDecimal gratificacion=0.0
	
	BigDecimal permisoPorPaternidad=0.0
	
	BigDecimal tiempoExtraDobleExento=0.0
	
	BigDecimal tiempoExtraDobleGravado=0.0
	
	BigDecimal tiempoExtraTripleGravado=0.0
	
	BigDecimal devISPT=0.0
	
	BigDecimal totalGravado=0.0
	
	BigDecimal totalExento=0.0
	
	BigDecimal total=0.0
	
	BigDecimal ingresoTotal=0.0
	
	BigDecimal retardos=0.0
	
	BigDecimal SubsEmpPagado=0.0
	
	BigDecimal SubsEmpAplicado=0.0
	
	BigDecimal ISR=0.0
	
	Boolean calculoAnual=true
	
	BigDecimal compensacionSAF=0.0
	
	BigDecimal impuestoDelEjercicio=0.0  
	
		
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

