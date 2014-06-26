package com.luxsoft.sw4.rh

import java.math.RoundingMode;

import org.apache.commons.logging.LogFactory

import com.luxsoft.sw4.rh.imss.*

class ProcesadorDeIncentivo {
	
	def conceptoClave='P010'
	
	def concepto
	
	private static final log=LogFactory.getLog(this)
	
	def procesar(NominaPorEmpleado nominaEmpleado) {
		/*
		if(!concepto) {
			concepto=ConceptoDeNomina.findByClave(conceptoClave)
		}
		assert concepto,"Se debe de dar de alta el concepto de nomina: $conceptoClave"
		log.debug "Procesando Incentivo para ${nominaEmpleado.empleado}"
		
		//Localizar el concepto
		def nominaPorEmpleadoDet=nominaEmpleado.conceptos.find(){ 
			it.concepto==concepto
		}
		
		if(!nominaPorEmpleadoDet){
			nominaPorEmpleadoDet=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
			nominaEmpleado.addToConceptos(nominaPorEmpleadoDet)
		}
		
		nominaPorEmpleadoDet.importeGravado=50
		nominaPorEmpleadoDet.importeExcento=0
		nominaEmpleado.actualizar()
		*/
	}
	
	def getModel(NominaPorEmpleadoDet det) {
		def nominaEmpleado=det.parent
		def model=[:]
		return model
	}
	
	def getTemplate() {
		return "/nominaPorEmpleado/conceptoInfo/percepcionIncentivo"
	}
	
	String toString() {
		"Procesador de ISTP "
	}

}
