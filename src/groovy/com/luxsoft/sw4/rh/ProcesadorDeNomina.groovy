package com.luxsoft.sw4.rh

import org.apache.commons.lang.exception.ExceptionUtils

class ProcesadorDeNomina {
	
	def reglas=[]
	
	def procesar(Nomina nomina) {
		nomina.partidas.each{
			procesar(it)
		}
		return nomina
	}
	
	def procesar(NominaPorEmpleado ne) {
		
		reglas.each{ p ->
			try {
				if(ne.asistencia){
					p.procesar(ne)
				}
			}catch(Exception ex) {
				ex.printStackTrace()
				String msg="Error en regla: ${p} Causa: "+ExceptionUtils.getRootCauseMessage(ex)
				throw new ProcesadorDeNominaException(message:msg,nominaPorEmpleado:ne)
			}
			
		}
		
		return ne
	}
	
	

}

class ProcesadorDeNominaException extends RuntimeException{
	
	String message
	NominaPorEmpleado nominaPorEmpleado
	
}
