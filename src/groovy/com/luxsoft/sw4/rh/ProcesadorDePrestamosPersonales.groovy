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
		
		//Buscando un prestamo vigente
		def prestamo=buscarPrestamo(ne)
		if(prestamo) {
			
			log.info "Aplicando decucccon para prestamo vigente: ${prestamo}"
			
			
			def percepciones=getPercepciones(ne)
			def deducciones=getRetencionesPrecedentes(ne)
			/*
			def deducciones=0
			["D002","D001","D013","D007","D006","D012","D014"].each{ clave->
				def c=ne.conceptos.find {it.concepto.clave==clave}
				if(c){
					log.info 'Deduccion previa: '+c
					deducciones+=c.getTotal()
				}
			}
			def retMaxima=(percepciones-deducciones)*0.3
			
			log.info 'Deducciones calculadas: '+deducciones
			log.info 'Percepcion maxima calculada: '+retMaxima
			*/
			def salarioMinimo=ZonaEconomica.valores.find(){it.clave='A'}.salario
			//def retMaxima=( (ne.salarioDiarioBase-salarioMinimo)*(ne.diasDelPeriodo-ne.incapacidades-ne.faltas) )*0.3
			def diasNetos=ne.diasDelPeriodo-ne.incapacidades-ne.faltas
			def retMaxima=percepciones-deducciones-(salarioMinimo*diasNetos)
			retMaxima*=0.3
			
			def otrasDeducciones=ne.conceptos.find {it.concepto.clave=="D005"}
			if(otrasDeducciones){
				retMaxima-=otrasDeducciones.getTotal()
				if(retMaxima<0)
					retMaxima=0
			}
			if(prestamo.importeFijo>0){
				retMaxima=prestamo.importeFijo
			}
			
			if(retMaxima){
				def saldo=prestamo.saldo
				def importeExcento=retMaxima<=saldo?retMaxima:saldo
				//Localizar el concepto
				def neDet=new NominaPorEmpleadoDet(concepto:concepto,importeGravado:0.0,importeExcento:0.0,comentario:'PENDIENTE')
				log.info "Deduccion calculada de: ${importeExcento}"
				neDet.importeGravado=0
				neDet.importeExcento=importeExcento.setScale(2,RoundingMode.HALF_EVEN)
				ne.addToConceptos(neDet)
				ne.actualizar()
			}
			
		}
		
	}
	
	private Prestamo buscarPrestamo(NominaPorEmpleado ne) {
		def prestamos=Prestamo.findAll("from Prestamo p where p.saldo>0.0 and p.empleado=? order by p.id  asc"
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

	private BigDecimal getDeducciones(){
		
	}
	
	private BigDecimal getRetencionesPrecedentes(NominaPorEmpleado ne) {
		
		def acu=0.0
		ne.conceptos.each{
			if(it.concepto.clave=='D012'){
				acu+=it.getTotal()
			}
		}
		return acu;
	}
	
	
	
	def getModel(NominaPorEmpleadoDet det) {
		def ne=det.parent
		def prestamo=buscarPrestamo(ne)
		def salarioMinimo=ZonaEconomica.valores.find(){it.clave='A'}.salario
		def retMaxima=( (ne.salarioDiarioBase-salarioMinimo)*(ne.diasDelPeriodo-ne.incapacidades-ne.faltas) )*0.3
		def otrasDeducciones=ne.conceptos.find {it.concepto.clave=="D005"}
		def model=[prestamo:prestamo
			,salarioDiario:ne.salarioDiarioBase
			,salarioMinimo:salarioMinimo
			,diasTrabajados:(ne.diasDelPeriodo-ne.incapacidades-ne.faltas)
			,factor:0.3
			,tope:retMaxima
			,otras:otrasDeducciones?.total
			,total:det.getTotal()
			]
		/*
		def deducciones=[]
		["D002","D001","D013","D007","D006","D012"].each{ clave->
			def c=ne.conceptos.find {it.concepto.clave==clave}
			if(c){
				//deducciones+=c.getTotal()
				deducciones.add(c)
			}
		}
		def otrasDeducciones=ne.conceptos.find {it.concepto.clave=="D005"}
		if(otrasDeducciones){
			deducciones.add(otrasDeducciones)
		}
		model.deducciones=deducciones
		
		def abono=ne.conceptos.find {it.concepto.clave=="D004"}
		
		model.total=abono.getTotal()
		*/
		return model
	}
	
	def getTemplate() {
		return "/nominaPorEmpleado/conceptoInfo/deduccionPrestamoPersonal"
	}
	
	String toString() {
		"Procesador de Prestamo personal "
	}

}
