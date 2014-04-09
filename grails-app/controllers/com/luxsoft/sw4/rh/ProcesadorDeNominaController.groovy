package com.luxsoft.sw4.rh

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import groovy.lang.Binding

import grails.transaction.Transactional;


class ProcesadorDeNominaController {
	
	@Transactional
    def generarPlantilla(Nomina nomina) { 
		//log.info 'Generando plantilla para nomina: '+nomina
		
		switch (nomina.tipo){
			case 'GENERAL':
				nomina=generarPlantillaGeneralQuincenal(nomina)
				break
			default:
				throw new RuntimeException("No hay procesador para el tipo de nomina: $nomina.tipo")
		}
		redirect controller:'nomina',action:'show',params:[id:nomina.id]
	}
	
	@Transactional
	private Nomina generarPlantillaGeneralQuincenal(Nomina nomina){
		//Buscar todos los empleados activos y de periodicidad igual a la nomina
		log.info 'Generando plantilla de nomina general a: '+nomina
		def empleados=Empleado.findAll{status=='ALTA' && salario.periodicidad==nomina.periodicidad}
		log.info 'Empleados a incluir: '+empleados.size()
		empleados.each{ emp ->
			NominaPorEmpleado ne=nomina.partidas.find{
				it.empleado.id==emp.id
			}
			if(!ne){
				ne=new NominaPorEmpleado(
					empleado:emp,
					ubicacion:emp.perfil.ubicacion,
					antiguedadEnSemanas:0,
					nomina:nomina
					)
				def res=nomina.addToPartidas(ne)
				println 'Agregando '+ne+' Res: '+res
				ne.antiguedadEnSemanas=ne.getAntiguedad()
			}
			//Actualizamos salarios si no se ha generado el recibo
			if(!ne.cfdi){
				ne.salarioDiarioBase=ne.empleado.salario.salarioDiario
				ne.salarioDiarioIntegrado=ne.empleado.salario.salarioDiarioIntegrado
				ne.antiguedadEnSemanas=ne.getAntiguedad()
			}
			
			//Aregar los conceptos basicos de una poliza general
			['P001','D001','D002'].each{ clave ->
				def concepto=ne.conceptos.find(){ det ->
					det.concepto.clave==clave
				}
				if(!concepto){
					concepto=ConceptoDeNomina.findByClave(clave)
					ne.addToConceptos(new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE'))
				}
				
			}
			
			
		}
		
		nomina=nomina.save()
		println 'Partidas' +nomina.partidas.size()
		return nomina
		
	}
	
	@Transactional
	def actualizarNominaPorEmpleado(NominaPorEmpleado ne){
		log.info 'Actualizando nomina de: '+ne
		ne.salarioDiarioBase=ne.empleado.salario.salarioDiario
		ne.salarioDiarioIntegrado=ne.empleado.salario.salarioDiarioIntegrado
		ne.antiguedadEnSemanas=ne.getAntiguedad()
		//Pasamos por cada concepto
		
		//Agregamo insentivos a quien corresponda
		
		ne.conceptos.each{ det ->
			def concepto=det.concepto
			//Busca la regla para este concepto y
			def rule=REGLAS[concepto.clave]
			if(rule){
				try {
					Binding binding=new Binding(
						nominaEmpleado:ne,
						nominaEmpleadoDet:det,
						empleado:ne.empleado
						)
					def shell=new GroovyShell(this.class.classLoader,binding)
					shell.evaluate(rule)
				} catch (Exception e) {
					det.comentario="Error en calculo: "+ExceptionUtils.getRootCauseMessage(e)
					log.error e
				}
				
			} else{
				det.importeGravado=0
				det.importeExcento=0
			}
			
		}
		ne.save(flush:true)
		redirect controller:'nominaPorEmpleado',action:'edit',params:[id:ne.id]
	}
	
	
	
	 static Map REGLAS=[
		 'P001':"""
	 		def salarioDiario=empleado.salario.salarioDiario
	 		def diasTrabajados=nominaEmpleado.nomina.getDiasPagados()
	 		def importeGravado=salarioDiario*diasTrabajados
	 		nominaEmpleadoDet.importeGravado=importeGravado
			nominaEmpleadoDet.importeExcento=0

		 """,
		 'D002':REGLA_ISTP,
		 'P021':REGLA_ISTP
		 ]
	 
	 static def REGLA_ISTP="""
import com.luxsoft.sw4.*
	 		import com.luxsoft.sw4.rh.*
	 		import com.luxsoft.sw4.rh.imss.*
	 		import java.math.*
	 		
def percepciones=nominaEmpleado.getPercepcionesGravadas()
def diasTrabajados=nominaEmpleado.nomina.getDiasPagados()

def found =TarifaIsr.obtenerTabla(diasTrabajados).find(){(percepciones>it.limiteInferior && percepciones<=it.limiteSuperior)}
def subsidio=Subsidio.obtenerTabla(diasTrabajados).find(){(percepciones>it.desde && percepciones<=it.hasta)}

def importeGravado=percepciones-found.limiteInferior
importeGravado*=found.porcentaje
importeGravado/=100
importeGravado+=found.cuotaFija
importeGravado=importeGravado.setScale(2,RoundingMode.HALF_EVEN)

def sub=importeGravado-subsidio.subsidio
nominaEmpleado.subsidioEmpleoAplicado=subsidio.subsidio

if(sub<0){
	 sub=sub.abs()
     nominaEmpleadoDet.concepto=ConceptoDeNomina.findByClave('P021')
     nominaEmpleadoDet.importeGravado=0.0
	 nominaEmpleadoDet.importeExcento=sub
}else{
	 nominaEmpleadoDet.importeGravado=importeGravado
	 nominaEmpleadoDet.importeExcento=0.0
}
nominaEmpleado.actualizar()
"""
}
