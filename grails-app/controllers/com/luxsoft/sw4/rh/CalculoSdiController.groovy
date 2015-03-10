package com.luxsoft.sw4.rh

import java.util.Map;

import grails.plugin.springsecurity.annotation.Secured

import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef

@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
class CalculoSdiController {
	
	def salarioService
	def jasperService

    def index(Long max) {
		
		params.max = Math.min(max ?: 1000, 10000)
		//params.sort=params.sort?:'ubicacion'
		//params.order='desc'
		def ejercicio=session.ejercicio
		def bimestre=session.bimestre
		def list=CalculoSdi.findAll("from CalculoSdi c where c.ejercicio=? and c.bimestre=?",[ejercicio,bimestre],params)
		[calculoSdiInstanceList:list]
		
	}
	
	def cambiarBimestre(CalculoBimestralCommand command){
		
		session.bimestre=command.bimestre
		session.ejercicio=command.ejercicio
		redirect action:'index'
		
	}
	
	def calcularSalarioDiarioIntegrado(){
		def found=CalculoSdi.findByEjercicioAndBimestreAndTipo(session.ejercicio,session.bimestre,'CALCULO_SDI')
		if(found){
			if(found.status=='APLICADO'){
				flash.message='Periodo ya calculado y aplicado'
				redirect action:'index'
				return
			}
		}
		def rows=salarioService.calcularSalarioDiario(session.ejercicio,session.bimestre)
		redirect action:'index'
		
	}
	
	def aplicarSalarioDiarioIntegrado(){
		log.info " Aplicando para $session.ejercicio - $session.bimestre"
		
		def list=CalculoSdi.findAll("from CalculoSdi c where c.ejercicio=? and c.bimestre=?",[session.ejercicio,session.bimestre])
		println 'Registros: '+list.size()
		list.each {
			salarioService.aplicarCalculo(it)
		}
		render view:'index',model:[calculoSdiInstanceList:list]
	}
	
	
	def print(){
		def ejercicio=session.ejercicio
		def bimestre=session.bimestre
		def repParams=[:]
		repParams['EJERCICIO']=ejercicio
		repParams['BIMESTRE']=bimestre
		repParams.reportName='SdiBimestral'
		ByteArrayOutputStream  pdfStream=runReport(repParams)
		render(file: pdfStream.toByteArray(), contentType: 'application/pdf'
			,fileName:"SdiBimestral")
		
	}
	
	def reporte(){
		def tipo=params.tipo
		def re=''
		switch(tipo) {
			case 'BASE':
				re='SdiBimestral'
				break;
			case 'CALCULO':
				re='SdiBimestralCalculo'
				break;
			case 'VARIABLES':
				re='SdiBimestralVariables'
				break;
			
			break
		}
		if(re){
			params.reportName=re
			params['EJERCICIO']=session.ejercicio
			params['BIMESTRE']=session.bimestre
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
