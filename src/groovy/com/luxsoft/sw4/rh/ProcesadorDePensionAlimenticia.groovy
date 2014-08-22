package com.luxsoft.sw4.rh

import java.math.RoundingMode;

import org.apache.commons.logging.LogFactory

import com.luxsoft.sw4.rh.imss.*

class ProcesadorDePensionAlimenticia {
	
	def conceptoClave='D007'
	
	def concepto
	
	private static final log=LogFactory.getLog(this)
	
	def procesar(NominaPorEmpleado ne) {
		
		if(!concepto) {
			concepto=ConceptoDeNomina.findByClave(conceptoClave)
			assert concepto,"Se debe de dar de alta el concepto de nomina: $conceptoClave"
		}
		
		//Buscando un prestamo vigente
		def pension=buscarPension(ne)
		if(pension) {
			
			log.info "Aplicando decucccon para pension alimenticia vigente: ${pension}"
			
			def percepciones=ne.getPercepciones()
			
			def deducciones=ne.conceptos.sum 0.0,{deduccion->
				
				def found=['D001','D002','D012'].find{it==deduccion.concepto.clave }
				return found?deduccion.importeExcento:0.0
				
			}
			
			def importeExcento=0.0
			
			if(!pension.neto){
				importeExcento=percepciones*(pension.porcentaje/100)
			}else{
				importeExcento=(percepciones-deducciones)*(pension.porcentaje/100)
			}
			
			def neDet=ne.conceptos.find(){
				it.concepto==concepto
			}
			
			if(!neDet){
				neDet=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
				ne.addToConceptos(neDet)
			}
			
			log.info "Deduccion calculada de: ${importeExcento}"
			neDet.importeGravado=0
			neDet.importeExcento=importeExcento.setScale(2,RoundingMode.HALF_EVEN)
			ne.actualizar()
		}
		
		
	}
	
	private PensionAlimenticia buscarPension(NominaPorEmpleado ne) {
		def pensiones=PensionAlimenticia.findAll("from PensionAlimenticia p where p.empleado=?"
			,[ne.empleado],[max:1])
		return pensiones?pensiones[0]:null
	}
	
	
	def getModel(NominaPorEmpleadoDet det) {
		def ne=det.parent
		def pension=buscarPension(ne)
		def model=[pension:pension
			,percepcion:getPercepciones(ne)
			]
		return model
	}
	
	def getTemplate() {
		return "/nominaPorEmpleado/conceptoInfo/deduccionPensionAlimenticia"
	}
	
	String toString() {
		"Procesador de Pension alimenticia"
	}

}
