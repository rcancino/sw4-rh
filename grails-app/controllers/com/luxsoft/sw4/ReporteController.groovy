package com.luxsoft.sw4



import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef
import grails.validation.Validateable
import grails.plugin.springsecurity.annotation.Secured
import org.apache.commons.lang.WordUtils
import com.luxsoft.sw4.cfdi.ImporteALetra
import com.luxsoft.sw4.rh.*

@Secured(['ROLE_ADMIN','RH_USER'])
class ReporteController {
    static scaffold = true
	
	def jasperService
	
	def impuestoSobreNominas(ImpuestoSobreNominaCommand command){
		if(request.method=='GET'){
			log.info 'Forma para reporte sobre nomina'
			render view:'impuestoSobreNominas',model:[reportCommand:new ImpuestoSobreNominaCommand()]
			return
		}
		command.validate()
		if(command.hasErrors()){
			log.info 'Errores de validacion al ejecurar reporte'
			render view:'impuestoSobreNominas',model:[reportCommand:command]
			return
		}
		def repParams=[:]
		repParams<<params
		def reportDef=new JasperReportDef(
			name:'ImpuestoSobreNomina'
			,fileFormat:JasperExportFormat.PDF_FORMAT
			,parameters:repParams
			)
		ByteArrayOutputStream  pdfStream=jasperService.generateReport(reportDef)
		log.info 'Ejecutando reporte Impuesto sobre nominas '+command
		render(file: pdfStream.toByteArray(), contentType: 'application/pdf',fileName:'impuestoSobreNominas')
	}
	
	def historicoDeSalarios(){
		[reportCommand:new EmpleadoPorEjercicioCommand(ejercicio:session.ejercicio)]
	}
	
	def reportePorEmpleadoEjercicio(EmpleadoPorEjercicioCommand command){
		if(command==null){
			render 'No esta bien generado el gsp para el reporte falta el bean PorEmpleadoCommand'
		}
		command.validate()
		if(command.hasErrors()){
			log.info 'Errores de validacion al ejecurar reporte: '+ params
			render view:WordUtils.uncapitalize(params.reportName),model:[reportCommand:command]
			return [reportCommand:command]
		}
		def repParams=[:]
		repParams['EJERCICIO']=command.ejercicio as Long
		repParams['EMPLEADO_ID']=command.empleado.id as Integer
		repParams.reportName=params.reportName?:'FaltaNombre Del Reporte'
		ByteArrayOutputStream  pdfStream=runReport(repParams)
		render(file: pdfStream.toByteArray(), contentType: 'application/pdf'
			,fileName:command.empleado.nombre+'_'+repParams.reportName)
	}
	
	def reportePorEmpleado(PorEmpleadoCommand command){
		if(command==null){
			render 'No esta bien generado el gsp para el reporte falta el bean PorEmpleadoCommand'
		}
		command.validate()
		if(command.hasErrors()){
			return [reportCommand:command]
		}
		def repParams=[:]
		repParams['ID']=command.empleado.id as Integer
		repParams.reportName=params.reportName?:'FaltaNombre Del Reporte'
		ByteArrayOutputStream  pdfStream=runReport(repParams)
		render(file: pdfStream.toByteArray(), contentType: 'application/pdf'
			,fileName:command.empleado.nombre+'_'+repParams.reportName)
	}
	
	
	private runReport(Map repParams){
		log.info 'Ejecutando reporte  '+repParams
		def nombre=WordUtils.capitalize(repParams.reportName)
		def reportDef=new JasperReportDef(
			name:nombre
			,fileFormat:JasperExportFormat.PDF_FORMAT
			,parameters:repParams
			)
		ByteArrayOutputStream  pdfStream=jasperService.generateReport(reportDef)
		return pdfStream
		
	}
	
	private File findFile(String name){
		return grailsApplication.mainContext.getResource("/reports/$name").file
	}
	
}

class ImpuestoSobreNominaCommand{
	Integer ejercicio
	String tipo
	String mes
	
	static constraints = {
		ejercicio inList:2014..2018
		tipo inList:['SEMANAL','QUINCENAL']
		mes inList:Mes.getNombres()
    }
}

@Validateable
class EmpleadoPorEjercicioCommand{
	
	Empleado empleado
	Integer ejercicio
	
	static constraints = {
		ejercicio inList:2014..2018
		empleado nullable:false
		
	}
	String toString(){
		return "$empleado $ejercicio"
	}
}


