package com.luxsoft.sw4.rh

import org.apache.commons.logging.LogFactory

class ProcesadorDePercepcionGenerica {
	
	def conceptoClave='P032'
	
	def concepto
	
	private static final log=LogFactory.getLog(this)
	
	def procesar(NominaPorEmpleado ne) {
		
		def calendarioDet=ne.nomina.calendarioDet
		def generica=OperacionGenerica.findByCalendarioDetAndTipo(calendarioDet,'PERCEPCION')
		if(generica){
			if(!concepto) {
				concepto=ConceptoDeNomina.findByClave(conceptoClave)
			}
			log.info "Procesando percepcion generica para ${ne.empleado}"
			//Localizar el concepto
			def nominaPorEmpleadoDet=ne.conceptos.find(){ 
				it.concepto==concepto
			}
			
			if(!nominaPorEmpleadoDet){
				log.info 'NominaPorEmpleadoDet nueva no localizo alguna existente...'
				nominaPorEmpleadoDet=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
				ne.addToConceptos(nominaPorEmpleadoDet)
			}
			nominaPorEmpleadoDet.importeGravado=generica.importeExcento
			nominaPorEmpleadoDet.importeExcento=generica.importeGravado
		}
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
