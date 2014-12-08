package com.luxsoft.sw4.rh



import org.apache.commons.lang.time.DateUtils

import grails.transaction.Transactional
import grails.transaction.NotTransactional

import com.luxsoft.sw4.rh.tablas.ZonaEconomica
import com.luxsoft.sw4.Periodo
import com.luxsoft.sw4.rh.imss.*
import com.luxsoft.sw4.rh.acu.*

import java.math.RoundingMode

@Transactional
class AguinaldoService {


	def generar(Empleado empleado ,Integer ejercicio){
		def aguinaldo=Aguinaldo.find{ejercicio==ejercicio && empleado==empleado}
		if(!aguinaldo){
			log.info "Generando aguinaldo para $empleado ($ejercicio)"
			aguinaldo=new Aguinaldo(empleado:empleado,ejercicio:ejercicio)
			def periodo=Periodo.getPeriodoAnual(ejercicio)
			aguinaldo.fechaInicial=DateUtils.addMonths(periodo.fechaInicial,-1)
			aguinaldo.fechaFinal=DateUtils.addMonths(periodo.fechaFinal,-1)
			aguinaldo.salario=empleado.salario.salarioDiario
			aguinaldo.sueldoMensual=empleado.salario.periodicidad=='SEMANAL'?aguinaldo.salario*31:aguinaldo.salario*32
			if(empleado.alta>aguinaldo.fechaInicial){
				
				aguinaldo.fechaInicial=empleado.alta
				aguinaldo.fechaFinal=periodo.fechaFinal
				
			}
			
			aguinaldo.diasParaAguinaldo=aguinaldo.getDiasDelEjercicio()
			aguinaldo.diasParaBono=aguinaldo.getDiasDelEjercicio()
			aguinaldo.save failOnError:true

		}
		return aguinaldo
	}

	@NotTransactional
	def calcular(Integer ejercicio){
		
		def ids=NominaPorEmpleado.executeQuery(
			"select distinct(n.empleado.id) from NominaPorEmpleado n where n.nomina.ejercicio=? and n.empleado.status!='BAJA'",[ejercicio])
		log.info "Calculando aguinaldo para $ids.size empleados del ejercicio $ejercicio"
		ids.each{ 
			def empleado=Empleado.get(it)
			try {
				def aguinaldo=Aguinaldo.findOrCreateWhere(ejercicio: ejercicio,empleado:empleado)
				def periodo=Periodo.getPeriodoAnual(aguinaldo.ejercicio)
				aguinaldo.fechaInicial=DateUtils.addMonths(periodo.fechaInicial,-1)
				aguinaldo.fechaFinal=DateUtils.addMonths(periodo.fechaFinal,-1)
				/*
				if(aguinaldo.id==null){
					def periodo=Periodo.getPeriodoAnual(ejercicio)
					
				}
				*/
				calcular(aguinaldo)
			}
			catch(Exception e) {
				log.error "Error calculando aguinaldo de $empleado ($ejercicio)",e
			}
			
		}
	}


    def calcular(Aguinaldo aguinaldo) {
		
		def periodo=Periodo.getPeriodoAnual(aguinaldo.ejercicio)
		def diasDelEjercicioReales=periodo.fechaFinal-periodo.fechaInicial+1
		def empleado =aguinaldo.empleado
		aguinaldo.fechaInicial=DateUtils.addMonths(periodo.fechaInicial,-1)
		aguinaldo.fechaFinal=DateUtils.addMonths(periodo.fechaFinal,-1)
		if(empleado.alta>aguinaldo.fechaInicial){
			
			aguinaldo.fechaInicial=empleado.alta
			aguinaldo.fechaFinal=periodo.fechaFinal
			
		}
		aguinaldo.diasParaAguinaldo=aguinaldo.getDiasDelEjercicio()
		aguinaldo.salario=aguinaldo.empleado.salario.salarioDiario
		aguinaldo.sueldoMensual=aguinaldo.empleado.salario.periodicidad=='SEMANAL'?aguinaldo.salario*31:aguinaldo.salario*32
		aguinaldo.diasParaAguinaldo=aguinaldo.getDiasDelEjercicio()
		aguinaldo.diasParaBono=aguinaldo.getDiasDelEjercicio()
		
    	log.info "Calculando aguinaldo: "+aguinaldo
		aguinaldo.salario=aguinaldo.empleado.salario.salarioDiario
		aguinaldo.diasParaAguinaldo=aguinaldo.diasDelEjercicio-aguinaldo.faltas-aguinaldo.incapacidades
		def factor=(aguinaldo.diasDeAguinaldo/diasDelEjercicioReales)*aguinaldo.diasParaAguinaldo
		aguinaldo.aguinaldo=factor*aguinaldo.salario
		
		log.info "Aguinaldo factor: ${factor} Salario:${aguinaldo.empleado.salario.salarioDiario} Aguinaldo:${aguinaldo.aguinaldo}"
    	if(aguinaldo.porcentajeBono<1.0 && aguinaldo.porcentajeBono>0.0){
			
		}else{
			aguinaldo.porcentajeBono=1.0
			if(aguinaldo.antiguedad<diasDelEjercicioReales){
				aguinaldo.porcentajeBono=0.0
			}
		}
		
		aguinaldo.diasParaBono=aguinaldo.diasDelEjercicio-aguinaldo.faltas-aguinaldo.incapacidades-aguinaldo.permisoEspecial
		def factorBono=(aguinaldo.diasDeBono/diasDelEjercicioReales)*aguinaldo.diasParaBono
		aguinaldo.bonoPreliminar=factorBono*aguinaldo.salario
		aguinaldo.bono=aguinaldo.bonoPreliminar*aguinaldo.porcentajeBono
		registrarCalculo(aguinaldo)
		calcularImpuestos(aguinaldo)
		calcularPago(aguinaldo)
		aguinaldo.save failOnError:true
    }
	
