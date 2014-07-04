package com.luxsoft.sw4.rh

import grails.plugin.springsecurity.annotation.Secured


@Secured(['ROLE_ADMIN'])
class AsistenciaDetController {
	
	static scaffold=true
	
   
}
