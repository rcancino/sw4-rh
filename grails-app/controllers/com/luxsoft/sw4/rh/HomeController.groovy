package com.luxsoft.sw4.rh

import org.springframework.security.access.annotation.Secured

//@Secured(['ROLE_USER'])
class HomeController {

	@Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def index() { 
		if(grailsApplication.config.grails.plugin.springsecurity.active == true){
			if(!isLoggedIn()){
				redirect (controller:'login')
			}
		}
    	
    }

    def cambioDeEjercicio(Integer ejercicio){
    	session.ejercicio=ejercicio
    }
	
	def cambiarCalendario(CalendarioDet calendario){
		if(calendario.calendario.tipo=='SEMANAL'){
			session.calendarioSemana=calendarioDet
		}else{
			session.calendarioQuincena=calendarioDet
		}
	}
}
