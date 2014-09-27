package com.luxsoft.sw4.rh

import grails.validation.Validateable

import org.apache.commons.lang.WordUtils;
import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef

import com.luxsoft.sw4.cfdi.ImporteALetra
import com.luxsoft.sw4.EmpleadoPorEjercicioCommand



class ContratacionController {
	
	def jasperService
	
	def index(){}
	
	def contrato(EmpleadoPorEjercicioCommand command){
		if(request.method=='GET'){
			render view:'contrato',model:[reportCommand:new EmpleadoPorEjercicioCommand()]
			return
		}
		command.ejercicio=session.ejercicio
		
		log.info 'Parametros: '+params
		command.validate()
		if(command.hasErrors()){
			render view:'contrato',model:[reportCommand:command]
			return
		}
		
		def salario=command.empleado.salario.getSalarioMensual()
		def repParams=[:]
		repParams['ID']=command.empleado.id as Integer
		repParams['SALARIO_MENSUAL']=salario as String
		repParams['IMP_CON_LETRA']=ImporteALetra.aLetra(salario)
		def reportDef=new JasperReportDef(
			name:'Contrato'
			,fileFormat:JasperExportFormat.PDF_FORMAT
			,parameters:repParams
			)
		ByteArrayOutputStream  pdfStream=jasperService.generateReport(reportDef)
		render(file: pdfStream.toByteArray(), contentType: 'application/pdf',fileName:command.empleado.nombre+'_contrato')
	}
	
	def solicitud(){
		def file=grailsApplication.mainContext.getResource("/reports/SolicitudDeEmpleo.pdf").file
		if(file.exists()){
			render(file: file, contentType: 'application/pdf',fileName:'SolicitudDeEmpleo.pdf')
		}else{
			flash.message="No existe el archivo "+file
			redirect action:'index'
		}
	}
	
	def recepcionDeDocumentos(){
		[reportCommand:new PorEmpleadoCommand()]
	}
	def induccion(){
		[reportCommand:new PorEmpleadoCommand()]
	}
	def entregaDeDocumentos(){
		[reportCommand:new PorEmpleadoCommand()]
	}
	def constanciaDeNoEmbarazo(){
		[reportCommand:new PorEmpleadoCommand()]
	}
	def constanciaDeNoFamiliares(){
		[reportCommand:new PorEmpleadoCommand()]
	}
	def solicitudDeTarjetaDeNomina(){
		[reportCommand:new PorEmpleadoCommand()]
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

@Validateable
class PorEmpleadoCommand {
	
	Empleado empleado
	
	static constraints = {
		empleado nullable:false
	}
}



