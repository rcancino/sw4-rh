package com.luxsoft.sw4.rh

import org.apache.commons.lang.time.DateUtils;

class ControlDeVacaciones {

	Empleado empleado
	
	Date aniversario
	
	Long ejercicio

	BigDecimal acumuladoExcento

	BigDecimal acumuladoGravado
	
	int antiguedadDias
	
	int antiguedadYears

	int diasVacaciones
	
	int diasTrasladados=0

	int diasTomados
	
	int diasPagados

	int diasDisponibles

	Date dateCreated

	Date lastUpdated


    static constraints = {
		empleado unique:['ejercicio']
    }

    static transients = ['diasDisponibles','vigencia','acumuladoExcentoCalculado']

    int getDiasDisponibles(){
    	return diasVacaciones+diasTrasladados-getTotalTomados()
    }
	
	def getTotalTomados(){
		return diasTomados
	}

    String toString(){
    	"$ejercicio $empleado $diasDisponibles"
    }
	
	static mapping = {
		aniversario type:'date'
	}
	
	Date getVigencia(){
		if(aniversario)
			return DateUtils.addMonths(aniversario, 6)
		return null
	}
	
	
	def getAcumuladoExcentoCalculado(){
		def acumulado=NominaPorEmpleadoDet.executeQuery(
			"select sum(n.importeExcento) from NominaPorEmpleadoDet n where n.parent.empleado=? and n.concepto.clave='P024' ",[empleado])
		def res=acumulado.get(0)?:0.0
		return res
	} 
	
	
}
