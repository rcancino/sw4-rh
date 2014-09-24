package com.luxsoft.sw4.rh

import java.math.RoundingMode

import org.apache.commons.logging.LogFactory


class ProcesadorDePagoDeComisiones {
	
	def conceptoClave='P029'
	
	def concepto
	
	private static final log=LogFactory.getLog(this)
	
	def procesar(NominaPorEmpleado ne) {
		
		
		if(!concepto) {
			concepto=ConceptoDeNomina.findByClave(conceptoClave)
			assert concepto,"Se debe de dar de alta el concepto de nomina: $conceptoClave"
		}
		
		def comisiones=buscarComisiones(ne)
		
		if(comisiones && comisiones.importe){
			//Localizar el concepto
			def neDet=ne.conceptos.find(){
				it.concepto==concepto
			}
			if(!neDet){
				neDet=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
				ne.addToConceptos(neDet)
			}
			log.info "Pago de comisiones poe: ${comisiones.importe}"
			neDet.importeGravado=comisiones.importe
			neDet.importeExcento=0.0
			ne.actualizar()
		}else{
			//Clean up en caso de existir previaente
			def neDet=ne.conceptos.find(){
				it.concepto==concepto
			}
			if(neDet){
				ne.removeFromConceptos(neDet)
			}
		}
		
	}
	
	private RegistroDeComisiones buscarComisiones(NominaPorEmpleado ne) {
		def prestamos=RegistroDeComisiones.findAll("from RegistroDeComisiones o where o.empleado=? and o.calendarioDet=?"
			,[ne.empleado,ne.nomina.calendarioDet],[max:1])
		return prestamos?prestamos[0]:null
	}
	
	def getModel(NominaPorEmpleadoDet det) {
		def ne=det.parent
		def model=[:]
		return model
	}
	
	def getTemplate() {
		return "/nominaPorEmpleado/conceptoInfo/percepcionPagoDeComisiones"
	}
	
	String toString() {
		"Procesador de Pago de comisiones "
	}
	
}
