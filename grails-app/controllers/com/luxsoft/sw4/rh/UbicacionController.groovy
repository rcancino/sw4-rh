package com.luxsoft.sw4.rh

import grails.plugin.springsecurity.annotation.Secured;


@Secured(['ROLE_ADMIN','RH_USER'])
class UbicacionController {
    static scaffold = true
}
