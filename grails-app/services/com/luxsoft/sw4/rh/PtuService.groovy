package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import com.luxsoft.sw4.Periodo

@Transactional
class PtuService {


	

	def generar(Integer ejercicio,Empleado empleado){
		
		def ptu=Ptu.findByEjercicioAndEmpleado(ejercicio,empleado)
		if(!ptu){
			def periodo=Periodo.getPeriodoAnual(ejercicio)

			ptu=new Ptu(
				ejercicio:ejercicio,
				empleado:empleado,
				fechaInicial:periodo.fechaInicial,
				fechaFinal:periodo.fechaFinal
				)
			ptu.save flush:true
			
		}
		actualizarPercepciones ptu

	}

    

    def actualizarPercepciones(Ptu ptu){
    	def SALARIO='P001'
		def VACACIONES='P025'
		def COMISION='P029'
		def RETARDOS ='D012'
    	def movimientos=NominaPorEmpleadoDet
    		.executeQuery(
    			"from NominaPorEmpleadoDet det where det.parent.nomina.ejercicio=? "
    			+" and det.parent.empleado=? "
    			+" and det.concepto.clave in(?,?,?,?)"
    			,[ptu.ejercicio,ptu.empleado
    			,SALARIO,VACACIONES,COMISION,RETARDOS
    			])
    	ptu.salario=movimientos.sum(0.0){it.concepto.clave==SALARIO?it.getTotal():0.0}
    	ptu.vacaciones=movimientos.sum(0.0){it.concepto.clave==VACACIONES?it.getTotal():0.0}
    	ptu.comisiones=movimientos.sum(0.0){it.concepto.clave==COMISION?it.getTotal():0.0}
    	ptu.retardos=movimientos.sum(0.0){it.concepto.clave==RETARDOS?it.getTotal():0.0}
    	ptu.total=(ptu.salario+ptu.vacaciones+ptu.comisiones)-ptu.retardos
    	ptu.save failOnError:true,flush:true
    	log.info " PTU $ptu.empleado( $ptu.ejercicio ) actualizado"
    }
}
