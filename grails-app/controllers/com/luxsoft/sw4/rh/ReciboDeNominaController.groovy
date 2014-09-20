package com.luxsoft.sw4.rh

import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.export.PdfExporterConfiguration;

import org.apache.commons.io.FileUtils;
import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat;
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef
import org.springframework.core.io.Resource









import com.luxsoft.sw4.Mes;
import com.luxsoft.sw4.cfdi.Cfdi;
import com.luxsoft.sw4.cfdi.CfdiPrintUtils;
import com.luxsoft.sw4.cfdi.ComplementoNomina;

import grails.plugin.springsecurity.annotation.Secured

@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
class ReciboDeNominaController {
	
	def jasperService

    def index(long nominaId) {
		
		params.periodicidad=params.periodicidad?:'QUINCENAL'
		
		println 'Lista de recibos para : '+params
		
		def nominasPorMes=[:]
		Mes.getMeses().each{
			nominasPorMes[it.nombre]=Nomina.findAll(
			"from Nomina n where n.periodicidad=? and month(n.periodo.fechaInicial)=?"
			,[params.periodicidad,it.clave+1])
		}
		
		def nominasList=[]
		
		[
			nominasPorMesInstanceMap:nominasPorMes  //Para el scrollpane
			,nominaInstance:Nomina.get(nominaId) //La nomina seleccionada
			,mesInstance:params.mesInstance //Para seleccionar el scrollpan activo
			,periodicidad:params.periodicidad
		]
		
	}
	
	def semanal(long nominaId) {
		println 'Lista de recibos para : '+params
		def nominasPorMes=[:]
		Mes.getMeses().each{
			nominasPorMes[it.nombre]=Nomina.findAll(
			"from Nomina n where n.periodicidad='SEMANAL' and month(n.periodo.fechaInicial)=? order by n.folio"
			,[it.clave+1])
		}
		
		def nominasList=[]
		
		[
			nominasPorMesInstanceMap:nominasPorMes  //Para el scrollpane
			,nominaInstance:Nomina.get(nominaId) //La nomina seleccionada
			,mesInstance:params.mesInstance //Para seleccionar el scrollpan activo
			,periodicidad:params.periodicidad
		]
		
	}
	
