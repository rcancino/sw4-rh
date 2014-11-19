package com.luxsoft.sw4.rh

import org.apache.commons.lang.time.DateUtils

import grails.transaction.Transactional
import grails.transaction.NotTransactional

import com.luxsoft.sw4.Periodo

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
			aguinaldo.diasParaAguinaldo=aguinaldo.getDiasDelEjercicio()
			aguinaldo.diasParaBono=aguinaldo.getDiasDelEjercicio()
			aguinaldo.save failOnError:true

		}
		return aguinaldo
	}

	@NotTransactional
	def calcular(Integer ejercicio){
		
		def ids=NominaPorEmpleado.executeQuery(
			"select distinct(n.empleado.id) from NominaPorEmpleado n where n.nomina.ejercicio=?",[ejercicio])
		log.info "Calculando aguinaldo para $ids.size empleados del ejercicio $ejercicio"
		ids.each{ 
			def empleado=Empleado.get(it)
			try {
				def aguinaldo=Aguinaldo.findOrCreateWhere(ejercicio: ejercicio,empleado:empleado)
				if(aguinaldo.id==null){
					def periodo=Periodo.getPeriodoAnual(ejercicio)
					aguinaldo.fechaInicial=periodo.fechaInicial
					aguinaldo.fechaFinal=periodo.fechaFinal
				}

				calcular(aguinaldo)
			}
			catch(Exception e) {
				log.error "Error calculando aguinaldo de $empleado ($ejercicio)",e
			}
			
		}
	}


    def calcular(Aguinaldo aguinaldo) {
    	log.info "Calculando aguinaldo: "+aguinaldo
		aguinaldo.salario=aguinaldo.empleado.salario.salarioDiario
		aguinaldo.diasParaAguinaldo=aguinaldo.diasDelEjercicio-aguinaldo.faltas-aguinaldo.incapacidades
		def factor=(aguinaldo.diasDeAguinaldo/aguinaldo.diasDelEjercicio)*aguinaldo.diasParaAguinaldo
		aguinaldo.aguinaldo=factor*aguinaldo.salario
		
		log.info "Aguinaldo factor: ${factor} Salario:${aguinaldo.empleado.salario.salarioDiario} Aguinaldo:${aguinaldo.aguinaldo}"
    	
		aguinaldo.diasParaBono=aguinaldo.diasDelEjercicio-aguinaldo.faltas-aguinaldo.incapacidades-aguinaldo.permisoEspecial
		def factorBono=(aguinaldo.diasDeBono/aguinaldo.diasDelEjercicio)*aguinaldo.diasParaBono
		aguinaldo.bono=factorBono*aguinaldo.salario
		
		aguinaldo.save failOnError:true
    }
}
