package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import org.apache.commons.lang.time.DateUtils

import grails.transaction.Transactional
import grails.transaction.NotTransactional

import com.luxsoft.sw4.rh.tablas.ZonaEconomica
import com.luxsoft.sw4.Periodo
import com.luxsoft.sw4.rh.imss.*
import com.luxsoft.sw4.rh.acu.*
import java.math.RoundingMode

@Transactional
class CalculoAnualService {

    def generar(Empleado empleado ,Integer ejercicio){
		def calculo=CalculoAnual.find{ejercicio==ejercicio && empleado==empleado}
		if(!calculo){
			log.info "Generando calculo anual para $empleado ($ejercicio)"
			calculo=new CalculoAnual(empleado:empleado,ejercicio:ejercicio)
			def periodo=Periodo.getPeriodoAnual(ejercicio)
			calculo.fechaInicial=DateUtils.addMonths(periodo.fechaInicial,-1)
			calculo.fechaFinal=DateUtils.addMonths(periodo.fechaFinal,-1)
			calculo.salario=empleado.salario.salarioDiario
			//calculo.sueldoMensual=empleado.salario.periodicidad=='SEMANAL'?calculo.salario*31:calculo.salario*32
			if(empleado.alta>calculo.fechaInicial){
				calculo.fechaInicial=empleado.alta
				calculo.fechaFinal=periodo.fechaFinal
			}
			calculo.save failOnError:true
		}
		return calculo
	}

	@NotTransactional
	def calcular(Integer ejercicio){
		def ids=NominaPorEmpleado.executeQuery(
			"select distinct(n.empleado.id) from NominaPorEmpleado n where n.nomina.ejercicio=? and n.empleado.status!='BAJA'",[ejercicio])
		log.info "Generando calculando anual para $ids.size empleados del ejercicio $ejercicio"
		ids.each{ 
			def empleado=Empleado.get(it)
			try {
				def calculo=CalculoAnual.findOrCreateWhere(ejercicio: ejercicio,empleado:empleado)
				def periodo=Periodo.getPeriodoAnual(calculo.ejercicio)
				calculo.fechaInicial=DateUtils.addMonths(periodo.fechaInicial,-1)
				calculo.fechaFinal=DateUtils.addMonths(periodo.fechaFinal,-1)
				
				calcular(calculo)
			}
			catch(Exception e) {
				log.error "Error calculando calculo de $empleado ($ejercicio)",e
			}
			
		}
	}

    def calcular(CalculoAnual calculo) {
		log.info 'Actualizando :'+calculo
		def periodo=Periodo.getPeriodoAnual(calculo.ejercicio)
		def diasDelEjercicioReales=periodo.fechaFinal-periodo.fechaInicial+1
		def empleado =calculo.empleado
		calculo.fechaInicial=DateUtils.addMonths(periodo.fechaInicial,-1)
		calculo.fechaFinal=DateUtils.addMonths(periodo.fechaFinal,-1)
		if(empleado.alta>calculo.fechaInicial){
			
			calculo.fechaInicial=empleado.alta
			calculo.fechaFinal=periodo.fechaFinal
			
		}
		calculo.salario=calculo.empleado.salario.salarioDiario
		calculo.resultado=0.0
		calculo.save failOnError:true
    }
}
