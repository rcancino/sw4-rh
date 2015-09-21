package com.luxsoft.sw4.rh

import java.math.RoundingMode;

import org.apache.commons.logging.LogFactory

import com.luxsoft.sw4.rh.imss.*

class ProcesadorDeOtrasDeducciones {
	
	def conceptoClave='D005'
	
	def concepto
	
	private static final log=LogFactory.getLog(this)
	
	def procesar(NominaPorEmpleado ne) {
		
		if(!concepto) {
			concepto=ConceptoDeNomina.findByClave(conceptoClave)
			assert concepto,"Se debe de dar de alta el concepto de nomina: $conceptoClave"
		}
	
		//Buscando un deduccion vigente
		def deduccion=buscarOtraDeduccion(ne)
		if(deduccion) {
			
			log.debug "Aplicando decucccon para otras deducciones  vigente: ${deduccion.id}"
			/*
			def percepciones=getPercepciones(ne)
			
			def deducciones=getRetencionesPrecedentes(ne)
			
			def retMaxima=(percepciones-deducciones)*0.3
			log.debug 'Deducciones calculadas: '+deducciones
			log.info 'Retencion maxima permitida: '+retMaxima
			*/
			
			//def salarioMinimo=ZonaEconomica.valores.find(){it.clave='A'}.salario
			//def retMaxima=( (ne.salarioDiarioBase-salarioMinimo)*(ne.diasDelPeriodo-ne.incapacidades-ne.faltas) )*0.3

			def percepciones=getPercepciones(ne)
			def deducciones=getRetencionesPrecedentes(ne)
			
			def salarioMinimo=ZonaEconomica.valores.find(){it.clave='A'}.salario

			def diasNetos=ne.diasDelPeriodo-ne.incapacidades-ne.faltas
			def retMaxima=percepciones-deducciones-(salarioMinimo*diasNetos)
			retMaxima*=0.3
			
			

			if(retMaxima){
				def saldo=deduccion.saldo
				
				def importeExcento=0.0
				if(retMaxima>=saldo){
					importeExcento=saldo
				}else{
					def prestamo=buscarPrestamo(ne)
					if(prestamo){
						importeExcento=retMaxima/2
					}else{
						importeExcento=retMaxima
					}
				}
				
				//def importeExcento=retMaxima<=saldo?retMaxima:saldo
				
				//Localizar el concepto
				def neDet=ne.conceptos.find(){
					it.concepto==concepto
				}
				
				if(!neDet){
					neDet=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
					ne.addToConceptos(neDet)
				}
				log.info "Deduccion calculada de: ${importeExcento}"
				neDet.importeGravado=0
				neDet.importeExcento=importeExcento.setScale(2,RoundingMode.HALF_EVEN)
				ne.actualizar()
				
				
				//Actualizar el saldo del prestamo
				/*
				def abono=deduccion.abonos.find{
					if(it.nominaPorEmpleadoDet){
						return it.nominaPorEmpleadoDet.id==neDet?.id
					}
					return false
				}
				if(abono){
					log.info 'Actualizando abono existente'
					abono.importe=neDet.importeExcento
				}else{
					log.info 'Generando abono nuevo '
					abono=new OtraDeduccionAbono(
							fecha:neDet.parent.nomina.pago
							,importe:neDet.importeExcento
							,nominaPorEmpleadoDet:neDet)
					deduccion.addToAbonos(abono)
				}
				*/
			}
		}else{
			def neDet=ne.conceptos.find(){
				it.concepto==concepto
			}
			if(neDet){
				ne.removeFromConceptos(neDet)
			}
		}
		
		
	}
	
	private OtraDeduccion buscarOtraDeduccion(NominaPorEmpleado ne) {
		def prestamos=OtraDeduccion.findAll("from OtraDeduccion o where o.saldo>0.0 and o.empleado=? order by o.id asc"
			,[ne.empleado],[max:1])
		return prestamos?prestamos[0]:null
	}
	
	private BigDecimal getPercepciones(NominaPorEmpleado ne){
		def invalidas=['P019','P021','P033']
		def res=0.0
		ne.conceptos.each{
			if(it.concepto.tipo=='PERCEPCION'){
				if(!invalidas.contains(it.concepto.clave) ){
					res+=it.getTotal()
				}
			}
		}
		return res;
	}
	
	/*
	private BigDecimal getRetencionesPrecedentes(NominaPorEmpleado ne) {
		def deducciones=0
		//["D002","D001","D013","D007","D006","D012","D014"].each{ clave->
		["D012"].each{ clave->
				def c=ne.conceptos.find {it.concepto.clave==clave}
				if(c){
					log.info 'Deduccion previa: '+c
					deducciones+=c.getTotal()
				}
			}
		return deducciones
	}
	*/
	
	private BigDecimal getRetencionesPrecedentes(NominaPorEmpleado ne) {
		
		def acu=0.0
		ne.conceptos.each{
			if(it.concepto.clave=='D012'){
				acu+=it.getTotal()
			}
		}
		return acu;
	}
	
	private Prestamo buscarPrestamo(NominaPorEmpleado ne) {
		def prestamos=Prestamo.findAll("from Prestamo p where p.saldo>0 and p.empleado=? order by p.saldo desc"
			,[ne.empleado],[max:1])
		return prestamos?prestamos[0]:null
	}
	
	
	
	def getModel(NominaPorEmpleadoDet det) {
		def ne=det.parent
		def prestamo=buscarOtraDeduccion(ne)
		def salarioMinimo=ZonaEconomica.valores.find(){it.clave='A'}.salario
		//def retMaxima=( (ne.salarioDiarioBase-salarioMinimo)*ne.diasTrabajados )*0.3
		def retMaxima=( (ne.salarioDiarioBase-salarioMinimo)*(ne.diasDelPeriodo-ne.incapacidades-ne.faltas) )*0.3
		
		def otrasPrestamo=ne.conceptos.find {it.concepto.clave=="D004"}
		def model=[prestamo:prestamo
			,salarioDiario:ne.salarioDiarioBase
			,salarioMinimo:salarioMinimo
			,diasTrabajados:(ne.diasDelPeriodo-ne.incapacidades-ne.faltas)
			,factor:0.3
			,tope:retMaxima
			,otroPrestamo:otrasPrestamo.getTotal()
			,total:det.getTotal()
			]
		
		return model
	}
	
	def getTemplate() {
		return "/nominaPorEmpleado/conceptoInfo/deduccionOtrasDeducciones"
	}
	
	String toString() {
		"Procesador de Otra deduccion "
	}

}
