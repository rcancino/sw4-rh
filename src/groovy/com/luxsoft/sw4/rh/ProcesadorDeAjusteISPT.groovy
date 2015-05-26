package com.luxsoft.sw4.rh

import java.math.RoundingMode;

import org.apache.commons.logging.LogFactory

import com.luxsoft.sw4.rh.imss.*
import com.luxsoft.sw4.rh.acu.*

class ProcesadorDeAjusteISPT {
	
	
	
	private static final log=LogFactory.getLog(this)
	
	def procesar(NominaPorEmpleado ne) {
		
		log.info "Procesando Ajuste meunsual ISTP para ${ne.empleado}"

		def ajuste=IsptMensual.findByNominaPorEmpleado(ne)
		if(! ajuste) return
		
		ne.subsidioEmpleoAplicado=ajuste.resultadoSubsidioAplicado
		
		def subsidio=ne.conceptos.find{it.concepto.clave=='P021'}
		if(subsidio){
			subsidio.importeGravado=0.0
			subsidio.importeExcento=0.0
			
		}
		def impuesto=ne.conceptos.find{it.concepto.clave=='D002'}
		if(impuesto){
			impuesto.importeGravado=0.0
			impuesto.importeExcento=0.0
			ne.save flush:true
			
		}
		
		/****** Evaluacion del impuesto segun resultado mensual*******/
		def devo
		if(ajuste.resultadoImpuesto<0.0){
			def concepto=ConceptoDeNomina.findByClave('P019')
			devo=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
			devo.importeGravado=0.0
			devo.importeExcento=ajuste.resultadoImpuesto.abs()
			ne.addToConceptos(devo)
		}else{
			if(!impuesto){
				def concepto=ConceptoDeNomina.findByClave('D002')
				impuesto=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
				ne.addToConceptos(impuesto)
			}
			impuesto.importeGravado=0.0
			impuesto.importeExcento=ajuste.resultadoImpuesto.abs()
		}

		/****** Evaluacion del subsidiio segun resultado mensual *******/

		if(ajuste.resultadoSubsidio<0.0){
			
			if(!subsidio){
				//println 'Agregando registro P021 para el subsidio de: '+ajuste.resultadoSubsidio
				def concepto=ConceptoDeNomina.findByClave('P021')
				subsidio=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
				ne.addToConceptos(subsidio)
			}
			subsidio.importeGravado=0.0
			subsidio.importeExcento=ajuste.resultadoSubsidio.abs()
			
		}else if(ajuste.resultadoSubsidio>0.0){
			// El subsidio mensual fue menor que el subsidio acumulado por lo tantopor lo tanto se debe retener impuesto
			// en el concepto D002
			if(!impuesto){
				def concepto
				if(ajuste.resultadoSubsidio.abs()-ajuste.impuestoAcumulado>0.0){
					concepto=ConceptoDeNomina.findByClave('D002')
				}else if(ajuste.resultadoSubsidio.abs()-ajuste.impuestoAcumulado<0.0){
					concepto=ConceptoDeNomina.findByClave('P021')
				}
				impuesto=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
				ne.addToConceptos(impuesto)
			}
			impuesto.importeGravado=0.0
			impuesto.importeExcento=(ajuste.resultadoSubsidio.abs()-ajuste.impuestoAcumulado).abs()

			if(devo){
				ne.removeFromConceptos(devo);
			}
		}
		
		ne.actualizar()
		
	}
	
	
	
	def getModel(NominaPorEmpleadoDet det) {
		def nominaEmpleado=det.parent
		def model=[:]
		return model
	}
	
	def getTemplate() {
		return "/nominaPorEmpleado/conceptoInfo/ajusteMensualISTP"
	}
	
	String toString() {
		"Procesador de ajuste mensual ISTP "
	}

}
