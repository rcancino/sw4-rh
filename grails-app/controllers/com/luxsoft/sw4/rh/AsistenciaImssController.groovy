package com.luxsoft.sw4.rh



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.plugin.springsecurity.annotation.Secured
import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef

@Transactional(readOnly = true)
@Secured(['ROLE_ADMIN'])
class AsistenciaImssController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def asistenciaImssService


    def index() {
        def calendarioDet
        def tipo=params.tipo?:'SEMANA'
        switch (tipo) {
            case 'QUINCENA':
                calendarioDet=session.calendarioQuincena
                break
            case 'SEMANA':
                calendarioDet=session.calendarioSemana
                break
        }
        calendarioDet.attach()
        def list=AsistenciaImss.findAll("from AsistenciaImss a where a.asistencia.calendarioDet=?"
                ,[calendarioDet])
        def ejercicio=session.ejercicio
        def periodos=CalendarioDet.findAll{calendario.ejercicio==ejercicio && calendario.tipo==tipo}
        [asistenciaImssInstanceList:list,calendarioDet:calendarioDet,periodos:periodos]
        
    }

    def actualizarCalendario(CalendarioDet cal){
        if(cal.calendario.tipo=='SEMANA')
            session.calendarioSemana=cal
        else
            session.calendarioQuincena=cal
        redirect action:'index',params:[tipo:cal.calendario.tipo]
    }
    
    @Transactional
    def generar(CalendarioDet calendarioDetInstance){
        log.info 'Generando asistencia imss para '+calendarioDetInstance.id
        asistenciaImssService.generar(calendarioDetInstance)
        redirect action:'index',params:[tipo:calendarioDetInstance.calendario.tipo]
    }

    @Transactional
    def actualizar(AsistenciaImss asistenciaImssInstance){
        log.debug 'Actualizando asistencia imss: '+asistenciaImssInstance
        asistenciaImssInstance=asistenciaImssService.actualizar(asistenciaImssInstance)
        flash.message="Asistencia IMSS actualizada"
        redirect action:'show',id:asistenciaImssInstance.id
    }  

    def show(AsistenciaImss asistenciaImssInstance) {
        def fechas=new ArrayList((asistenciaImssInstance.calendarioDet.inicio..asistenciaImssInstance.calendarioDet.fin) )
        fechas=fechas.sort().reverse()
        respond asistenciaImssInstance,model:[fechas:fechas]
    }

    @Transactional
    def deleteAll(CalendarioDet calendarioDetInstance){
        asistenciaImssService.eliminar(calendarioDetInstance)
        flash.message="Registros del ${calendarioDetInstance.folio} eliminados"
        redirect action:'index',id:calendarioDetInstance.id
    }

    def create() {
        respond new AsistenciaImss(params)
    }

    @Transactional
    def save(AsistenciaImss asistenciaImssInstance) {
        if (asistenciaImssInstance == null) {
            notFound()
            return
        }

        if (asistenciaImssInstance.hasErrors()) {
            respond asistenciaImssInstance.errors, view:'create'
            return
        }

        asistenciaImssInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'asistenciaImssInstance.label', default: 'AsistenciaImss'), asistenciaImssInstance.id])
                redirect asistenciaImssInstance
            }
            '*' { respond asistenciaImssInstance, [status: CREATED] }
        }
    }

    def edit(AsistenciaImss asistenciaImssInstance) {
        respond asistenciaImssInstance
    }

    @Transactional
    def update(AsistenciaImss asistenciaImssInstance) {
        if (asistenciaImssInstance == null) {
            notFound()
            return
        }

        if (asistenciaImssInstance.hasErrors()) {
            respond asistenciaImssInstance.errors, view:'edit'
            return
        }

        asistenciaImssInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'AsistenciaImss.label', default: 'AsistenciaImss'), asistenciaImssInstance.id])
                redirect asistenciaImssInstance
            }
            '*'{ respond asistenciaImssInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(AsistenciaImss asistenciaImssInstance) {

        if (asistenciaImssInstance == null) {
            notFound()
            return
        }

        asistenciaImssInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'AsistenciaImss.label', default: 'AsistenciaImss'), asistenciaImssInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }


def jasperService
    
    def reporteAusentismoSua(){
        def reportDef = new JasperReportDef(
            name:'AusentismoSua',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            parameters:params
            )
    }




    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'asistenciaImssInstance.label', default: 'AsistenciaImss'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
