package com.luxsoft.sw4.rh



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.plugin.springsecurity.annotation.Secured

@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
@Transactional(readOnly = true)
class DepartamentoController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 15, 100)
        respond Departamento.list(params), model:[departamentoInstanceCount: Departamento.count()]
    }

    def show(Departamento departamentoInstance) {
        respond departamentoInstance
    }

    def create() {
        respond new Departamento(params)
    }

    @Transactional
    def save(Departamento departamentoInstance) {
        if (departamentoInstance == null) {
            notFound()
            return
        }

        if (departamentoInstance.hasErrors()) {
            respond departamentoInstance.errors, view:'create'
            return
        }

        departamentoInstance.save flush:true
		flash.message = message(code: 'default.created.message', args: [message(code: 'departamentoInstance.label', default: 'Departamento'), departamentoInstance.clave])
		
        redirect action:'index'
		/*
		request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'departamentoInstance.label', default: 'Departamento'), departamentoInstance.id])
                redirect departamentoInstance
            }
            '*' { respond departamentoInstance, [status: CREATED] }
        }*/
    }

    def edit(Departamento departamentoInstance) {
        //respond departamentoInstance
		render view:'create', model:[departamentoInstance:departamentoInstance,actionForm:'update',actionMethod:'PUT']
    }

    @Transactional
    def update(Departamento departamentoInstance) {
        if (departamentoInstance == null) {
            notFound()
            return
        }

        if (departamentoInstance.hasErrors()) {
            respond departamentoInstance.errors, view:'edit'
            return
        }

        departamentoInstance.save flush:true
		flash.message = message(code: 'default.updated.message', args: [message(code: 'Departamento.label', default: 'Departamento'), departamentoInstance.clave])
		redirect action:'index'
		/*
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Departamento.label', default: 'Departamento'), departamentoInstance.id])
                redirect departamentoInstance
            }
            '*'{ respond departamentoInstance, [status: OK] }
        }*/
    }

    @Transactional
    def delete(Departamento departamentoInstance) {

        if (departamentoInstance == null) {
            notFound()
            return
        }

        departamentoInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Departamento.label', default: 'Departamento'), departamentoInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'departamentoInstance.label', default: 'Departamento'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
