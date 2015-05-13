package com.luxsoft.sw4.rh

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.transaction.NotTransactional
import grails.plugin.springsecurity.annotation.Secured
import grails.converters.JSON

@Transactional(readOnly = true)
@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
class PtuController {

    //static allowedMethods = [save: "POST", update: "PUT"]
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
        ptuInstance.montoDias=0.0
        ptuInstance.montoSalario=0.0
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
        redirect action:"index"
        // request.withFormat {
        //     form multipartForm {
        //         flash.message = message(code: 'default.deleted.message', args: [message(code: 'Ptu.label', default: 'Ptu'), ptuInstance.id])
        //         redirect action:"index", method:"GET"
        //     }
        //     '*'{ render status: NO_CONTENT }
        // }
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

    @NotTransactional
    def recalcular(Ptu ptuInstance){
        if (ptuInstance == null) {
            notFound()
            return
        }
        ptuInstance=ptuService.recalcular(ptuInstance)
        //respond  ptuInstance,view:'show'
        redirect action:'show',params:[id:ptuInstance.id]
    }

    

    @NotTransactional
    def recalcularPago(Ptu ptuInstance){
        if (ptuInstance == null) {
            notFound()
            return
        }
        ptuInstance.partidas.each{
            log.info('Actualizando pago para: '+it.empleado)
            ptuService.calcularPago(it)
        }
        ptuInstance.save flush:true
        redirect action:'show',params:[id:ptuInstance.id]
    }

    def getPartidas(Ptu ptuInstance){
        def data=ptuInstance.partidas.collect{
            [nombre:it.empleado.nombre,
             ubicacion:it.empleado.perfil.ubicacion.clave,
             salario:it.salario,
             ptu:it]
        }
        render data as JSON
    }

    def asignacionCalendario(Ptu ptuInstance){
        def partidas=ptuInstance.partidas.grep {it.empleado.status=='BAJA' && !it.noAsignado}
        [ptuInstance:ptuInstance,partidas:partidas]
    }
}
