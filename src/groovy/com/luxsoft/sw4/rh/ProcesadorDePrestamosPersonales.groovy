package com.luxsoft.sw4.rh

import java.math.RoundingMode;

import org.apache.commons.logging.LogFactory

import com.luxsoft.sw4.rh.imss.*

class ProcesadorDePrestamosPersonales {
	
	def conceptoClave='D004'
	
	def concepto
	
	private static final log=LogFactory.getLog(this)
	
	def procesar(NominaPorEmpleado ne) {
		
		if(!concepto) {
			concepto=ConceptoDeNomina.findByClave(conceptoClave)
			assert concepto,"Se debe de dar de alta el concepto de nomina: $conceptoClave"
		}
		
		//Localizar el concepto
		def neDet=ne.conceptos.find(){
			it.concepto==concepto
		}
		
		//Buscando un prestamo vigente
		def prestamo=buscarPrestamo(ne)
		if(prestamo) {
			
			log.debug "Aplicando decucccon para prestamo vigente: ${prestamo}"
			
			def importeExcento=deduccion(prestamo, ne)
			
			if(!neDet){
				neDet=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
				ne.addToConceptos(nominaPorEmpleadoDet)
			}
			log.debug "Deduccion generada de: ${importeExcento}"
			neDet.importeGravado=0
			neDet.importeExcento=importeExcento
			neDet.actualizar()
			
		}else {
			if(neDet) {
				def nomina=ne.nomina
				nomina.removeFromConcepto(neDet)
			}
		}
		
		
	}
	
	private Prestamo buscarPrestamo(NominaPorEmpleado ne) {
		def prestamos=Prestamo.findAll("from Prestamo p where p.saldo>0 and p.empleado=? order by p.saldo desc"
			,[ne.empleado],[max:1])
		return prestamos?prestamos[0]:null
	}
	
	private BigDecimal deduccion(Prestamo prestamo,NominaPorEmpleado ne) {
		def saldo=prestamo.saldo
		def percepciones=ne.getPercepciones()-getRetencionesPrecedentes(ne)
		def abonoTentativo=percepciones*prestamo.tasaDescuento
		def importeExcento=abonoTentativo<=saldo?abonoTentativo:saldo
		return importeExcento
	}
	
	private BigDecimal getRetencionesPrecedentes(NominaPorEmpleado ne) {
		return 0.0;
	}
	
	
	
	def getModel(NominaPorEmpleadoDet det) {
		def ne=det.parent
		def prestamo=buscarPrestamo(ne)
		def deduccion=deduccion(prestamo,ne)
		def model=[prestamo:prestamo,deduccion:deduccion]
		return model
	}
	
	def getTemplate() {
		return "/nominaPorEmpleado/conceptoInfo/deduccionPrestamo"
	}
	
	String toString() {
		"Procesador de Prestamo personal "
	}

}
