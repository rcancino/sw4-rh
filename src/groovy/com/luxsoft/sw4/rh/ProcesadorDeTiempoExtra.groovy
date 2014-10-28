package com.luxsoft.sw4.rh

import java.math.RoundingMode;

import org.apache.commons.logging.LogFactory

import com.luxsoft.sw4.rh.imss.*

class ProcesadorDeTiempoExtra {
	
	
	private static final log=LogFactory.getLog(this)
	
	def procesar(NominaPorEmpleado ne) {
		
		//Buscando un prestamo vigente
		def te=buscarTiempoExtra(ne)
		
		if(te) {
			
			def concepto=ConceptoDeNomina.findByClave('P022')
			def neDet=ne.conceptos.find(){
				it.concepto==concepto
			}
			if(!neDet){
				neDet=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
				
			}
			neDet.importeGravado=te.getDoblesGravados()
			neDet.importeExcento=te.getDoblesExcentos()
			
			if(!neDet.id)
				ne.addToConceptos(neDet)
				
			if(te.getTriplesGravados()>0){
				
				def horasTriplesConcepto=ConceptoDeNomina.findByClave('P023')
				def horasTriplesDet=ne.conceptos.find(){
					it.concepto==horasTriplesConcepto
				}
				log.info 'Aplicando horas triples concepto: '+horasTriplesConcepto
				if(!horasTriplesDet){
					log.info 'No existia en la nomina el concepto generando uno nuevo'
					horasTriplesDet=new NominaPorEmpleadoDet(concepto:horasTriplesConcepto
						,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
					ne.addToConceptos(horasTriplesDet)
				}
				horasTriplesDet.importeGravado=te.getTriplesGravados()
				horasTriplesDet.importeExcento=0.0
				/*
				if(!neDet2){
					neDet2=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
					
				}
				neDet2.importeGravado=te.getTriplesGravados()
				neDet2.importeExcento=0.0
				if(!neDet2.id)
					ne.addToConceptos(neDet)
					*/
			}
			
			ne.actualizar()

		}
		
		
	}
	
	private TiempoExtra buscarTiempoExtra(NominaPorEmpleado ne) {
		def te=TiempoExtra.find("from TiempoExtra t where t.asistencia=? "
			,[ne.asistencia])
		return te
	}
	
	
	
	def getModel(NominaPorEmpleadoDet det) {
		def ne=det.parent
		def model=[:]
		return model
	}
	
	def getTemplate() {
		return "/nominaPorEmpleado/conceptoInfo/percepcionTiempoExtra"
	}
	
	String toString() {
		"Procesador de credito Tiempo Extra "
	}

}
