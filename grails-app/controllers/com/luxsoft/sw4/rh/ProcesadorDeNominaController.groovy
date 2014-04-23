package com.luxsoft.sw4.rh

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import groovy.lang.Binding

import grails.plugin.springsecurity.annotation.Secured;
import grails.transaction.Transactional;

@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
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
		
		//Conceptos basicos
		['P001','D001','D002'].each{ clave ->
			def concepto=ne.conceptos.find(){ det ->
				det.concepto.clave==clave
			}
			if(!concepto){
				concepto=ConceptoDeNomina.findByClave(clave)
				ne.addToConceptos(new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE'))
			}
		}
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
				//det.importeGravado=0
				//det.importeExcento=0
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
		 'P021':REGLA_ISTP,
		 //'D001':REGLA_IMSS
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
	 /*
	static def REGLA_IMSS="""

	import com.luxsoft.sw4.*
	import com.luxsoft.sw4.rh.*
	import com.luxsoft.sw4.rh.imss.*
	import java.math.*

	def salarioMinimo=ZonaEconomica.valores.find(){it.clave='A'}.salario
	def sdi=empleado.salario.salarioDiarioIntegrado
	def diasTrabajados=nominaEmpleado.nomina.getDiasPagados()

	def prima=0.5 //Numer magico por el momento

	def aporacionAsegurado=0.0


	//EyM sobre 1 SMGDF
	aportacionAsegurado=0

	//'EyM sobre dif. entre SBC y 3 SMGDF
	def emd=0.0
	if(sdi<(salarioMinimo*25)){
		if(sdi<(salarioMinimo*3)){
    		emd=0.0
		}else{
    		emd=sdi-(salarioMinimo*3)
		}
	}else{
		emd=(salarioMinimo*25)-(salarioMinimo*3)
	}
	emd=(emd*0.40*diasTrabajados)/100
	emd=emd.setScale(2,RoundingMode.HALF_EVEN)
	println 'EyM sobre dif. entre SBC y 3 SMGDF: '+emd
	aporacionAsegurado+=emd

	'Prestaciones en dinero EyM sobre SBC'
	def val3
	if(sdi<(salarioMinimo*1.0452)){
		val3=(salarioMinimo*1.0452)
	}else if(sdi<(salarioMinimo*25)){
		val3=sdi
	}else{
		val3=(salarioMinimo*25)
	}
	def pd=((val3*0.25)*diasTrabajados)/100
	pd=pd.setScale(2,RoundingMode.HALF_EVEN)
	aporacionAsegurado+=pd
	println 'Prestaciones en dinero EyM sobre SBC: '+pd

	def gmp=((val3*0.375)*diasTrabajados)/100
	gmp=gmp.setScale(2,RoundingMode.HALF_EVEN)
	aporacionAsegurado+=gmp
	println 'Gastos mdicos pensionado sobre SBC: '+gmp

	def iv=((val3*0.625)*diasTrabajados)/100
	iv=iv.setScale(2,RoundingMode.HALF_EVEN)
	aporacionAsegurado+=iv
	println 'Invalidez y Vida sobre SBC: '+iv

	def sr=0
	aporacionAsegurado+=sr
	println 'Seguro de Retiro: '+sr

	def cv=((val3*1.125)*diasTrabajados)/100
	cv=cv.setScale(2,RoundingMode.HALF_EVEN)
	aporacionAsegurado+=cv
	println 'Cesanta edad avanzada y vejez sobre SBC: '+cv

	def sg=0
	aporacionAsegurado+=sg
	println 'Seguro de Guarderas sobre SBC: '+sg

	def rt=0
	aporacionAsegurado+=rt
	println 'Riesgos de trabajo: '+rt

	def inf=0
	aporacionAsegurado+=inf
	println 'Infonavit: '+inf

	nominaEmpleadoDet.importeGravado=aporacionAsegurado

	"""
	*/
}
