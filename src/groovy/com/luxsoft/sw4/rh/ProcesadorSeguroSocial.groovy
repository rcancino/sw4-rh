package com.luxsoft.sw4.rh

import org.apache.commons.logging.LogFactory
import com.luxsoft.sw4.rh.imss.*
import java.math.*

class ProcesadorSeguroSocial {
	
	def conceptoClave='D001'
	
	def concepto
	
	private static final log=LogFactory.getLog(this)
	
	def procesar(NominaPorEmpleado nominaPorEmpleado) {
		
		if(!concepto) {
			concepto=ConceptoDeNomina.findByClave(conceptoClave)
		}
		log.info "Procesando retension de seguro social  para ${nominaPorEmpleado.empleado}"
		//Localizar el concepto
		def nominaPorEmpleadoDet=nominaPorEmpleado.conceptos.find(){ 
			it.concepto==concepto
		}
		
		if(!nominaPorEmpleadoDet){
			nominaPorEmpleadoDet=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
			nominaPorEmpleado.addToConceptos(nominaPorEmpleadoDet)
		}
		
		def salarioMinimo=ZonaEconomica.valores.find(){it.clave='A'}.salario
		log.debug 'Salario minimo: '+salarioMinimo
		def empleado=nominaPorEmpleado.empleado
		if(salarioMinimo==empleado.salario.salarioDiario){
			nominaPorEmpleado.removeFromConceptos(nominaPorEmpleadoDet)
			return
		}
		
		def sdi=empleado.salario.salarioDiarioIntegrado
		def factorDescanso=1/6
	
		def faltas=nominaPorEmpleado.faltas
		faltas=faltas+(faltas*factorDescanso)
		log.debug "Faltas $nominaPorEmpleado.faltas  FactorDescanso: $factorDescanso"
		
		
		def diasTrabajados=nominaPorEmpleado.diasTrabajados+(nominaPorEmpleado.fraccionDescanso as BigDecimal)
			diasTrabajados+=nominaPorEmpleado.vacaciones+nominaPorEmpleado.asistencia.paternidad
		def diasDelPeriodo=nominaPorEmpleado.diasDelPeriodo-nominaPorEmpleado.incapacidades
		if(nominaPorEmpleado.asistencia.diasTrabajados>0 && (nominaPorEmpleado.empleado.controlDeAsistencia) ){
			diasDelPeriodo=nominaPorEmpleado.asistencia.diasTrabajados		
			
		}
		log.debug 'Dias trabajados: '+diasTrabajados+ ' Vacaciones: '+nominaPorEmpleado.vacaciones
		log.debug 'Dias del periodo: '+diasDelPeriodo
	
		def prima=0.5 //Numer magico por el momento
	
		def aporacionAsegurado=0.0
	
	
		//EyM sobre 1 SMGDF
		aporacionAsegurado=0
	
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
		emd=(emd*0.40*diasDelPeriodo)/100
		emd=emd.setScale(2,RoundingMode.HALF_EVEN)
		log.debug 'EyM sobre dif. entre SBC y 3 SMGDF: '+emd
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
		def pd=((val3*0.25)*diasDelPeriodo)/100
		pd=pd.setScale(2,RoundingMode.HALF_EVEN)
		aporacionAsegurado+=pd
		log.debug 'Prestaciones en dinero EyM sobre SBC: '+pd
	
		def gmp=((val3*0.375)*diasDelPeriodo)/100
		gmp=gmp.setScale(2,RoundingMode.HALF_EVEN)
		aporacionAsegurado+=gmp
		log.debug 'Gastos mdicos pensionado sobre SBC: '+gmp
	
		def iv=((val3*0.625)*diasTrabajados)/100
		iv=iv.setScale(2,RoundingMode.HALF_EVEN)
		aporacionAsegurado+=iv
		log.debug 'Invalidez y Vida sobre SBC: '+iv
	
		def sr=0
		aporacionAsegurado+=sr
		log.debug 'Seguro de Retiro: '+sr
	
		def cv=((val3*1.125)*diasTrabajados)/100
		cv=cv.setScale(2,RoundingMode.HALF_EVEN)
		aporacionAsegurado+=cv
		log.debug 'Cesanta edad avanzada y vejez sobre SBC: '+cv
	
		def sg=0
		aporacionAsegurado+=sg
		log.debug 'Seguro de Guarderas sobre SBC: '+sg
	
		def rt=0
		aporacionAsegurado+=rt
		log.debug 'Riesgos de trabajo: '+rt
	
		def inf=0
		aporacionAsegurado+=inf
		log.debug 'Infonavit: '+inf
			
		if(aporacionAsegurado>0){
			nominaPorEmpleadoDet.importeExcento=aporacionAsegurado
			nominaPorEmpleadoDet.importeGravado=0.0
			if(diasTrabajados<=0){
				nominaPorEmpleadoDet.importeExcento=0.0
			}
		}
		
		
	}
	
	def getModel(NominaPorEmpleadoDet det) {
		def ne=det.parent
		def model=[:]
		
		return model
	}
	
	def getTemplate() {
		return "/nominaPorEmpleado/conceptoInfo/deduccionIMSS"
	}
	
	String toString() {
		"Procesador IMSS "
	}

}
