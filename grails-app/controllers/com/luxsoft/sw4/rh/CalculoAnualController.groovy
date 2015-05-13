package com.luxsoft.sw4.rh

import com.luxsoft.sw4.*
import grails.plugin.springsecurity.annotation.Secured
import grails.converters.JSON
import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef

@Secured(['ROLE_ADMIN'])
class CalculoAnualController {

	def calculoAnualService

    def jasperService

    def index(){
    	def ejercicio=session.ejercicio
    	def list=CalculoAnual.findAll("from CalculoAnual a where a.ejercicio=?",[ejercicio])
        list=list.sort{a,b ->
            a.empleado.perfil.ubicacion.clave<=>b.empleado.perfil.ubicacion.clave?:a.empleado.nombre<=>b.empleado.nombre
        }
    	[calculoAnualInstanceList:list]
    }

    def edit(CalculoAnual calculoAnualInstance){
        [calculoAnualInstance:calculoAnualInstance]
    }

    def actualizar(){
    	def ejercicio=session.ejercicio
    	calculoAnualService.calcular(ejercicio)
    	redirect action:'index'
    }
	
	def recalcular(CalculoAnual c){
		c=calculoAnualService.calcular(c.empleado,c.ejercicio)
		redirect action:'edit',params:[id:c.id]
		
	}


    def reporte(){
		def tipo=params.tipo
		def re=''
		switch(tipo) {
			case 'ACUMULADOS':
				re='CalculoAnualIngresosAcu'
				break;
			case 'RESULTADOS':
			
				re='CalculoAnualResultados'
				break;
				
			case 'SALDOS':
				
				re='CalculoAnualSaldosRH'
				break;
			
			
			break
		}
		if(re){
			params.reportName=re
			params['EJERCICIO']=session.ejercicio
		
			ByteArrayOutputStream  pdfStream=runReport(params)
			render(file: pdfStream.toByteArray(), contentType: 'application/pdf',fileName:params.reportName+'.pdf')
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
