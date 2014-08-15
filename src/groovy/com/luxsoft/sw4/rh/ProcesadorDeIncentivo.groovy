package com.luxsoft.sw4.rh

import java.math.RoundingMode;

import org.apache.commons.logging.LogFactory

import com.luxsoft.sw4.rh.imss.*

class ProcesadorDeIncentivo {
	
	def conceptoClave='P010'
	
	def concepto
	
	private static final log=LogFactory.getLog(this)
	
	def procesar(NominaPorEmpleado nominaEmpleado) {
		log.info "Procesando Incentivo para ${nominaEmpleado.empleado}"
		
		if(!concepto) {
			concepto=ConceptoDeNomina.findByClave(conceptoClave)
		}
		assert concepto,"Se debe de dar de alta el concepto de nomina: $conceptoClave"
		
		def incentivo=null
		if(nominaEmpleado.empleado.salario.periodicidad=='QUINCENAL'){
			incentivo=Incentivo.findByCalendarioFinAndEmpleado(nominaEmpleado.nomina.calendarioDet,nominaEmpleado.empleado)
		}
		if(incentivo==null){
			log.debug 'No existe calculo de incentivo para empleado: '+nominaEmpleado.empleado+ ' Calendario: '+nominaEmpleado.nomina.calendarioDet.id
			return
		}
		
		//Localizar el concepto
		def nominaPorEmpleadoDet=nominaEmpleado.conceptos.find(){ 
			it.concepto==concepto
		}
		
		if(!nominaPorEmpleadoDet){
			nominaPorEmpleadoDet=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
			nominaEmpleado.addToConceptos(nominaPorEmpleadoDet)
		}
		
		
		def importeGravado=0.0
		// Buscae el sueldo
		def salario=nominaEmpleado.conceptos.find{it.concepto.clave=='P001'}
		if(salario){
			def bono=(incentivo.tasaBono1+incentivo.tasaBono2)
			log.debug 'Aplicando a sueldo: '+salario.total+ ' bono del: '+bono
			importeGravado=salario.total*(bono)
		}
		
		if(importeGravado>0){
			nominaPorEmpleadoDet.importeGravado=importeGravado
			nominaPorEmpleadoDet.importeExcento=0
			nominaEmpleado.actualizar()
		}else{
			nominaEmpleado.removeFromConceptos(nominaEmpleado)
		}
		
		
		
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
