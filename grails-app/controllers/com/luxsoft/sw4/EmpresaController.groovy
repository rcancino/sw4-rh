package com.luxsoft.sw4

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import org.springframework.security.access.annotation.Secured
import grails.validation.Validateable


@Secured(["hasAnyRole('ROLE_ADMIN')"])
@Transactional(readOnly = true)
class EmpresaController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    
    def indx(){
    	redirect action:'edit'
    }

    def edit() {
    	Empresa empresaInstance = Empresa.first()
        [empresaInstance:empresaInstance]
    }
    

    @Transactional
    @Secured(["hasAnyRole('ADMIN')"])
    def update(Empresa empresaInstance) {
        if (empresaInstance == null) {
            notFound()
            return
        }

        if (empresaInstance.hasErrors()) {
            respond empresaInstance.errors, view:'edit'
            return
        }

        empresaInstance = empresaInstance.save flush:true
        render view:'edit', model:[empresaInstance:empresaInstance]
        
    }

   

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'empresa.label', default: 'Empresa'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
    

    @Transactional
    def registrarLlavePrivada(Empresa empresaInstance) {
        if (empresaInstance == null) {
            notFound()
            return
        }
        def file=request.getFile('file')
        empresaInstance.llavePrivada=file.getBytes()
        empresaInstance.save flush:true
        redirect action: 'edit'
        
    }

    @Transactional
    def registrarCertificado(Empresa empresaInstance) {
        if (empresaInstance == null) {
            notFound()
            return
        }
        def file=request.getFile('file')
        
        empresaInstance.numeroDeCertificado=file.getOriginalFilename()-'.cer'
        empresaInstance.certificadoDigital=file.getBytes()
        empresaInstance.save flush:true
        redirect action: 'edit'
        
    }

    
}

