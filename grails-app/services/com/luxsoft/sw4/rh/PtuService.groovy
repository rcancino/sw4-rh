package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import com.luxsoft.sw4.Periodo

@Transactional
class PtuService {
	

	def save(Ptu ptu){
        ptu?.partidas?.clear()
        def empleados=buscarEmpleadosDelEjercicio ptu.ejercicio
        empleados.each{
        	def det=actualizarPercepciones(new PtuDet(empleado:it,noAsignado:false),ptu.ejercicio)
        	ptu.addToPartidas(det)
        }
        ptu.save failOnError:true
        return ptu
	}

	def buscarEmpleadosDelEjercicio(def ejercicio){
		def list=NominaPorEmpleado
            .executeQuery("select distinct n.empleado from NominaPorEmpleado n where n.nomina.ejercicio=?",ejercicio)
        return list
	}
    

    def actualizarPercepciones(PtuDet ptuDet,def ejercicio){
    	
    	def SALARIO='P001'
		def VACACIONES='P025'
		def COMISION='P029'
		def RETARDOS ='D012'
    	def movimientos=NominaPorEmpleadoDet
    		.executeQuery(
    			"from NominaPorEmpleadoDet det where det.parent.nomina.ejercicio=? "
    			+" and det.parent.empleado=? "
    			+" and det.concepto.clave in(?,?,?,?)"
    			,[ejercicio,ptuDet.empleado
    			,SALARIO,VACACIONES,COMISION,RETARDOS
    			])
    	ptuDet.salario=movimientos.sum(0.0){it.concepto.clave==SALARIO?it.getTotal():0.0}
    	ptuDet.vacaciones=movimientos.sum(0.0){it.concepto.clave==VACACIONES?it.getTotal():0.0}
    	ptuDet.comisiones=movimientos.sum(0.0){it.concepto.clave==COMISION?it.getTotal():0.0}
    	ptuDet.retardos=movimientos.sum(0.0){it.concepto.clave==RETARDOS?it.getTotal():0.0}
    	ptuDet.total=(ptuDet.salario+ptuDet.vacaciones+ptuDet.comisiones)-ptuDet.retardos
    	log.info " ptuDet $ptuDet.empleado( $ejercicio ) actualizado"
    	return ptuDet
    }
}
