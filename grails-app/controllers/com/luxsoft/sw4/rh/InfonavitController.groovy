package com.luxsoft.sw4.rh

import grails.plugin.springsecurity.annotation.Secured
import com.luxsoft.sw4.*

import grails.transaction.Transactional

@Secured(['ROLE_ADMIN'])
class InfonavitController {
    static scaffold = true
	
	def infonavitService
	
	def index(Long max){
		params.max = Math.min(max ?: 1000, 10000)
		def tipo=params.tipo?:'SEMANAL'
		params.sort=params.sort?:'lastUpdated'
		params.order='desc'
		[infonavitInstanceList:Infonavit.list(params)]
		
	}
	
	def calcularCuotaBimestral(Infonavit infonavit){
		def ejercicio=session.ejercicio
		def bimestre=Bimestre.getCurrentBimestre()
		//infonavitService.calcularCuota(infonavit,ejercicio,bimestre)
		flash.message="C�lculo de bimestre $bimestre actualizado"
		redirect action:'show',params:[id:infonavit.id]
	}
	
	def calcularBimestre(){
		def ejercicio=session.ejercicio
		def bimestre=Bimestre.getCurrentBimestre()
		def list=Infonavit.findAll{activo==true}
		list.each{
			infonavitService.calcularCuota(it,ejercicio,bimestre)
		}
		flash.message="Actualizaci�n general exitosa"
		redirect action:'index'
	}
	
	def show(Infonavit infonavitInstance){
		
		def ejercicio=session.ejercicio
		def abonos=infonavitService.geAcumuladoActual(infonavitInstance,ejercicio)
		log.info "Ejercicio $ejercicio $infonavitInstance.empleado $abonos"
		[infonavitInstance:infonavitInstance,abonos:abonos]	
	}
	
	def save(Infonavit infonavitInstance){
		log.info 'Alta de nuevo credito infonavit: '+infonavitInstance
		def bimestre=Bimestre.getCurrentBimestre()
		// infonavitInstance.validate()
		// if(infonavitInstance.hasErrors()){
		// 	println 'Infonavit Errores: '+infonavitInstance.errors
		// 	render view:'create' ,model:[infonavitInstance:infonavitInstance]
		// 	return
		// }
		infonavitInstance.comentario=infonavitInstance.comentario?:''
		infonavitInstance=infonavitService.altaDeCuota(infonavitInstance, session.ejercicio, bimestre)
		flash.message="Credito infonavit generado :"+infonavitInstance.id
		redirect action:'show',params:[id:infonavitInstance.id]
	}
	
	@Transactional
	def update(Infonavit infonavitInstance){
		log.info 'Actualizacion credito infonavit: '+infonavitInstance
		def bimestre=Bimestre.getCurrentBimestre()
		
		// infonavitInstance.validate()
		// if(infonavitInstance.hasErrors()){
		// 	render view:'edit' ,model:[infonavitInstance:infonavitInstance]
		// 	return
		// }
		//infonavitService.altaDeCuota(infonavitInstance, session.ejercicio, bimestre)
		infonavitInstance.comentario=infonavitInstance.comentario?:''
		infonavitInstance=infonavitService.altaDeCuota(infonavitInstance, session.ejercicio, bimestre)
		//infonavitInstance.save()
		flash.message="Credito infonavit modificado :"+infonavitInstance
		redirect action:'show',params:[id:infonavitInstance.id]
	}
	
	def editarBitacora(Infonavit infonavitInstance){
		[infonavitInstance:infonavitInstance]
	}
	
	def updateBitacora(Infonavit infonavitInstance){
		infonavitInstance.validate()
		if(infonavitInstance.hasErrors()){
			render view:'editarBitacora' ,model:[infonavitInstance:infonavitInstance]
		}
		infonavitInstance.save failOnError:true
		flash.message="Credito infonavit actualizado :"+infonavitInstance
		redirect action:'show',params:[id:infonavitInstance.id]
	}

	def delete(Infonavit infonavitInstance){
		infonavitInstance.delete flush:true
		flash.message='Registro de prestamo Infonavit eliminado: '+infonavitInstance.id
		redirect action:'index'
	}
}
