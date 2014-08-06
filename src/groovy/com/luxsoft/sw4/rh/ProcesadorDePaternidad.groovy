package com.luxsoft.sw4.rh

import org.apache.commons.logging.LogFactory

class ProcesadorDePaternidad {
	
	def conceptoClave='P032'
	
	def concepto
	
	private static final log=LogFactory.getLog(this)
	
	def procesar(NominaPorEmpleado ne) {
		
		if(ne.asistencia==null)
			return
			
		if(ne.asistencia.paternidad<=0)
			return
			
		if(!concepto) {
			concepto=ConceptoDeNomina.findByClave(conceptoClave)
		}
		log.info "Procesando paternidad para ${ne.empleado}"
		//Localizar el concepto
		def nominaPorEmpleadoDet=ne.conceptos.find(){ 
			it.concepto==concepto
		}
		
		if(!nominaPorEmpleadoDet){
			log.info 'NominaPorEmpleadoDet nueva no localizo alguna existente...'
			nominaPorEmpleadoDet=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
			ne.addToConceptos(nominaPorEmpleadoDet)
		}
		
		def empleado=ne.empleado
		def calendario=ne.nomina.calendarioDet
		assert calendario,"Debe existir el calendario vinculado a la nomina: ${ne}"
		
		//Buscamos los dias de paternidad
		def paternidad=ne.asistencia.paternidad
		
		def salarioDiario=empleado?.salario?.salarioDiario?:0
		def importeGravado=salarioDiario*paternidad
		nominaPorEmpleadoDet.importeGravado=importeGravado
		nominaPorEmpleadoDet.importeExcento=0
		
		
	}
	
	def getModel(NominaPorEmpleadoDet det) {
		def ne=det.parent
		def model=[:]
		model.salario=ne.salarioDiarioBase
		model.diasDelPeriodo=ne.nomina.getDiasPagados()
		model.paternidad=ne.asistencia.paternidad
		model.importeGravado=det.importeGravado
		return model
	}
	
	def getTemplate() {
		return "/nominaPorEmpleado/conceptoInfo/percepcionPaternidad"
	}
	
	String toString() {
		"Procesador de paternidad "
	}

}
