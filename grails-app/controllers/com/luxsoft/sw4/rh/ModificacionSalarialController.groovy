package com.luxsoft.sw4.rh

import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat;
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef;

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
class ModificacionSalarialController {
	
	def jasperService
    
    def index(Long max){
    	params.max = Math.min(max ?: 15, 100)
		params.sort=params.sort?:'id'
		params.order='desc'
		[modificacionInstanceList:ModificacionSalarial.list(params)
		,modificacionInstanceListTotal:ModificacionSalarial.count()]
    }

    def create(){
    	[modificacionInstance:new ModificacionSalarial(tipo:'AUMENTO')]
    }

    @Transactional
    def save(ModificacionSalarial modificacionInstance){
    	//modificacionInstance.sdiNuevo=0.0
    	modificacionInstance.validate()
    	if(modificacionInstance.hasErrors()){
    		flash.message="Errores de validacion en modificacion salarial"
    		render view:'create',model:[modificacionInstance:modificacionInstance]
    	}
		
    	modificacionInstance.save failOnError:true
    	redirect action:'show',params:[id:modificacionInstance.id]

    }
    
    @Transactional
    def delete(Long id){
        def modificacion=ModificacionSalarial.get(id)
        modificacion.delete(flush:true)
        flash.message="Modificacion salarial ${id} eliminada"
        redirect action:'index'
    }

    def show(Long id){
    	def modificacionInstance=ModificacionSalarial.get(id)
    	[modificacionInstance:modificacionInstance]
    }
	
	def reporteDeSDI(ModificacionSalarial ms){
		log.info 'Reporte de analis SDI para modificacion salarial: '+ms
		
		def parameters=['EMPLEADO_ID':ms.empleado.id]
		parameters['FECHA_INICIAL',ms.fecha.format('yyyy/MM/dd')]
		parameters['TIPO',ms.empleado.salario.periodicidad]
		parameters['SALARIO',ms.salarioAnterior]
		
		JasperReportDef reportDef=new JasperReportDef(name:'AnalisisDeSDIPorEmpleado'
			,fileFormat:JasperExportFormat.PDF_FORMAT
			,parameters:parameters)
		
		def res=jasperService.generateReport(reportDef)
		def fileName="AnalisisSDI_"+ms.id
		response.setHeader("Content-disposition", 'attachment; filename="$fileName"' );
		response.contentType = reportDef.fileFormat.mimeTyp
		response.characterEncoding = "UTF-8"
		response.outputStream << reportDef.contentStream.toByteArray()
		//chain controller:'jasper',action:'index',params:params
	}

}
