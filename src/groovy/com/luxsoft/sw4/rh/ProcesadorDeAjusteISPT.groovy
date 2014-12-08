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
			impuesto.importeGravado=0.0
			impuesto.importeExcento=0.0
			
		}
		def impuesto=ne.conceptos.find{it.concepto.clave=='D002'}
		if(impuesto){
			//println 'Eliminando impuesto*************************** '+impuesto
			impuesto.importeGravado=0.0
			impuesto.importeExcento=0.0
			ne.save flush:true
			
		}
		
		if(ajuste.resultadoImpuesto<0.0){
			def concepto=ConceptoDeNomina.findByClave('P019')
			def devo=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
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
		if(ajuste.resultadoSubsidio<0.0){
			if(!subsidio){
				def concepto=ConceptoDeNomina.findByClave('P021')
				subsidio=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
				ne.addToConceptos(subsidio)
			}
			subsidio.importeGravado=0.0
			subsidio.importeExcento=ajuste.resultadoSubsidio.abs()
		}
		
		
		
		
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
