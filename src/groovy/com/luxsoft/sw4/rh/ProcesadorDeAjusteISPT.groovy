package com.luxsoft.sw4.rh

import java.math.RoundingMode;

import org.apache.commons.logging.LogFactory

import com.luxsoft.sw4.rh.imss.*
import com.luxsoft.sw4.rh.acu.*

class ProcesadorDeAjusteISPT {
	
	
	
	private static final log=LogFactory.getLog(this)
	
	def procesar(NominaPorEmpleado nominaEmpleado) {
		
		log.info "Procesando Ajuste meunsual ISTP para ${nominaEmpleado.empleado}"

		def found=IsptMensual.findByNominaPorEmpleado(det.parent)
		if(! found) return

		def resultadoImpuesto=found.resultadoImpuesto
		def resultadoSubsidio=found.resultadoSubsidio

		
	}
	
	def procesarAjusteMensualISPT(NominaPorEmpleadoDet det){
		
		if(found){
			
			NominaPorEmpleado ne=det.parent
			def ajuste=found.resultadoImpuesto
			if(ajuste>0.0){
				det.importeExcento=ajuste
			}else if(ajuste<0.0){
				def concepto=ConceptoDeNomina.findByClave('P019')
				det.concepto=concepto
				det.importeExcento=ajuste.abs()
			}else{
				ne.removeFromConceptos(det)
			}
			
			if(found.resultadoSubsidio!=0.0){
				def subc= null
				if(found.resultadoSubsidio<0.0){
					subc=ConceptoDeNomina.findByClave('P021')
				}else{
					subc=ConceptoDeNomina.findByClave('D013')
				}
				def nominaPorEmpleadoDet=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
				nominaPorEmpleadoDet.concepto=subc
				nominaPorEmpleadoDet.importeGravado=0.0
				nominaPorEmpleadoDet.importeExcento=found.resultadoSubsidio.abs()
				ne.addToConceptos(nominaPorEmpleadoDet)
			}
		}
		
	}
	
	def  procesarAjusteMensualSubsidio(NominaPorEmpleadoDet det){
		
		def found=IsptMensual.findByNominaPorEmpleado(det.parent)
		if(found){
			//def ajuste=det.importeExcento-found.resultadoSubsidio
			def ajuste=found.resultadoSubsidio
			if(ajuste){
				det.importeExcento=ajuste
			}
		}
	}
	
	def procesarAjusteSubsidioAplicado(NominaPorEmpleado ne){
		def found=IsptMensual.findByNominaPorEmpleado(ne)
		if(found){
			
			def ajuste=ne.subsidioEmpleoAplicado+found.resultadoSubsidioAplicado
			println 'Ajustando subsidio aplicado en nomina empleado: '+ne + '  Subsidio aplicado nuevo: '+ajuste
			ne.subsidioEmpleoAplicado=ajuste
		}
	}
	
	def getModel(NominaPorEmpleadoDet det) {
		def nominaEmpleado=det.parent
		def model=[:]
		return model
	}
	
	def getTemplate() {
		return "/nominaPorEmpleado/conceptoInfo/deduccionISTP"
	}
	
	String toString() {
		"Procesador de ajuste mensual ISTP "
	}

}
