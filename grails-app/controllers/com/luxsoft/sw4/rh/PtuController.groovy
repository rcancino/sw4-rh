package com.luxsoft.sw4.rh

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.transaction.NotTransactional
import grails.plugin.springsecurity.annotation.Secured

@Transactional(readOnly = true)
@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
class PtuController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    def ptuService

    def index() {
        respond Ptu.findAll()
    }

    def show(Ptu ptuInstance) {
        respond ptuInstance
    }

    def create(){
        [ptuInstance:new Ptu(ejercicio:session.ejercicio-1)]
    }

    @Transactional
    def save(Ptu ptuInstance) {
        if (ptuInstance == null) {
            notFound()
            return
        }

        if (ptuInstance.hasErrors()) {
            respond ptuInstance.errors, view:'create'
            return
        }

        ptuInstance=ptuService.save ptuInstance

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'ptuInstance.label', default: 'Ptu'), ptuInstance.id])
                redirect ptuInstance
            }
            '*' { respond ptuInstance, [status: CREATED] }
        }
    }

    def edit(Ptu ptuInstance) {
        respond ptuInstance
    }

    @Transactional
    def update(Ptu ptuInstance) {
        if (ptuInstance == null) {
            notFound()
            return
        }

        if (ptuInstance.hasErrors()) {
            respond ptuInstance.errors, view:'edit'
            return
        }

        ptuInstance=ptuService.save ptuInstance

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Ptu.label', default: 'Ptu'), ptuInstance.id])
                redirect ptuInstance
            }
            '*'{ respond ptuInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Ptu ptuInstance) {

        if (ptuInstance == null) {
            notFound()
            return
        }

        ptuInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Ptu.label', default: 'Ptu'), ptuInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'ptuInstance.label', default: 'Ptu'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
