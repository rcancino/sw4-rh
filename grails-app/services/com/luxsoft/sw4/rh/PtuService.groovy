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
        recalcular ptu
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

    def recalcular(Ptu ptu){
        def tope=ptu.getSalarioTope()
        ptu.partidas.each{
            it.topeAnual=it.total>tope?tope:it.total
            it.diasDelEjercicio=calcularDiasDelEjercicio it
            it.diasPtu=it.diasDelEjercicio-it.faltas-it.incapacidades-it.permisosP
            it.noAsignado=it.diasPtu<60 
            if(it.empleado.id==260 || it.empleado.id==280){
                it.noAsignado=true
            }
        }

        ptu.montoDias=ptu.total*0.5
        ptu.montoSalario=ptu.total-ptu.montoDias
        ptu.diasPtu=ptu.partidas.sum 0,{
            if(!it.noAsignado) 
                it.diasPtu
            else
                0
        }
        ptu.topeAnualAcumulado=ptu.partidas.sum 0.0,{
            if(!it.noAsignado) 
                it.topeAnual
            else
                0
        }
        ptu.factorDias=ptu.montoDias/ptu.diasPtu
        ptu.factorSalario=ptu.montoSalario/ptu.topeAnualAcumulado
        ptu.partidas.each{
            it.montoDias=it.diasPtu*ptu.factorDias
            it.montoSalario=it.topeAnual*ptu.factorSalario
        }
        ptu.save flush:true
        return ptu
    }


    def calcularDiasDelEjercicio(PtuDet ptuDet){

        def periodo=ptuDet.getPeriodo()
        def alta=ptuDet.empleado.alta
        def baja=ptuDet.empleado?.baja?.fecha

        def de=0
        if(!baja){
          if(alta<periodo.fechaInicial){
            
            de=periodo.fechaFinal-periodo.fechaInicial+1
          }else{
            de=periodo.fechaFinal-alta+1
          }
        }else{
          if(baja<periodo.fechaInicial && (baja>alta)){
            de=0
          }else{
            def fechaSuperior=(baja<periodo.fechaFinal && baja>alta)?baja:periodo.fechaFinal
            def fechaDeInicio=alta<periodo.fechaInicial?periodo.fechaInicial:alta
            de=fechaSuperior-fechaDeInicio+1
          }
        }
    }

    // def actualizarAsistencia(Ptu ptu){
    //     if(ptu.ejercicio){
    //         def file=grailsApplication.mainContext.getResource("/WEB-INF/data/PrestamosINFONAVIT2.csv").file
    //          file.eachLine{line,row ->
    //             if(row>1){
    //                 def fields=line.split(",")
    //                 def clave=fields[0].split(" ")
    //                 def empleado=Empleado.findByClave(clave)
    //                 def tipo=fields[1]
    //             }
    //         }
    //     }
    // }
}