	def registrarCalculo(Aguinaldo aguinaldo){
		def zona=ZonaEconomica.findByClave('A')
		def topeSalarial=30*zona.salario
		def aguinaldoExcento=aguinaldo.aguinaldo<topeSalarial?aguinaldo.aguinaldo:topeSalarial
		def aguinaldoGravable=aguinaldo.aguinaldo-aguinaldoExcento
		
		def bonoGravable=aguinaldo.bono
		def totalGravable=aguinaldoGravable+bonoGravable
		
		aguinaldo.aguinaldoExcento=aguinaldoExcento
		aguinaldo.aguinaldoGravado=aguinaldoGravable
		aguinaldo.totalGravable=totalGravable
		
		aguinaldo.promedioGravable=(totalGravable/aguinaldo.diasDelEjercicio)*30.4
		aguinaldo.sueldoMensual=aguinaldo.salario*31
		aguinaldo.proporcionPromedioMensual=aguinaldo.promedioGravable+aguinaldo.sueldoMensual
		
		
		
		
	}
	
	def calcularImpuestos(Aguinaldo a){
		
		a.isrMensual=calcularImpuesto(a.sueldoMensual)
		a.isrPromedio=calcularImpuesto(a.proporcionPromedioMensual)
		a.difIsrMensualPromedio=a.isrPromedio-a.isrMensual
		a.tasa=a.difIsrMensualPromedio<=0?0.0:(a.difIsrMensualPromedio/a.promedioGravable).setScale(4,RoundingMode.HALF_EVEN)
		a.isrPorRetener=(a.tasa*a.totalGravable).setScale(2,RoundingMode.HALF_EVEN)
		
		def subsidio=Subsidio.obtenerTabla(30.4).find(){(a.sueldoMensual>it.desde && a.sueldoMensual<=it.hasta)}
		a.subsidio=subsidio.subsidio
		a.isrOSubsidio=a.isrMensual-a.subsidio
		a.resultadoIsrSubsidio=a.isrPorRetener+a.isrOSubsidio
		
		//Tabla normal ISR
		//def tablaNormalIsrSub=
		def t1=a.totalGravable+a.sueldoMensual
		def t1_isr=calcularImpuesto(t1)
		
		def t1_sub=Subsidio.obtenerTabla(30.4).find(){(t1>it.desde && t1<=it.hasta)}.subsidio
		a.tablaNormalIsrSub=t1_isr-t1_sub
		
		a.beneficioPerjuicio=a.resultadoIsrSubsidio-a.tablaNormalIsrSub
		
	}
	
	private BigDecimal calcularImpuesto(BigDecimal percepciones){
		def tarifa =TarifaIsr.obtenerTabla(30.4)
		.find(){(percepciones>it.limiteInferior && percepciones<=it.limiteSuperior)}
		def importeGravado=percepciones-tarifa.limiteInferior
		importeGravado*=tarifa.porcentaje
		importeGravado/=100
		importeGravado+=tarifa.cuotaFija
		importeGravado=importeGravado.setScale(2,RoundingMode.HALF_EVEN)
		return importeGravado
	}
	
	def calcularPago(Aguinaldo a){
		
		
		
		a.subTotal=a.totalGravable+a.aguinaldoExcento-a.isrPorRetener-a.isrEjerAnt
		def percepcion=a.subTotal
		
		def pension=buscarPension(a.empleado)
		if(pension){
			def importeP=0.0
			
			if(!pension.neto){
				importeP=(a.totalGravable+a.aguinaldoExcento)*(pension.porcentaje/100)
			}else{
				importeP=(a.subTotal)*(pension.porcentaje/100)
			}
			a.pensionA=importeP
			percepcion-=importeP
		}
		
		percepcion*=0.75
		
		def otraDeduccion=buscarOtrasDeducciones(a.empleado)
		
		if(otraDeduccion){
			
			if(otraDeduccion.saldo<=percepcion){
				a.otrasDed=otraDeduccion.saldo
				percepcion-=otraDeduccion.saldo
			}else{
				a.otrasDed=percepcion
			}
		}
		
		def prestamo=buscarPrestamo(a.empleado)
		
		if(prestamo){
			if(prestamo.saldo<=percepcion){
				a.prestamo=prestamo.saldo
			}else{
				a.prestamo=percepcion
			}
		}
		a.netoPagado=a.subTotal-a.pensionA-a.otrasDed-a.prestamo
		
		
	}
	
	private PensionAlimenticia buscarPension(Empleado e) {
		def pensiones=PensionAlimenticia.findAll("from PensionAlimenticia p where p.empleado=?"
			,[e],[max:1])
		return pensiones?pensiones[0]:null
	}
	
	private Prestamo buscarPrestamo(Empleado e) {
		def prestamos=Prestamo.findAll("from Prestamo p where p.saldo>0 and p.empleado=? order by p.saldo desc"
			,[e],[max:1])
		return prestamos?prestamos[0]:null
	}
	
	private OtraDeduccion buscarOtrasDeducciones(Empleado e){
		def d=OtraDeduccion.findAll("from OtraDeduccion d where d.saldo>0 and d.empleado=? order by d.saldo desc"
			,[e],[max:1])
		return d?d[0]:null
	}
}
