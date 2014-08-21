package com.luxsoft.sw4.rh

import org.springframework.security.access.annotation.Secured
import grails.transaction.Transactional

@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
@Transactional(readOnly = true)
class CalendarioDetController {
    static scaffold = true

    def show(CalendarioDet calendarioDetInstance){
    	redirect controller:'calendario',action:'edit',params:[id:calendarioDetInstance.calendario.id]
    }
}
