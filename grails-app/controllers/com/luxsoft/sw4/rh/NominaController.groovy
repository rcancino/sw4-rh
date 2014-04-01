package com.luxsoft.sw4.rh



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class NominaController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 20, 100)
    }

    def show(Long id) {
		def nominaInstance=Nomina.get(id)
		        
    }

    def create() {
        respond new Nomina(params)
    }

    @Transactional
    def save(Nomina nominaInstance) {
        if (nominaInstance == null) {
            notFound()
            return
        }

        if (nominaInstance.hasErrors()) {
            respond nominaInstance.errors, view:'create'
            return
        }

        nominaInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'nominaInstance.label', default: 'Nomina'), nominaInstance.id])
                redirect nominaInstance
            }
            '*' { respond nominaInstance, [status: CREATED] }
        }
    }

    def edit(Nomina nominaInstance) {
        respond nominaInstance
    }

    @Transactional
    def update(Nomina nominaInstance) {
        if (nominaInstance == null) {
            notFound()
            return
        }

        if (nominaInstance.hasErrors()) {
            respond nominaInstance.errors, view:'edit'
            return
        }

        nominaInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Nomina.label', default: 'Nomina'), nominaInstance.id])
                redirect nominaInstance
            }
            '*'{ respond nominaInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Nomina nominaInstance) {

        if (nominaInstance == null) {
            notFound()
            return
        }

        nominaInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Nomina.label', default: 'Nomina'), nominaInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'nominaInstance.label', default: 'Nomina'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
