package com.luxsoft.sw4.rh

import java.math.RoundingMode;

import org.apache.commons.logging.LogFactory

import com.luxsoft.sw4.rh.imss.*

class ProcesadorDeISTP {
	
	def conceptoClave='D002'
	
	def concepto
	
	private static final log=LogFactory.getLog(this)
	
	def procesar(NominaPorEmpleado nominaEmpleado) {
		
		if(!concepto) {
			concepto=ConceptoDeNomina.findByClave(conceptoClave)
		}
		log.debug "Procesando ISTP para ${nominaEmpleado.empleado}"
		
		//Localizar el concepto
		def nominaPorEmpleadoDet=nominaEmpleado.conceptos.find(){ 
			it.concepto==concepto
		}
		
		if(!nominaPorEmpleadoDet){
			nominaPorEmpleadoDet=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
			nominaEmpleado.addToConceptos(nominaPorEmpleadoDet)
		}
		
		
		def percepciones=nominaEmpleado.getPercepcionesGravadas()
		if(percepciones<=0)
			return
			
		def diasTrabajados=nominaEmpleado.diasTrabajados
		
		if(diasTrabajados<=0)
			return
		
		def tarifa =TarifaIsr.obtenerTabla(diasTrabajados).find(){(percepciones>it.limiteInferior && percepciones<=it.limiteSuperior)}
		assert tarifa,"No encontro TarifaIsr para los parametros: Dias: ${diasTrabajados} Perc:${percepciones} Empleado: ${nominaEmpleado.empleado}"
		def subsidio=Subsidio.obtenerTabla(diasTrabajados).find(){(percepciones>it.desde && percepciones<=it.hasta)}
		
		def importeGravado=percepciones-tarifa.limiteInferior
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
		nominaEmpleado.actualizar()
		
		
		
	}
	
	def getModel(NominaPorEmpleadoDet det) {
		def nominaEmpleado=det.parent
		def model=[:]
		
		model.percepciones=nominaEmpleado.getPercepcionesGravadas()
		if(model.percepciones<=0){
			return model
		}
		model.diasTrabajados=nominaEmpleado.diasTrabajados
		
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