package com.luxsoft.sw4.rh

import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante;

import com.luxsoft.sw4.Mes;
import com.luxsoft.sw4.cfdi.Cfdi;
import com.luxsoft.sw4.cfdi.CfdiPrintUtils;
import com.luxsoft.sw4.cfdi.ComplementoNomina;

class ReciboDeNominaController {

    def index(long nominaId) {
		
		params.periodicidad=params.periodicidad?:'QUINCENAL'
		
		println 'Lista de recibos para : '+params
		
		def nominasPorMes=[:]
		Mes.getMeses().each{
			nominasPorMes[it.nombre]=Nomina.findAll(
			"from Nomina n where n.periodicidad='QUINCENAL' and month(n.periodo.fechaInicial)=?"
			,[it.clave+1])
		}
		
		def nominasList=[]
		
		[
			nominasPorMesInstanceMap:nominasPorMes  //Para el scrollpane
			,nominaInstance:Nomina.get(nominaId) //La nomina seleccionada
			,mesInstance:params.mesInstance //Para seleccionar el scrollpan activo
		]
		
	}
	
	def imprimirCfdi() {
		println 'Imprimeindo CFDI: '+params.id
		def cfdi=Cfdi.findById(params.id)
		if(cfdi==null){
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'cfdiInstance.label', default: 'Cfdi'), params.id])
			redirect action: "show", params:[id:id]
		}
		Comprobante comprobante=cfdi.comprobante
		def conceptos=comprobante.getConceptos().getConceptoArray()
		def modelData=conceptos.collect { cc ->
			
			def res=[
				'GRUPO':''+cc.cantidad,
				'DESCRIPCION':''+cc.unidad,
				'IMPORTE_GRABADO':cc.valorUnitario,
				'IMPORTE_EXCENTO':0.0
			 ]
			return res
		}
		ComplementoNomina complemento=new ComplementoNomina(comprobante)
		def repParams=CfdiPrintUtils.resolverParametros(comprobante,complemento.nomina)
		params<<repParams
		params.FECHA=comprobante.fecha.getTime().format("yyyy-MM-dd'T'HH:mm:ss")
		println 'Parametros enviados: '+params
		chain(controller:'jasper',action:'index',model:[data:modelData],params:params)
		//chain(controller:'jasper',action:'index',params:params)
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

