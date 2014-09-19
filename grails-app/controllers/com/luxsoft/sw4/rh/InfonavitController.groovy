package com.luxsoft.sw4.rh

import grails.plugin.springsecurity.annotation.Secured
import com.luxsoft.sw4.*

@Secured(['ROLE_ADMIN'])
class InfonavitController {
    static scaffold = true
	
	def infonavitService
	
	def index(Long max){
		params.max = Math.min(max ?: 60, 100)
		def tipo=params.tipo?:'SEMANAL'
		params.sort=params.sort?:'lastUpdated'
		params.order='desc'
		[infonavitInstanceList:Infonavit.list(params)]
		
	}
	
	def calcularCuotaBimestral(Infonavit infonavit){
		def ejercicio=session.ejercicio
		def bimestre=Bimestre.getCurrentBimestre()
		//infonavitService.calcularCuota(infonavit,ejercicio,bimestre)
		flash.message="Cálculo de bimestre $bimestre actualizado"
		redirect action:'show',params:[id:infonavit.id]
	}
	
	def calcularBimestre(){
		def ejercicio=session.ejercicio
		def bimestre=Bimestre.getCurrentBimestre()
		def list=Infonavit.findAll{activo==true}
		list.each{
			infonavitService.calcularCuota(it,ejercicio,bimestre)
		}
		flash.message="Actualización general exitosa"
		redirect action:'index'
	}
	
	def show(Infonavit infonavitInstance){
		log.info 'Probando .....'
		def ejercicio=session.ejercicio
		def abonos=infonavitService.geAcumuladoActual(infonavitInstance,ejercicio)
		[infonavitInstance:infonavitInstance,abonos:abonos]	
	}
}
