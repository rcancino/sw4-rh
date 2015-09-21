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
		
		def nominaPorEmpleadoDet=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
		
		
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
		if(ne.nomina.periodicidad=='QUINCENAL' && ne.nomina.folio==4){
			ne.diasDelPeriodo=15
		}
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
			if(ne.nomina.periodicidad=='QUINCENAL' && ne.nomina.folio==4){
				asistencia.diasTrabajados=15
			}
			ne.faltas=asistencia.faltasManuales
			
			ne.fraccionDescanso=(1/6*ne.faltas)
			ne.diasTrabajados=asistencia.diasTrabajados-ne.faltas-ne.fraccionDescanso-ne.incapacidades
		}else{
			ne.diasTrabajados=ne.diasDelPeriodo-ne.faltas-ne.fraccionDescanso-ne.vacaciones-ne.incapacidades-asistencia.paternidad
			
			if(ne.diasTrabajados==0 &&  asistencia.asistencias!=0 )
			   ne.diasTrabajados=asistencia.asistencias

			  def asi=(asistencia.periodo.dias()+1)-ne.incapacidades
			
			if(asi==0)
				ne.diasTrabajados=0


		}
		
		def sueldo=salarioDiario*ne.diasTrabajados
		if(sueldo>0.0){
			nominaPorEmpleadoDet.importeGravado=sueldo
			nominaPorEmpleadoDet.importeExcento=0
			ne.addToConceptos(nominaPorEmpleadoDet)
			
		}
		
		
	}
	
	def getModel(NominaPorEmpleadoDet det) {
		def ne=det.parent
		def model=[:]
		model.salario=ne.salarioDiarioBase
		model.diasDelPeriodo=ne.nomina.getDiasPagados()
		if(ne.nomina.periodicidad=='QUINCENAL' && ne.nomina.folio==4){
			model.diasDelPeriodo=15
		}
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
