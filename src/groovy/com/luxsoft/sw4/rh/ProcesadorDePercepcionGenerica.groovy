package com.luxsoft.sw4.rh

import org.apache.commons.logging.LogFactory

class ProcesadorDePercepcionGenerica {
	
	
	
	def concepto
	
	private static final log=LogFactory.getLog(this)
	
	def procesar(NominaPorEmpleado ne) {
		
		def calendarioDet=ne.nomina.calendarioDet
		def operaciones=OperacionGenerica.findAllByEmpleadoAndCalendarioDetAndTipo(ne.empleado,calendarioDet,'PERCEPCION')
		operaciones.each{ generica->
			concepto=generica.concepto
			
			log.info "Procesando percepcion generica para ${ne.empleado}"
			//Localizar el concepto
			def nominaPorEmpleadoDet=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
			nominaPorEmpleadoDet.importeGravado=generica.importeGravado
			nominaPorEmpleadoDet.importeExcento=generica.importeExcento
			ne.addToConceptos(nominaPorEmpleadoDet)
			
		}
		//def generica=OperacionGenerica.findByEmpleadoAndCalendarioDetAndTipo(ne.empleado,calendarioDet,'PERCEPCION')
		//println 'Operacion detectada: '+generica
		
	}
	
	def getModel(NominaPorEmpleadoDet det) {
		def ne=det.parent
		def model=[:]
		return model
	}
	
	def getTemplate() {
		return "/nominaPorEmpleado/conceptoInfo/percepcionGenerica"
	}
	
	String toString() {
		"Procesador de percepcion generica"
	}

}
