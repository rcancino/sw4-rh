package com.luxsoft.sw4.rh

import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN','RH_USER'])
class OtraDeduccionController {
    static scaffold = true
	
	def show(OtraDeduccion otraDeduccionInstance){
		//chain action:'edit',params:[id:otraDeduccionInstance.id]
		redirect action:'index'
	}
	
	def delete(OtraDeduccion otraDeduccionInstance){
		otraDeduccionInstance.delete flush:true
		flash.message="Otra deduccion eliminada "+otraDeduccionInstance.id
		redirect action:'index'
	}
}
