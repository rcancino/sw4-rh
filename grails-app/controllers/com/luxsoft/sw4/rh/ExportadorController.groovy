package com.luxsoft.sw4.rh


import grails.plugin.springsecurity.annotation.Secured;
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN','RH_USER'])
@Transactional(readOnly = true)
class ExportadorController {

    def index() { }
}
