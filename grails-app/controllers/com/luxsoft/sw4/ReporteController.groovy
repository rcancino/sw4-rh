package com.luxsoft.sw4

import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef

import com.luxsoft.sw4.cfdi.ImporteALetra;
import com.luxsoft.sw4.rh.*

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
	
	def contrato(EmpleadoPorEjercicioCommand command){
		if(request.method=='GET'){
			render view:'contrato',model:[reportCommand:new EmpleadoPorEjercicioCommand()]
			return
		}
		command.ejercicio=session.ejercicio
		log.info 'Generando contrato: '+command
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
