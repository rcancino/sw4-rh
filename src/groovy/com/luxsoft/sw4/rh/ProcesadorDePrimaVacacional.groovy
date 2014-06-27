package com.luxsoft.sw4.rh

import org.apache.commons.logging.LogFactory

import com.luxsoft.sw4.rh.tablas.ZonaEconomica



class ProcesadorDePrimaVacacional {
	
	def conceptoClave='P024'
	
	def concepto
	
	private static final log=LogFactory.getLog(this)
	
	def procesar(NominaPorEmpleado ne) {
		
		if(!concepto) {
			concepto=ConceptoDeNomina.findByClave(conceptoClave)
		}
		log.info "Procesando prima vacacional para ${ne.empleado}"
		
		//Localizar el concepto
		def nominaPorEmpleadoDet=ne.conceptos.find(){
			it.concepto==concepto
		}
		
		if(!nominaPorEmpleadoDet){
			nominaPorEmpleadoDet=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
			ne.addToConceptos(nominaPorEmpleadoDet)
		}
		def asistencia=ne.asistencia
		def vacaciones=asistencia.vacaciones+asistencia.vacacionesp
		
		if(vacaciones){
			
			def empleado=ne.empleado
			
			def salarioDiario=ne.salarioDiarioBase
			
			def sm=ZonaEconomica.findByClave('A').salario
			def diasSalarioMinimo=15
			def topeSalarial=sm*diasSalarioMinimo
			
			def importeDeVacaciones=vacaciones*salarioDiario
			
			//Posteriormente la tasa se debe sacar de una tabla (Contemplar la antiguedad del empleado)
			def tasa=0.25
			def prima=importeDeVacaciones*tasa
			
			def dif=prima-topeSalarial
			def gravado=dif>0?dif:0.0
			def excento=dif<0?prima:topeSalarial
			
			nominaPorEmpleadoDet.importeGravado=gravado
			nominaPorEmpleadoDet.importeExcento=excento
			ne.actualizar()
		}
		
		
	}
	
	def getModel(NominaPorEmpleadoDet det) {
		println 'ModelView '+det
		def ne=det.parent
		def asistencia=ne.asistencia
		def model=[:]
		def vacaciones=asistencia.vacaciones+asistencia.vacacionesp
		
		if(vacaciones){
			
			def empleado=ne.empleado
			
			def salarioDiario=ne.salarioDiarioBase
			
			def sm=ZonaEconomica.findByClave('A').salario
			def diasSalarioMinimo=15
			def topeSalarial=sm*diasSalarioMinimo
			
			def importeDeVacaciones=vacaciones*salarioDiario
			
			//Posteriormente la tasa se debe sacar de una tabla (Contemplar la antiguedad del empleado)
			def tasa=0.25
			def prima=importeDeVacaciones*tasa
			
			def dif=prima-topeSalarial
			def gravado=dif>0?dif:0.0
			def excento=dif<0?prima:topeSalarial
			
			model.salarioDiario=salarioDiario
			model.sm=sm
			model.dias=diasSalarioMinimo
			model.topeSalarial=topeSalarial
			model.importeDeVacaciones=importeDeVacaciones
			model.tasa=tasa
			model.prima=prima
			model.gravado=gravado
			model.excento=excento
			
		}
		return model
	}
	
	def getTemplate() {
		return "/nominaPorEmpleado/conceptoInfo/percepcionPrimaVacacional"
	}
	
	String toString() {
		"Procesador de prima vacacional "
	}

}
