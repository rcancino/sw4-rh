package com.luxsoft.sw4.rh

import com.luxsoft.sw4.*
import grails.plugin.springsecurity.annotation.Secured
import grails.converters.JSON
import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef

@Secured(['ROLE_ADMIN'])
class AguinaldoController {

    static scaffold = true

    def aguinaldoService

    def jasperService

    def index(){
    	def ejercicio=session.ejercicio
    	def list=Aguinaldo.findAll("from Aguinaldo a where a.ejercicio=?",[ejercicio])
        list=list.sort{a,b ->
            a.empleado.perfil.ubicacion.clave<=>b.empleado.perfil.ubicacion.clave?:a.empleado.nombre<=>b.empleado.nombre
        }
    	[aguinaldoInstanceList:list]
    }

    def save(EmpleadoPorEjercicioCommand command){
        log.info 'Generando aguinaldo para '+command
        def aguinaldo=Aguinaldo.find{ejercicio==command.ejercicio && empleado==command.empleado}
        if(aguinaldo){
            flash.message="Agunaldo para $command.empleado para el ejercicio $command.ejercicio ya existe"
            render view:'create',model:[aguinaldoInstance:new EmpleadoPorEjercicioCommand(ejercicio:session.ejercicio)]
            return
        }
        aguinaldo=aguinaldoService.generar(command.empleado,command.ejercicio)
        flash:'Nuevo registro de aguinaldo generado '+aguinaldo.id
        redirect action:'index'
    }

    def create(){
        [aguinaldoInstance:new EmpleadoPorEjercicioCommand(ejercicio:session.ejercicio)]
    }

    def actualizar(){
    	def ejercicio=session.ejercicio
    	aguinaldoService.calcular(ejercicio)
    	redirect action:'index'
    }
	
	def recalcular(Aguinaldo a){
		a=aguinaldoService.calcular(a)
		redirect action:'edit',params:[id:a.id]
		
	}
	
	def editarAguinaldo(Aguinaldo a){
		a=aguinaldoService.calcular(a)
		redirect action:'edit',params:[id:a.id]
	}

    def reporte(){
        def tipo=params.tipo
        def re=''
        switch(tipo) {
            case 'BASE':
                re='AguinaldoBase'
                break;
            case 'CALCULO':
                re='AguinaldoCalculo'
                break;
            case 'IMPUESTO':
                re='AguinaldoImpuesto'
                break;
            case 'PAGO':
                re='AguinaldoPago'
                break;
            break
        }
        if(re){
            params.reportName=re
            params['EJERCICIO']=session.ejercicio
            ByteArrayOutputStream  pdfStream=runReport(params)
            render(file: pdfStream.toByteArray(), contentType: 'application/pdf',fileName:params.reportName)
        }else{
            flash.message="Reporte incorrecto: "+re
            redirect action:'index'

        }
    }

    private runReport(Map repParams){
        log.info 'Ejecutando reporte  '+repParams
        def nombre=repParams.reportName
        def reportDef=new JasperReportDef(
            name:nombre
            ,fileFormat:JasperExportFormat.PDF_FORMAT
            ,parameters:repParams
            )
        ByteArrayOutputStream  pdfStream=jasperService.generateReport(reportDef)
        return pdfStream
        
    }
}


