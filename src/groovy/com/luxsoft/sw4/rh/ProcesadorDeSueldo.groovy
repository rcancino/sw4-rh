package com.luxsoft.sw4.rh

import org.apache.commons.logging.LogFactory

class ProcesadorDeSueldo {
	
	def conceptoClave='P001'
	
	def concepto
	
	private static final log=LogFactory.getLog(this)
	
	def procesar(NominaPorEmpleado ne) {
		if(ne.asistencia==null)
			return
		
		if(!concepto) {
			concepto=ConceptoDeNomina.findByClave(conceptoClave)
		}
		log.info "Procesando sueldo para ${ne.empleado}"
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
		
		//Buscamos las faltas en el subsitema de control de asistencia
		def asistencia=ne.asistencia
		assert asistencia,"Debe existir el registro de asistencia para el calendario: ${calendario} empleado ${empleado}"
		
		
		def salarioDiario=empleado?.salario?.salarioDiario?:0
		
		//Actualizamos datos desde asistencia
		ne.salarioDiarioBase=salarioDiario
		ne.salarioDiarioIntegrado=empleado.salario.salarioDiarioIntegrado
		ne.diasDelPeriodo=ne.nomina.getDiasPagados()
		ne.faltas=asistencia.faltas+asistencia.incidencias
		/*
		if(asistencia.diasTrabajados>0){
			ne.faltas=0
		}
		*/
		ne.incapacidades=asistencia.incapacidades
		ne.vacaciones=asistencia.vacaciones
		ne.fraccionDescanso=(1/6*ne.faltas)
		
		//Calculo de dias trabajados y sueldo
		if(asistencia.diasTrabajados>0.0){
			ne.diasTrabajados=asistencia.diasTrabajados
		}else{
			ne.diasTrabajados=ne.diasDelPeriodo-ne.faltas-ne.fraccionDescanso-ne.vacaciones-ne.incapacidades-asistencia.paternidad
		}
		
		def sueldo=salarioDiario*ne.diasTrabajados
		if(sueldo>0){
			nominaPorEmpleadoDet.importeGravado=sueldo
			nominaPorEmpleadoDet.importeExcento=0
			
		}		
		
	}
	
	def getModel(NominaPorEmpleadoDet det) {
		def ne=det.parent
		def model=[:]
		model.salario=ne.salarioDiarioBase
		model.diasDelPeriodo=ne.nomina.getDiasPagados()
		model.faltas=ne.faltas
		model.incapacidades=ne.incapacidades
		model.vacaciones=ne.vacaciones
		model.fraccionDescanso=ne.fraccionDescanso
		model.diasTrabajados=ne.diasTrabajados
		model.sueldo=det.importeGravado
		return model
	}
	
	def getTemplate() {
		return "/nominaPorEmpleado/conceptoInfo/percepcionSalario"
	}
	
	String toString() {
		"Procesador de sueldos "
	}

}
