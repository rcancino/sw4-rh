package com.luxsoft.sw4.rh

import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat;
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef;

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import grails.validation.Validateable;


import org.grails.databinding.BindingFormat

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
        modificacion.delete(flush:true)
        flash.message="Modificacion salarial ${id} eliminada"
        redirect action:'index'
    }

    def show(Long id){
    	def modificacionInstance=ModificacionSalarial.get(id)
    	[modificacionInstance:modificacionInstance]
    }
	
	def reporteDeSDI(ModificacionSalarial ms){
		//log.info 'Reporte de analis SDI para modificacion salarial: '+ms
		def val=CalendarioDet.executeQuery("select min(d.bimestre) from CalendarioDet d where date(?) between d.inicio and d.fin",[ms.fecha])
		def bimestre=val.get(0)-1
		def tipo=ms.empleado.salario.periodicidad=='SEMANAL'?'SEMANA':'QUINCENA'
		def ejercicio=session.ejercicio
		def res=CalendarioDet
			.executeQuery("select min(d.inicio),max(d.fin) from CalendarioDet d where d.bimestre=? and d.calendario.tipo=? and d.calendario.ejercicio=?"
		,[bimestre,tipo,ejercicio])
	
		def inicio=new Date(res.get(0)[0].getTime())
		def fin=new Date(res.get(0)[1].getTime())
		println 'Tipo de fecha'+ fin.class
		def parameters=['ID':ms.empleado.id.toString()]
		parameters['TIPO']=ms.empleado.salario.periodicidad
		//parameters['FECHA_ULT_MODIF']=ms.fecha.format('yyyy-MM-dd')
		//parameters['FECHA_INI']=inicio.format('yyyy-MM-dd')
		//parameters['FECHA_FIN']=fin.format('yyyy-MM-dd')
		//parameters['SDI_ANTERIOR']=ms.sdiAnterior
		log.info 'Ejecurando reporte con parametros:'+parameters
		JasperReportDef reportDef=new JasperReportDef(name:'SalarioDiarioIntegradoIndividual'
			,fileFormat:JasperExportFormat.PDF_FORMAT
			,parameters:parameters)
		
		def out=jasperService.generateReport(reportDef)
		def fileName="sdi"+ms.empleado+".pdf"
		response.setHeader("Content-disposition", "attachment; filename=$fileName" );
		response.contentType = reportDef.fileFormat.mimeTyp
		response.characterEncoding = "UTF-8"
		response.outputStream << out
		//chain controller:'jasper',action:'index',params:params
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
