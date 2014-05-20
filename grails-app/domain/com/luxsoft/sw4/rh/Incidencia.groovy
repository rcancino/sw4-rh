package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Periodo

import groovy.time.TimeCategory


class Incidencia {

	
	Empleado empleado
	
	Periodo periodo
	
	List partidas
	
	String comentario
	
	int diasDescanso

	Date dateCreated
	
	Date lastUpdated

    static constraints = {
    	comentario nullable:true
    	
    }

    static hasMany = [partidas: IncidenciaDet]

    static embedded = ['periodo']

    String toString(){
    	return "$empleado  $total"
    }
	
	private void calcularDescuento(BigDecimal horas) {
		
	}

	public Integer getDias(){
		use(TimeCategory){
			def duration= periodo.fechaFinal-periodo.fechaInicial+1.day
			return duration.days
		}
	}

    Incidencia actualizarImportes(){
		def salario=this.salarioDiario
		def acumulado=0.0
		def dias=periodo.dias()
		partidas?.each{ it ->

			it.actualizar(salario,dias)
			.actualizarImporteProporcional(salario,dias,diasDescanso)
			it.total=it.importe+it.importeProporcional
			acumulado+=it.total
		}
		this.total=acumulado
        this
    }

    
}
