package com.luxsoft.sw4.rh

import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat;
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef;

import java.util.Map;

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import grails.validation.Validateable;



import org.grails.databinding.BindingFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef
import org.apache.commons.lang.WordUtils

@Transactional(readOnly = true)
@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
class ModificacionSalarialController {
	
	def calculoSdiService
	
	def jasperService
    
	def search(ModificacionSearch command){
		//println 'Filtrando con parametros: '+command
		
		def list=ModificacionSalarial.findAllWhere(empleado:command.empleado)
		render view:'index',model:[modificacionInstanceList:list
		,modificacionInstanceListTotal:0]
		
	}
	
	def cambiarBimestre(CalculoBimestralCommand command){
		
		session.bimestre=command.bimestre
		redirect action:'index'
		
	}
    def index(Long max){
    	params.max = Math.min(max ?: 15, 100)
		params.sort=params.sort?:'lastUpdated'
		params.order='desc'
		def list=ModificacionSalarial.findAllByBimestre(session.bimestre,params)
		[modificacionInstanceList:list
		,modificacionInstanceListTotal:ModificacionSalarial.countByBimestre(session.bimestre)]
    }

    def create(){
    	[modificacionInstance:new ModificacionSalarial(tipo:'AUMENTO')]
    }

    @Transactional
    def save(ModificacionSalarial modificacionInstance){
    	modificacionInstance.sdiNuevo=0.0
    	modificacionInstance.validate()
    	if(modificacionInstance.hasErrors()){
    		flash.message="Errores de validacion en modificacion salarial"
    		render view:'create',model:[modificacionInstance:modificacionInstance]
    	}
		
    	modificacionInstance.save failOnError:true
		calculoSdiService.calcularSdi(modificacionInstance)
    	redirect action:'show',params:[id:modificacionInstance.id]

    }
	
	@Transactional
	def recalcular(ModificacionSalarial m){
		calculoSdiService.calcularSdi(m)
		redirect action:'show',params:[id:m.id]
	}
	
	@Transactional
	def aplicar(ModificacionSalarial modificacion){
		modificacion.empleado.salario.salarioDiario=modificacion.salarioNuevo
		modificacion.empleado.salario.salarioDiarioIntegrado=modificacion.sdiNuevo
		modificacion.calculoSdi.status='APLICADO'
		modificacion.save flush:true
		modificacion.empleado.save()
		modificacion.calculoSdi.save()
		redirect action:'show',params:[id:modificacion.id]
	}
    
    @Transactional
    def delete(Long id){

        def modificacion=ModificacionSalarial.get(id)

        def calculoSdi = modificacion.calculoSdi
       
        modificacion.calculoSdi = null
        modificacion.delete(flush:true)
        
        //def calculoSdi=CalculoSdi.get(modificacion.calculoSdi.id)
        //def calculoSdi=modificacion.calculoSdi

        if(calculoSdi){
        	
        	calculoSdi.refresh()
        	println "Eliminando el calculo Sdi para:" + modificacion.empleado
        	println "El calculo Sdi es:" +calculoSdi.id
        	calculoSdi.delete(flush:true)
        }
        flash.message="Modificacion salarial ${id} eliminada"
        redirect action:'index'
    }

    def show(Long id){
    	def modificacionInstance=ModificacionSalarial.get(id)
    	[modificacionInstance:modificacionInstance]
    }
	
	def reporteDeSDI(ModificacionSalarial ms){
		
		def repParams=[:]
		repParams['ID']=ms.id
		repParams['FECHA_MODIFICADO']=ms.fecha.format('dd-MM-yyyy')
		
		repParams.reportName='SalarioDiarioIntegradoIndividual'
		ByteArrayOutputStream  pdfStream=runReport(repParams)
		render(file: pdfStream.toByteArray(), contentType: 'application/pdf'
			,fileName:"SDI_"+ms.empleado+".pdf")
		
		
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

}




import groovy.transform.ToString
import grails.validation.Validateable

@ToString(includeNames=true,includePackage=false)
@Validateable
class ModificacionSearch{
	
	Empleado empleado
	
	@BindingFormat('dd/MM/yyyy')
	Date fechaInicial
	
	@BindingFormat('dd/MM/yyyy')
	Date fechaFinal
	
}
