package com.luxsoft.sw4.rh

class ControlDeVacaciones {

	Empleado empleado
	
	Long ejercicio

	BigDecimal acumuladoExcenta

	BigDecimal acumuladaGravada

	int diasCorrespondientes

	int diasTomados

	int diasDisponibles

	Date dateCreated

	Date lastUpdated


    static constraints = {

    }

    static transients = ['diasDisponibles']

    int getDiasDisponibles(){
    	return diasCorrespondientes-diasTomados
    }

    String toString(){
    	"$ejercicio $empleado $diasDisponibles"
    }
}
