package com.luxsoft.sw4.rh

import java.math.RoundingMode;

import org.apache.commons.logging.LogFactory

import com.luxsoft.sw4.rh.imss.*

class ProcesadorRetardoPermiso {
	
	def conceptoClave='D012'
	
	def concepto
	
	private static final log=LogFactory.getLog(this)
	
	def procesar(NominaPorEmpleado ne) {
		
		if(!concepto) {
			concepto=ConceptoDeNomina.findByClave(conceptoClave)
			assert concepto,"Se debe de dar de alta el concepto de nomina: $conceptoClave"
		}
		
		//Buscando un prestamo vigente
		def asistencia=ne.asistencia
		def minutos=asistencia.minutosNoLaborados+asistencia.retardoMayor+asistencia.retardoComida+asistencia.minutosPorDescontar
		
		if(minutos>0) {
			
			def neDet=ne.conceptos.find(){
				it.concepto==concepto
			}
			if(!neDet){
				neDet=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
				ne.addToConceptos(neDet)
			}
			
			def salarioMinuto=ne.salarioDiarioBase/8.0/60
			def deduccion=minutos*salarioMinuto
			
			log.info "Aplicando decucccon PermisoRetardo: ${deduccion}"
			neDet.importeGravado=0
			neDet.importeExcento=deduccion
			ne.actualizar()
		}
	}
	
	
	def getModel(NominaPorEmpleadoDet det) {
		def model=[:]
		def ne=det.parent
		def asistencia=ne.asistencia
		def salarioMinuto=ne.salarioDiarioBase/8.0/60
		
		def minutos=asistencia.minutosNoLaborados+asistencia.retardoMayor+asistencia.retardoComida+asistencia.minutosPorDescontar
		def deduccion=minutos*salarioMinuto
		model.asistencia=asistencia
		model.deduccion=deduccion
		model.salarioMinuto=salarioMinuto
		model.salarioDiario=ne.salarioDiarioBase
		model.minutos=minutos
		model.salarioMinuto=salarioMinuto
		return model
	}
	
	def getTemplate() {
		return "/nominaPorEmpleado/conceptoInfo/deduccionRetardoPermiso"
	}
	
	String toString() {
		"Procesador de SALIDA PERMISO "
	}

}
