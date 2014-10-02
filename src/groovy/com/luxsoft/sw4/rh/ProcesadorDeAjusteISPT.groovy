package com.luxsoft.sw4.rh

import java.math.RoundingMode;

import org.apache.commons.logging.LogFactory

import com.luxsoft.sw4.rh.imss.*
import com.luxsoft.sw4.rh.acu.*

class ProcesadorDeAjusteISPT {
	
	
	
	private static final log=LogFactory.getLog(this)
	
	def procesar(NominaPorEmpleado ne) {
		
		def ispt=IsptMensual.findByNominaPorEmpleado(ne)
		
		if(ispt){
			log.info 'Generando ajuste mensual ISPT'
			clean(ne)
			ne.actualizar()
		}
		
		
	}
	
	def clean(NominaPorEmpleado ne){
		['D002','P021'].each{
			def found=ne.conceptos.find{it.concepto.clave==it}
			if(found){
				println 'Eliminando basura...'+found
				ne.removeFromConceptos(found)
			}
		}
		
	}
	
	def procesarAjusteMensualISPT(NominaPorEmpleadoDet det){
		def found=IsptMensual.findByNominaPorEmpleado(det.parent)
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
		
		model.percepciones=nominaEmpleado.getPercepcionesGravadas()
		if(model.percepciones<=0){
			return model
		}
		model.diasTrabajados=nominaEmpleado.diasDelPeriodo
		
		model.tarifa =TarifaIsr.obtenerTabla(model.diasTrabajados).find(){(model.percepciones>it.limiteInferior && model.percepciones<=it.limiteSuperior)}
		model.subsidio=Subsidio.obtenerTabla(model.diasTrabajados).find(){(model.percepciones>it.desde && model.percepciones<=it.hasta)}
		
		model.baseGravable=model.percepciones-model.tarifa.limiteInferior
		model.tarifaPorcentaje=model.tarifa.porcentaje/100
		model.importeGravado=model.baseGravable*model.tarifaPorcentaje
		model.cuotaFija=model.tarifa.cuotaFija
		
		model.istp=(model.importeGravado+model.cuotaFija).setScale(2,RoundingMode.HALF_EVEN)
		model.importeSubsidio=model.istp-model.subsidio.subsidio
		model.importeExcento=model.importeSubsidio<0?model.subsidio.abs():0.0
		model.importeISTP=model.importeSubsidio<0?0.0:model.istp
		
		
		/*
		importeGravado*=tarifa.porcentaje
		importeGravado/=100
		importeGravado+=tarifa.cuotaFija
		importeGravado=importeGravado.setScale(2,RoundingMode.HALF_EVEN)
		
		def sub=importeGravado-subsidio.subsidio
		nominaEmpleado.subsidioEmpleoAplicado=subsidio.subsidio
		
		if(sub<0){
			 sub=sub.abs()
			 nominaPorEmpleadoDet.concepto=ConceptoDeNomina.findByClave('P021')
			 nominaPorEmpleadoDet.importeGravado=0.0
			 nominaPorEmpleadoDet.importeExcento=sub
		}else{
			 nominaPorEmpleadoDet.importeGravado=importeGravado
			 nominaPorEmpleadoDet.importeExcento=0.0
		}
		*/
		return model
	}
	
	def getTemplate() {
		return "/nominaPorEmpleado/conceptoInfo/deduccionISTP"
	}
	
	String toString() {
		"Procesador de ISTP "
	}

}
