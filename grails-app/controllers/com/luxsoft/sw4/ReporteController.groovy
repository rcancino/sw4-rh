package com.luxsoft.sw4

import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef
import grails.validation.Validateable

import com.luxsoft.sw4.cfdi.ImporteALetra
import com.luxsoft.sw4.rh.*

import grails.plugin.springsecurity.annotation.Secured

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
	
	def historicoDeSalarios(EmpleadoPorEjercicioCommand command){
		
		if(request.method=='GET'){
			log.info 'Reporte Historico de salarios'
			render view:'historicoDeSalarios',model:[reportCommand:new EmpleadoPorEjercicioCommand()]
			return
		}
		
		command.validate()
		println 'Metodo: '+request.method+' Command errors: '+command.errors
		if(command.hasErrors()){
			log.info 'Errores de validacion al ejecurar reporte'
			render view:'historicoDeSalarios',model:[reportCommand:command]
			return
		}
		def repParams=[:]
		repParams['EJERCICIO']=command.ejercicio as Long
		repParams['EMPLEADO_ID']=command.empleado.id as Integer
		def reportDef=new JasperReportDef(
			name:'HistoricoDeSalarios'
			,fileFormat:JasperExportFormat.PDF_FORMAT
			,parameters:repParams
			)
		ByteArrayOutputStream  pdfStream=jasperService.generateReport(reportDef)
		log.info 'Ejecutando reporte HistoricoDeSalarios '+command
		render(file: pdfStream.toByteArray(), contentType: 'application/pdf',fileName:'historicoDeSalarios')
		
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

class EmpleadoCommand{
	Empleado empleado
	
	static constraints = {
		empleado nullable:false
		
	}
	
	String toString(){
		return "$empleado "
	}
}
