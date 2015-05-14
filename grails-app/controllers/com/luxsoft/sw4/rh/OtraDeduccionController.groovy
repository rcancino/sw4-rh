package com.luxsoft.sw4.rh

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN','RH_USER'])
class OtraDeduccionController {
    static scaffold = true
	
	
	def index(Long max){
    	params.max = Math.min(max ?: 15, 100)
		params.sort=params.sort?:'lastUpdated'
		params.order='desc'
		[otraDeduccionInstanceList:OtraDeduccion.list(params)
		,otraDeduccionInstanceListTotal:OtraDeduccion.count()]
    }
	
	def show(OtraDeduccion otraDeduccionInstance){
		//chain action:'edit',params:[id:otraDeduccionInstance.id]
		redirect action:'index'
	}
	
	def delete(OtraDeduccion otraDeduccionInstance){
		otraDeduccionInstance.delete flush:true
		flash.message="Otra deduccion eliminada "+otraDeduccionInstance.id
		redirect action:'index'
	}


	def eliminarPartida(OtraDeduccionAbono abono){
		OtraDeduccion o=abono.otraDeduccion
		o.removeFromAbonos(abono)
		o.actualizarSaldo()
		o.save flush:true
		flash.message="Abono  eliminada "+abono.id
		redirect action:'edit',params:[id:o.id]
	}

	def update(OtraDeduccion otraDeduccionInstance){
		otraDeduccionInstance.actualizarSaldo()
		otraDeduccionInstance.save flush:true
		redirect action:'edit',params:[id:otraDeduccionInstance.id]
	}

	def agregarAbono(OtraDeduccion otraDeduccionInstance){
		[otraDeduccionInstance:otraDeduccionInstance,otraDeduccionAbonoInstance:new OtraDeduccionAbono()]
		
	}
	
	@Transactional
	def salvarAbono(OtraDeduccionAbono abono){
		def otraDeduccion=OtraDeduccion.get(params.otraDeduccionId)
		otraDeduccion.addToAbonos(abono)
		abono.validate()
		if(abono.hasErrors()){
			flash.message="Error de validacion en abono: "
			render view:'agregarAbono',model:[otraDeduccionInstance:otraDeduccion,otraDeduccionAbonoInstance:abono]
			return
		}
		
		otraDeduccion.save(flush:true)
		redirect action:'edit',params:[id:otraDeduccion.id]
	}
}
