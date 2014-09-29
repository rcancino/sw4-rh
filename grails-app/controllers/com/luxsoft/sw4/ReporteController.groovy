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

	def bitacoraDeChecado(EmpleadoCalendarioDetCommand command){
		if(request.method=='GET'){
			return [reportCommand:new EmpleadoCalendarioDetCommand()]
		}
		command.validate()
		if(command.hasErrors()){
			log.info 'Errores de validacion al ejecurar reporte'
			render view:'bitacoraDeChecado',model:[reportCommand:command]
			return
		}
		def repParams=[:]
		repParams['EMPLEADO_ID']=command.empleado.id as String
		repParams['CALENDARIODET']=command.calendario.id as String
		repParams.reportName=params.reportName?:'FaltaNombre Del Reporte'
		ByteArrayOutputStream  pdfStream=runReport(repParams)
		render(file: pdfStream.toByteArray(), contentType: 'application/pdf'
			,fileName:command.empleado.nombre+'_'+repParams.reportName)
		
	}

	def incrementosIndividuales(Nomina nomina){
		if(request.method=='GET'){
			return []
		}
		def repParams=[:]
		repParams['NOMINA_ID']=nomina.id 
		repParams.reportName=params.reportName?:'FaltaNombre Del Reporte'
		ByteArrayOutputStream  pdfStream=runReport(repParams)
		render(file: pdfStream.toByteArray(), contentType: 'application/pdf'
			,fileName:repParams.reportName)
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
	def reportePorEmpleadoEjercicio2(EmpleadoPorEjercicioCommand command){
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
		repParams['EJERCICIO']=command.ejercicio as Integer
		repParams['EMPLEADO_ID']=command.empleado.id as Long
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

	def historicoDePrestamos(){
		[reportCommand:new PorEmpleadoCommand()]
	}

	def vacacionesEjercicio(EjercicioCommand command){
		if(request.method=='GET'){
			return [reportCommand:new EjercicioCommand()]
		}
		command.validate()
		if(command.hasErrors()){
			log.info 'Errores de validacion al ejecurar reporte'
			render view:'vacacionesEjercicio',model:[reportCommand:command]
			return
		}
		def repParams=[:]
		repParams['EJERCICIO']=command.ejercicio 
		repParams.reportName=params.reportName?:'FaltaNombre Del Reporte'
		ByteArrayOutputStream  pdfStream=runReport(repParams)
		render(file: pdfStream.toByteArray(), contentType: 'application/pdf'
			,fileName:command.ejercicio+'_'+repParams.reportName)
	}
	def vacacionesEmpleado(){
		[reportCommand:new EmpleadoPorEjercicioCommand(ejercicio:session.ejercicio)]
	}

	def incapacidades(PeriodoCommand command){
		if(request.method=='GET'){
			return [reportCommand:new PeriodoCommand()]
		}
		command.validate()
		if(command.hasErrors()){
			log.info 'Errores de validacion al ejecurar reporte'
			render view:'incapacidades',model:[reportCommand:command]
			return
		}
		def repParams=[:]
		repParams['FECHA_INICIAL']=command.fechaInicial
		repParams['FECHA_FINAL']=command.fechaFinal
		repParams.reportName=params.reportName?:'FaltaNombre Del Reporte'
		ByteArrayOutputStream  pdfStream=runReport(repParams)
		render(file: pdfStream.toByteArray(), contentType: 'application/pdf'
			,fileName:repParams.reportName)
	}

	def incapacidadesEmpleado(EmpleadoPeriodoCommand command){
		if(request.method=='GET'){
			return [reportCommand:new EmpleadoPeriodoCommand()]
		}
		command.validate()
		if(command.hasErrors()){
			log.info 'Errores de validacion al ejecurar reporte'
			render view:'incapacidadesEmpleado',model:[reportCommand:command]
			return
		}
		def repParams=[:]
		repParams['FECHA_INICIAL']=command.fechaInicial
		repParams['FECHA_FINAL']=command.fechaFinal
		repParams['EMPLEADO_ID']=command.empleado.id as Long
		repParams.reportName=params.reportName?:'FaltaNombre Del Reporte'
		ByteArrayOutputStream  pdfStream=runReport(repParams)
		render(file: pdfStream.toByteArray(), contentType: 'application/pdf'
			,fileName:repParams.reportName)
	}

	def tiempoExtra(EmpleadoCalendarioDetCommand command){
		if(request.method=='GET'){
			return [reportCommand:new EmpleadoCalendarioDetCommand()]
		}
		command.validate()
		if(command.hasErrors()){
			log.info 'Errores de validacion al ejecurar reporte'
			render view:'tiempoExtra',model:[reportCommand:command]
			return
		}
		def repParams=[:]
		repParams['EMPLEADO_ID']=command.empleado.id 
		repParams['CALENDARIODET']=command.calendario.id 
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

@Validateable
class EmpleadoCalendarioDetCommand{
	Empleado empleado
	CalendarioDet calendario
	static constraints={
		empleado nullable:false
		calendario nullable:false
	}
}

@Validateable
class EjercicioCommand{
	
	Integer ejercicio
	
	static constraints = {
		ejercicio inList:2014..2018
		
		
	}
	String toString(){
		return "$ejercicio"
	}
}

@Validateable
class PeriodoCommand{
	Date fechaInicial
	Date fechaFinal

}

@Validateable
class EmpleadoPeriodoCommand{
	Empleado empleado
	Date fechaInicial
	Date fechaFinal

	static constraints={
		empleado nullable:false
		fechaInicial nullable:false
		fechaFinal nullable:false
	}

}


