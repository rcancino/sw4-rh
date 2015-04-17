package com.luxsoft.sw4.rh

import org.apache.commons.logging.LogFactory

import com.luxsoft.sw4.rh.acu.AcumuladoPorConcepto;
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
		def nominaPorEmpleadoDet=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
		
		
		def asistencia=ne.asistencia
		def vacaciones=asistencia.vacaciones+asistencia.vacacionesp
		def calendarioDet=asistencia.calendarioDet
		log.debug "El empleado ${ne.empleado.clave} tiene ${vacaciones} en el periodo:${calendarioDet.asistencia}"
		
		if(vacaciones>0){
			
			def empleado=ne.empleado
			def ejercicio=ne.nomina.calendarioDet.calendario.ejercicio
			def control=ControlDeVacaciones.findByEjercicioAndEmpleado(ejercicio,empleado)
			//def acumulado=AcumuladoPorConcepto.find{empleado==empleado && concepto==concepto && ejercicio==calendarioDet.calendario.ejercicio}
			assert control,'Debe existir el control de vacaciones para: '+empleado+' Para el ejercicio: '+ejercicio
			//def acumulado=control.acumuladoExcentoCalculado
			def acumulado=control.acumuladoExcento
			
			
			def salarioDiario=ne.salarioDiarioBase
			if(ne.empleado.salario.salarioVariable>0){
				salarioDiario=ne.empleado.salario.salarioVariable
			}
			
			def sm=ZonaEconomica.findByClave('A').salario
			def diasSalarioMinimo=15
			def topeSalarial=sm*diasSalarioMinimo
			
			def importeDeVacaciones=vacaciones*salarioDiario
			
			//Posteriormente la tasa se debe sacar de una tabla (Contemplar la antiguedad del empleado)
			def tasa=0.25
			
			def prima=importeDeVacaciones*tasa
			//def acuExcento=acumulado.acumuladoExcento
			def disponibleExcento=(topeSalarial-acumulado)
			
			
			def gravado=disponibleExcento>prima?0.0:prima-disponibleExcento
			def excento=disponibleExcento<prima?disponibleExcento:prima
			
			log.info "Acumulabe  excento:${acumulado} disponible excento:${disponibleExcento}"
			log.info "Prima: ${prima} Excento:${excento} Gravado:${gravado}"
			
			nominaPorEmpleadoDet.importeGravado=gravado
			nominaPorEmpleadoDet.importeExcento=excento
			ne.addToConceptos(nominaPorEmpleadoDet)
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
			def calendarioDet=asistencia.calendarioDet
			def acumulado=AcumuladoPorConcepto.find{empleado==empleado && concepto==concepto && ejercicio==calendarioDet.calendario.ejercicio}
			//def acumulado=vacaciones?.control?.acumuladoExcento
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
			
			model.acumuladoExcento=acumulado?.acumuladoExcento?:0
			model.salarioDiario=salarioDiario
			model.sm=sm
			model.dias=vacaciones
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
