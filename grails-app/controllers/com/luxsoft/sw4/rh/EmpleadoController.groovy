package com.luxsoft.sw4.rh



import grails.plugin.springsecurity.annotation.Secured
import grails.validation.Validateable

import org.apache.commons.lang.WordUtils
import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef
import com.luxsoft.sw4.rh.*
import com.luxsoft.sw4.Mes



@Secured(['ROLE_ADMIN','RH_USER'])
class EmpleadoController {
	
    def empleadoService
	def jasperService
	
	def index(Long max) {
		//params.max = Math.min(max ?: 50, 100)
		params.sort=params.sort?:'apellidoPaterno'
		params.order='asc'
		def tipo=params.tipo?:'QUINCENAL'
		def query=Empleado.where{
			activo==true && salario.periodicidad==tipo
		}
		def list=query.list(params)
		[empleadoInstanceList:list,
			empleadoInstanceCount:query.count(),
			tipo:tipo]
	}
	
	def show(Empleado empleadoInstance){
		redirect action:'generales',params:params
	}
	
	def search(String apellidoPaterno,String apellidoMaterno){
		//println 'Buscando empleados: '+apellidoPaterno
		params.max = 20
		params.sort='apellidoPaterno'
		params.order='asc'
		//def query=Empleado.where{apellidoPaterno=~apellidoPaterno+"%"}
		//println 'Encontrados: '+query.list(params).size()
		//def model=[empleadoInstanceList:query.list(params),empleadoInstanceCount:query.count()]
		
		def list=Empleado.findAll("from Empleado e where lower(e.apellidoPaterno)=? or lower(e.apellidoMaterno)=? order by e.apellidoPaterno asc"
			,[apellidoPaterno?.toLowerCase(),apellidoMaterno?.toLowerCase()],params)
		def model=[empleadoInstanceList:list,empleadoInstanceCount:list.size()]
		render view:'index',model:model
	}
	
	def searchAndShow(String apellidoPaterno,String apellidoMaterno) {
		def query=Empleado.where{apellidoPaterno==~apellidoPaterno }
		//query=query.where{apellidoMaterno==~apellidoMaterno }
		def found=query.list()
		
		if(found.size()==1) {
			params.id=found[0].id
			redirect action:'generales',params:params
		}else {
			render view:'index',model:[empleadoInstanceList:found,empleadoInstanceCount:found.size()]
		}
	}

	def generales(Empleado empleadoInstance){
		def baja=BajaDeEmpleado.findByEmpleado(empleadoInstance)?:new BajaDeEmpleado()
		[empleadoInstance:empleadoInstance,edit:params.edit]
	}
	
	def contactos(Empleado empleadoInstance){
		[empleadoInstance:empleadoInstance,edit:params.edit]
	}
	
	def create(){
		[empleadoInstance:new Empleado(status:'ALTA')]
	}
	
	def save(Empleado empleado) {
		log.info 'Salvando empleado nuevo: '+empleado
		try {
			def found=Empleado.findByRfc(empleado.rfc)
			if(found){
				flash.message="RFC Ya est asignado a: "+found.nombre
				render view:'create' ,model:[empleadoInstance:empleado,edit:true]
				return
			}
				
			def res=empleadoService.save empleado
			
			render view:'generales',model:[empleadoInstance:res,edit:true]
		}catch(EmpleadoException ex) {
			ex.printStackTrace()
			flash.message=ex.message
			println 'Errores: '+ex.empleado.errors
			render view:'create' ,model:[empleadoInstance:ex.empleado,edit:true]
		}
	}

	def update(Empleado empleadoInstance){
		//def empleadoInstance=Empleado.get(id)
		//log.info 'Salvando empleado: '+empleadoInstance
		//log.info 'Datos de salario: '+empleadoInstance.salario
		//println 'Salvando: '+empleadoInstance.perfil
		def v=params.view?:'generales'
		//log.info 'Pamaetros: '+params
		//bindData(empleadoInstance.salario, params)
		
		if(empleadoInstance==null){
			notFound()
			return
		}
		
		try{
			
			empleadoInstance=empleadoService.updateEmpleado(empleadoInstance)
			flash.message="Empleado ${empleadoInstance.clave} actualizado"
			render view:v,model:[empleadoInstance:empleadoInstance,edit:false]
		}catch(EmpleadoException ex){
			//println ex.message
			flash.message=ex.message
			render view:v,model:[empleadoInstance:ex.empleado,edit:true]
		}
		
	}
	
