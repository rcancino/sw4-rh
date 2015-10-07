package com.luxsoft.sw4.rh



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class AsistenciaImssDetController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond AsistenciaImssDet.list(params), model:[asistenciaImssDetInstanceCount: AsistenciaImssDet.count()]
    }

    def show(AsistenciaImssDet asistenciaImssDetInstance) {
        respond asistenciaImssDetInstance
    }

    def create() {
        respond new AsistenciaImssDet(params)
    }

    @Transactional
    def save(AsistenciaImssDet asistenciaImssDetInstance) {
        if (asistenciaImssDetInstance == null) {
            notFound()
            return
        }

        if (asistenciaImssDetInstance.hasErrors()) {
            respond asistenciaImssDetInstance.errors, view:'create'
            return
        }

        asistenciaImssDetInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'asistenciaImssDetInstance.label', default: 'AsistenciaImssDet'), asistenciaImssDetInstance.id])
                redirect asistenciaImssDetInstance
            }
            '*' { respond asistenciaImssDetInstance, [status: CREATED] }
        }
    }

    def edit(AsistenciaImssDet asistenciaImssDetInstance) {
        respond asistenciaImssDetInstance
    }

    @Transactional
    def update(AsistenciaImssDet asistenciaImssDetInstance) {
        if (asistenciaImssDetInstance == null) {
            notFound()
            return
        }

        if (asistenciaImssDetInstance.hasErrors()) {
            respond asistenciaImssDetInstance.errors, view:'edit'
            return
        }

        asistenciaImssDetInstance.save flush:true
        flash.message = message(code: 'default.updated.message', args: [message(code: 'AsistenciaImssDet.label', default: 'AsistenciaImssDet'), asistenciaImssDetInstance.id])
        redirect controller:'asistenciaImss', action:'show',id:asistenciaImssDetInstance.asistenciaImss.id
        
    }

    @Transactional
    def delete(AsistenciaImssDet asistenciaImssDetInstance) {

        if (asistenciaImssDetInstance == null) {
            notFound()
            return
        }

        asistenciaImssDetInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'AsistenciaImssDet.label', default: 'AsistenciaImssDet'), asistenciaImssDetInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'asistenciaImssDetInstance.label', default: 'AsistenciaImssDet'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
