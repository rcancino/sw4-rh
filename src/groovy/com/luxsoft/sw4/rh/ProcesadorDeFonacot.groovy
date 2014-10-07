package com.luxsoft.sw4.rh

import java.math.RoundingMode;

import org.apache.commons.logging.LogFactory

import com.luxsoft.sw4.rh.imss.*

class ProcesadorDeFonacot {
	
	def conceptoClave='D014'
	
	def concepto
	
	private static final log=LogFactory.getLog(this)
	
	def procesar(NominaPorEmpleado ne) {
		
		if(!concepto) {
			concepto=ConceptoDeNomina.findByClave(conceptoClave)
			assert concepto,"Se debe de dar de alta el concepto de nomina: $conceptoClave"
		}
		
		def neDet=ne.conceptos.find(){
			it.concepto==concepto
		}
		
		//Buscando un prestamo vigente
		def fonacot=buscarPrestamo(ne)
		if(fonacot) {

					
			if(!neDet){
				neDet=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
				
			}
			def importeExcento=fonacot.retencionDiaria*(ne.diasTrabajados+ne.vacaciones)
			
			println "Aplicando decucccon para FONACOT: ${fonacot.empleado}  ${fonacot.retencionDiaria} Deduccion calculada de: ${importeExcento}"
			neDet.importeGravado=0
			neDet.importeExcento=importeExcento
			ne.actualizar()
			if(!neDet.id)
				ne.addToConceptos(neDet)

		}else{
			//Cleanup
			if(neDet){
				ne.removeFromConceptos(neDet)
			}
		}
		
		
	}
	
	private Fonacot buscarPrestamo(NominaPorEmpleado ne) {
		def fonacot=Fonacot.findAll("from Fonacot i where i.empleado=? and i.activo=true order by i.id desc"
			,[ne.empleado],[max:1])
		return fonacot?fonacot[0]:null
	}
	
	
	
	def getModel(NominaPorEmpleadoDet det) {
		def ne=det.parent
		def model=[:]
		return model
	}
	
	def getTemplate() {
		return "/nominaPorEmpleado/conceptoInfo/deduccionFonacot"
	}
	
	String toString() {
		"Procesador de credito FONACOT "
	}

}
