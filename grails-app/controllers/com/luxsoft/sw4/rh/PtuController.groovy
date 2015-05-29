package com.luxsoft.sw4.rh

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.transaction.NotTransactional
import grails.plugin.springsecurity.annotation.Secured
import grails.converters.JSON
import grails.validation.Validateable
import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef
import org.apache.commons.lang.WordUtils
import com.luxsoft.sw4.cfdi.ImporteALetra

@Transactional(readOnly = true)
@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
class PtuController {

    //static allowedMethods = [save: "POST", update: "PUT"]
    def ptuService
    def jasperService

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

    @NotTransactional
    def recalcularPagoDeBajas(Ptu ptuInstance){
        if (ptuInstance == null) {
            notFound()
            return
        }
        def partidas=ptuInstance.partidas.grep {it.empleado.status=='BAJA' && !it.noAsignado && !it.nominaPorEmpleado}
        partidas.each{
            log.info('Actualizando pago para: '+it.empleado)
            ptuService.calcularPago(it)
            it.save flush:true
        }
        //ptuInstance.save flush:true
        //redirect action:'show',params:[id:ptuInstance.id]
        redirect action:'asignacionCalendario' ,id:ptuInstance.id
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
        def calendarios=CalendarioDet
            .findAll('from CalendarioDet d where d.calendario.comentario=? and d.calendario.ejercicio=? and d.folio>1'
                ,['PTU',ptuInstance.ejercicio+1])
        //println 'Calendarios: '+calendarios.size()+ " Ptu ejercicio: "+ptuInstance.ejercicio
        [ptuInstance:ptuInstance,partidas:partidas,calendarios:calendarios]
    }

    @Transactional
    def asignarCalendario(Ptu ptuInstance){
        def calendarioDet=CalendarioDet.get(params.calendarioDet)
        assert calendarioDet,'No se definio ningun calendario'
        println "Asignando calendario $calendarioDet BAJAS de PTU: "+ptuInstance.id
        println 'params: '+params
        params.partidas.each{
            def det=PtuDet.get(it)
            det.calendarioDet=calendarioDet
            det.save flush:true
        }
        redirect action:'asignacionCalendario' ,id:ptuInstance.id

    }

    def recibosDePTU(NominaCommand command){
       
        Nomina n =command.nomina
        def reportes=[]
        n.partidas.sort{it.orden}.each{ nominaPorEmpleado->
                def repParams=[:]
                repParams['NE_ID']=nominaPorEmpleado.id
                PtuDet ptu=PtuDet.findByNominaPorEmpleado(nominaPorEmpleado)

               // repParams['IMPORTE_LETRA']=  com.luxsoft.sw4.cfdi.ImporteALetra.aLetra(ptu.montoDias+ptu.montoSalario)
                 repParams['IMPORTE_LETRA']=  com.luxsoft.sw4.cfdi.ImporteALetra.aLetra(ptu.montoDias+ptu.montoSalario)

                def reportDef=new JasperReportDef(
                    name:'ReciboPTU'
                    ,fileFormat:JasperExportFormat.PDF_FORMAT
                    ,parameters:repParams
                    )
                reportes.add(reportDef)
        }

    ByteArrayOutputStream  pdfStream=jasperService.generateReport(reportes)
        def fileName="nomina_${n.ejercicio}_${n.periodicidad}_${n.folio}_${n.tipo}.pdf"
        render(file: pdfStream.toByteArray(), contentType: 'application/pdf',fileName:fileName)

    }


   

 
}

// @Validateable
// class NominaCommand{
//     Nomina nomina   
//     static constraints={
//          nomina nullable:false
//     }
// }