	def updateSalario(Empleado empleadoInstance){
		
		def v=params.view?:'salario'
		log.info "Actualizando salario para $empleadoInstance salario: $empleadoInstance.salario.salarioDiario SDI: $empleadoInstance.salario.salarioDiarioIntegrado "
		
		try{
			empleadoInstance=empleadoService.updateSalario(empleadoInstance)
			flash.message="Salario actualizado  ${empleadoInstance.clave}  SDI: ${empleadoInstance.salario.salarioDiarioIntegrado}"
			render view:v,model:[empleadoInstance:empleadoInstance,edit:false]
		}catch(EmpleadoException ex){
			render view:v,model:[empleadoInstance:ex.empleado,edit:true]
		}
		
	}

	def perfil(Empleado empleadoInstance){
		[empleadoInstance:empleadoInstance,edit:params.edit]
	}
	
	def updatePerfile(){
		
	}
	
	def salario(Empleado empleadoInstance){
		[empleadoInstance:empleadoInstance,edit:params.edit]
	}
	def seguridadSocial(Empleado empleadoInstance){
		[empleadoInstance:empleadoInstance,edit:params.edit]
	}
	
	def datosPersonales(Empleado empleadoInstance){
		[empleadoInstance:empleadoInstance,edit:params.edit]
	}

	protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message'
                	, args: [message(code: 'empleadoInstance.label', default: 'Empleado'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
	
	
	def registrarBaja(BajaDeEmpleado baja){
		
		if(request.method=='GET'){
			def empleadoInstance=Empleado.get(params.id)
			return [empleadoInstance:empleadoInstance,bajaInstance:new BajaDeEmpleado()]
		}
		if(request.method=="POST"){
			println 'Salvando baja de empleado: '+baja
			//baja.empleado=empleadoInstance
			
			def empleadoInstance=empleadoService.registrarBaja(baja)
			flash.message="Baja registrada  ${empleadoInstance.clave} "
			
			//redirect action:'show',params:[id:empleadoInstance.id]
			//redirect action:'index'
			redirect action:'show',params:[id:empleadoInstance.id]
			
		}
		
	}
	
	def registrarReingreso(Empleado empleado){
		
	}
	
	def reporteEmpleados(ReporteDeEmpleadosCommand command){
		def repParams=[:]
		repParams['FPAGO']=command.fpago=='TODOS' ? '%' : command.fpago
		repParams['PERIODICIDAD']=command.periodicidad=='TODOS'? '%' : command.periodicidad
		
		repParams.reportName='Empleados'
		ByteArrayOutputStream  pdfStream=runReport(repParams)
		render(file: pdfStream.toByteArray(), contentType: 'application/pdf'
			,fileName:repParams.reportName)
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
	
	
	def cumpleanios(EjercicioMesReportCommand command){
		def repParams=[:]
		repParams['EJERCICIO']=command.ejercicio
		repParams['MES']=command.mes
		
		repParams.reportName='Cumpleanios'
		ByteArrayOutputStream  pdfStream=runReport(repParams)
		render(file: pdfStream.toByteArray(), contentType: 'application/pdf'
			,fileName:repParams.reportName)
	}

}

@Validateable
class ReporteDeEmpleadosCommand{
	
	String fpago
	String periodicidad

	static constraints = {
		fpago inList:['TODOS','CHEQUE','TRANSFERENCIA']
		periodicidad inList:['TODOS','SEMANAL','QUINCENAL']
		
	}
}


@Validateable
class EjercicioMesReportCommand{
	
	
	String mes
	String ejercicio
	
	static constraints={
		ejercicio inList:2015..2025
		mes inList:['1','2','3','4','5','6','7','8','9','10','11','12']
		
	}
}