	def imprimirCfdi() {
		println 'Imprimeindo CFDI: '+params.id
		def cfdi=Cfdi.findById(params.id)
		if(cfdi==null){
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'cfdiInstance.label', default: 'Cfdi'), params.id])
			redirect action: "show", params:[id:id]
		}
		NominaPorEmpleado nominaPorEmpleado=NominaPorEmpleado.findByCfdi(cfdi)
		Comprobante comprobante=cfdi.comprobante
		ComplementoNomina complemento=new ComplementoNomina(comprobante)
		
		
		mx.gob.sat.nomina.NominaDocument.Nomina nomina=complemento.nomina
		
		def deducciones=nomina?.deducciones?.deduccionArray
		def modelData=deducciones.collect { cc ->
			def res=[
				'GRUPO':cc.tipoDeduccion,
				'CLAVE':cc.clave,
				'DESCRIPCION':cc.concepto,
				'IMPORTE_GRAVADO':cc.importeGravado,
				'IMPORTE_EXENTO':cc.importeExento,
				'CONCEPTO':'D'
			 ]
			return res
		}
		def percepciones=nomina.percepciones.percepcionArray
		percepciones.each{ cc->
			def res=[
				'GRUPO':cc.tipoPercepcion,
				'CLAVE':cc.clave,
				'DESCRIPCION':cc.concepto,
				'IMPORTE_GRAVADO':cc.importeGravado,
				'IMPORTE_EXENTO':cc.importeExento,
				'CONCEPTO':'P'
			 ]
			modelData<<res
		}
		
		modelData.sort{
			it.clave
		}
		
		def repParams=CfdiPrintUtils.resolverParametros(comprobante,complemento.nomina,nominaPorEmpleado)
		params<<repParams
		params.FECHA=comprobante.fecha.getTime().format("yyyy-MM-dd'T'HH:mm:ss")
		//params['SALARIO_DIARIO_BASE']=nominaPorEmpleado.salarioDiarioBase
		//params['SALARIO_DIARIO_INTEGRADO']=nominaPorEmpleado.salarioDiarioIntegrado
		params['RECIBO_NOMINA']=nominaPorEmpleado.id
		params[PdfExporterConfiguration.PROPERTY_PDF_JAVASCRIPT]="this.print();"
		//println 'Parametros enviados: '+params
		chain(controller:'jasper',action:'index',model:[data:modelData],params:params)
		//chain(controller:'jasper',action:'index',params:params)
	}
	
	def impresionDirecta() {
		
		def cfdi=Cfdi.findById(params.id)
		if(cfdi==null){
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'cfdiInstance.label', default: 'Cfdi'), params.id])
			redirect action: "show", params:[id:id]
		}
		NominaPorEmpleado nominaPorEmpleado=NominaPorEmpleado.findByCfdi(cfdi)
		Comprobante comprobante=cfdi.comprobante
		ComplementoNomina complemento=new ComplementoNomina(comprobante)
		mx.gob.sat.nomina.NominaDocument.Nomina nomina=complemento.nomina
		
		def deducciones=nomina?.deducciones?.deduccionArray
		def modelData=deducciones.collect { cc ->
			def res=[
				'GRUPO':cc.tipoDeduccion,
				'CLAVE':cc.clave,
				'DESCRIPCION':cc.concepto,
				'IMPORTE_GRAVADO':cc.importeGravado,
				'IMPORTE_EXENTO':cc.importeExento,
				'CONCEPTO':'D'
			 ]
			return res
		}
		def percepciones=nomina.percepciones.percepcionArray
		percepciones.each{ cc->
			def res=[
				'GRUPO':cc.tipoPercepcion,
				'CLAVE':cc.clave,
				'DESCRIPCION':cc.concepto,
				'IMPORTE_GRAVADO':cc.importeGravado,
				'IMPORTE_EXENTO':cc.importeExento,
				'CONCEPTO':'P'
			 ]
			modelData<<res
		}
		
		modelData.sort{
			it.clave
		}
		
		def repParams=CfdiPrintUtils.resolverParametros2(comprobante,complemento.nomina,nominaPorEmpleado)
		params<<repParams
		params.FECHA=comprobante.fecha.getTime().format("yyyy-MM-dd'T'HH:mm:ss")
		//params['SALARIO_DIARIO_INTEGRADO']=nominaPorEmpleado.salarioDiarioIntegrado as String
		params['RECIBO_NOMINA']=nominaPorEmpleado.id as String
		params[PdfExporterConfiguration.PROPERTY_PDF_JAVASCRIPT]="this.print();"
		def reportDef=new JasperReportDef(
			name:'NominaDigitalCFDI'
			,fileFormat:JasperExportFormat.PDF_FORMAT
			,reportData:modelData,
			,parameters:params
			)
		Resource resource = reportDef.getReport()
		JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(reportDef.reportData)
		JasperPrint print= JasperFillManager.fillReport(JasperCompileManager.compileReport(resource.inputStream)
			, reportDef.parameters
			, jrBeanCollectionDataSource)
		ByteArrayOutputStream  pdfStream = new ByteArrayOutputStream();
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, pdfStream); // your output goes here
		//exporter.setParameter(JRPdfExporterParameter.PDF_JAVASCRIPT, "this.print();");
		exporter.exportReport();
		render(file: pdfStream.toByteArray(), contentType: 'application/pdf')
	}
	
	def imprimirCfdis(Nomina n){
		def reportes=[]
		n.partidas.sort{it.orden}.each{ nominaPorEmpleado->
			
			if(nominaPorEmpleado.cfdi){
				def cfdi=nominaPorEmpleado.cfdi
				Comprobante comprobante=cfdi.comprobante
				ComplementoNomina complemento=new ComplementoNomina(comprobante)
				mx.gob.sat.nomina.NominaDocument.Nomina nomina=complemento.nomina
				def deducciones=nomina?.deducciones?.deduccionArray
				def modelData=deducciones.collect { cc ->
					def res=[
						'GRUPO':cc.tipoDeduccion,
						'CLAVE':cc.clave,
						'DESCRIPCION':cc.concepto,
						'IMPORTE_GRAVADO':cc.importeGravado,
						'IMPORTE_EXENTO':cc.importeExento,
						'CONCEPTO':'D'
					 ]
					return res
				}
				def percepciones=nomina.percepciones.percepcionArray
				percepciones.each{ cc->
					def res=[
						'GRUPO':cc.tipoPercepcion,
						'CLAVE':cc.clave,
						'DESCRIPCION':cc.concepto,
						'IMPORTE_GRAVADO':cc.importeGravado,
						'IMPORTE_EXENTO':cc.importeExento,
						'CONCEPTO':'P'
					 ]
					modelData<<res
				}
				modelData.sort{
					it.clave
				}
				
				def repParams=CfdiPrintUtils.resolverParametros2(comprobante,complemento.nomina,nominaPorEmpleado)
				//params<<repParams
				repParams.FECHA=comprobante.fecha.getTime().format("yyyy-MM-dd'T'HH:mm:ss")
				repParams['RECIBO_NOMINA']=nominaPorEmpleado.id as String
				
				def reportDef=new JasperReportDef(
					name:'NominaDigitalCFDI'
					,fileFormat:JasperExportFormat.PDF_FORMAT
					,reportData:modelData,
					,parameters:repParams
					)
				reportes.add(reportDef)
			}
		}
		ByteArrayOutputStream  pdfStream=jasperService.generateReport(reportes)
		//FileUtils.writeByteArrayToFile(new File("c:/pruebas/testReport2.pdf"), jasperService.generateReport(reportes).toByteArray())
		def fileName="nomina_${n.ejercicio}_${n.periodicidad}_${n.folio}.pdf"
		render(file: pdfStream.toByteArray(), contentType: 'application/pdf',fileName:fileName)
	}
	
	def showXml(Long id){
		println 'Mostrando XML: '+id
		def cfdi=Cfdi.findById(id)
		if(cfdi==null){
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'cfdiInstance.label', default: 'Cfdi'), params.id])
			redirect action: "index", method: "GET"
		}
		render view:'cfdiXml',model:[cfdi:cfdi,xml:cfdi.getComprobanteDocument().xmlText()]
	}
}

