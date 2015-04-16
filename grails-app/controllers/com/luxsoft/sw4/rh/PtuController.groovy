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

    def index(Integer max) {
        def ejercicio =session.ejercicio
        //params.max = Math.min(max ?: 20, 100)
        def list =Ptu.findAll("from Ptu p where p.ejercicio=? "+
            " order by p.empleado.perfil.ubicacion.clave, p.empleado.apellidoPaterno"
            ,[ejercicio])
        respond list, model:[ptuInstanceCount: Ptu.countByEjercicio(ejercicio)]
    }

    def show(Ptu ptuInstance) {
        respond ptuInstance
    }

    @NotTransactional
    def generar(){
        def ejercicio=session.ejercicio
        def list=NominaPorEmpleado
            .executeQuery("select distinct n.empleado from NominaPorEmpleado n where n.nomina.ejercicio=?",ejercicio)
        list.each{
            ptuService.generar(ejercicio,it)
        }       
        redirect action:"index"
        
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

        ptuInstance.save flush:true

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

        ptuInstance.save flush:true

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
