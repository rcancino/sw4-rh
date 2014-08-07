package com.luxsoft.sw4.rh

class ControlDeVacaciones {

	Empleado empleado
	
	Long ejercicio

	BigDecimal acumuladoExcento

	BigDecimal acumuladoGravado
	
	int antiguedadDias
	
	int antiguedadYears

	int diasVacaciones

	int diasTomados

	int diasDisponibles

	Date dateCreated

	Date lastUpdated


    static constraints = {
		empleado unique:['ejercicio']
    }

    static transients = ['diasDisponibles']

    int getDiasDisponibles(){
    	return diasVacaciones-diasTomados
    }

    String toString(){
    	"$ejercicio $empleado $diasDisponibles"
    }
	
	static mapping = {
		
	}
}
