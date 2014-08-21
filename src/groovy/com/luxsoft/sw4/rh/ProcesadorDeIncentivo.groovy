package com.luxsoft.sw4.rh

import java.math.RoundingMode;

import org.apache.commons.logging.LogFactory

import com.luxsoft.sw4.rh.imss.*

class ProcesadorDeIncentivo {
	
	def conceptoClave='P010'
	
	def concepto
	
	private static final log=LogFactory.getLog(this)
	
	def procesar(NominaPorEmpleado ne) {
		log.info "Procesando Incentivo para ${ne.empleado}"
		
		if(!concepto) {
			concepto=ConceptoDeNomina.findByClave(conceptoClave)
		}
		assert concepto,"Se debe de dar de alta el concepto de nomina: $conceptoClave"

		//Localizar el concepto
		def nominaPorEmpleadoDet=ne.conceptos.find(){ 
			it.concepto==concepto
		}
		
		
		def tipo=ne.empleado.perfil.tipoDeIncentivo
		if(tipo){
			def incentivo=Incentivo.findByTipoAndEmpleadoAndAsistencia(tipo,ne.empleado,ne.asistencia)
			
			if(incentivo){

				def importeGravado=0.0

				switch(tipo) {
					case 'SEMANAL':
						importeGravado=calcularImporteMensual(ne,incentivo)
						break
					case 'QUINCENAL':
						importeGravado=calcularImporteQuincenal(ne,incentivo)
						break
					case 'MENSUAL':
						break
				}
				
				if(importeGravado>0){
					nominaPorEmpleadoDet.importeGravado=importeGravado
					nominaPorEmpleadoDet.importeExcento=0
					ne.actualizar()
				}
			}
		}

		if(nominaEmpleadoDet){
			ne.removeFromConceptos(nominaEmpleadoDet)
			
		}
	}

	BigDecimal calcularImporteQuincenal(NominaPorEmpleado ne,Incentivo i){
		def importe=0.0
		def salario=nominaEmpleado.conceptos.find{it.concepto.clave=='P001'}
		def vac=nominaEmpleado.conceptos.find{it.concepto.clave=='P025'}
		if(salario ){
			def bono=(incentivo.tasaBono1+incentivo.tasaBono2)
			def vacImp=vac?vac.getTotal():0.0
			def total=+salario.total+vacImp
			log.debug 'Base para Incentivo  : '+total+ ' bono del: '+bono
			importe=total*(bono)
		}
		return importe
	}

	BigDecimal calcularImporteMensual(NominaPorEmpleado ne,Incentivo i){
		def importe=0.0
		def salario=nominaPorEmpleado.empleado.salario.salarioDiario
		importe=(salario*30)i.tasaBono2
		return importe

	}
	
	def getModel(NominaPorEmpleadoDet det) {
		def nominaEmpleado=det.parent
		def model=[:]
		return model
	}
	
	def getTemplate() {
		return "/nominaPorEmpleado/conceptoInfo/percepcionIncentivo"
	}
	
	String toString() {
		"Procesador de Incentivo "
	}

}
