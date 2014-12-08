package com.luxsoft.sw4.rh

import java.math.RoundingMode;

import org.apache.commons.logging.LogFactory

import com.luxsoft.sw4.rh.imss.*
import com.luxsoft.sw4.rh.acu.*

class ProcesadorDeISTP {
	
	def conceptoClave='D002'
	  
	def concepto
	
	private static final log=LogFactory.getLog(this)
	
	def procesar(NominaPorEmpleado nominaEmpleado) {
		
		if(!concepto) {
			concepto=ConceptoDeNomina.findByClave(conceptoClave)
		}
		println "******************************Procesando ISTP para ${nominaEmpleado.empleado}"
		log.info "Procesando ISTP para ${nominaEmpleado.empleado}"
		
		def percepciones=nominaEmpleado.getPercepcionesGravadas()
		if(percepciones<=0)
			return
			
		def diasTrabajados=nominaEmpleado.diasDelPeriodo
		
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
			
			log.info 'Subsidio: '+sub
			
			ConceptoDeNomina subc=ConceptoDeNomina.findByClave('P021')
			//buscar uno existente en el sistena
			def ne2=nominaEmpleado.conceptos.find(){ 
				it.concepto==subc
			}
			if(ne2){
				ne2.concepto=subc
				ne2.importeGravado=0.0
				ne2.importeExcento=sub.abs()
				procesarAjusteMensualSubsidio(ne2)
			}else{
				def nominaPorEmpleadoDet=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
				nominaPorEmpleadoDet.concepto=subc
				nominaPorEmpleadoDet.importeGravado=0.0
				nominaPorEmpleadoDet.importeExcento=sub.abs()
				nominaEmpleado.addToConceptos(nominaPorEmpleadoDet)
			} 
			
		}else{
		
			
			def nominaPorEmpleadoDet=nominaEmpleado.conceptos.find(){
				it.concepto==concepto
			}
			if(!nominaPorEmpleadoDet){
				nominaPorEmpleadoDet=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
				nominaEmpleado.addToConceptos(nominaPorEmpleadoDet)
			}
			log.info 'ISTP : '+importeGravado+ '- Subsidio: '+subsidio.subsidio
			nominaPorEmpleadoDet.importeExcento=importeGravado-subsidio.subsidio
			nominaPorEmpleadoDet.importeGravado=0.0
			
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
		
		return model
	}
	
	def getTemplate() {
		return "/nominaPorEmpleado/conceptoInfo/deduccionISTP"
	}
	
	/*String toString() {
		"Procesador de ISTP "
	}*/

}
