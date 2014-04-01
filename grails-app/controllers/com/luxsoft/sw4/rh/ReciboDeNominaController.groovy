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
		
		def repParams=CfdiPrintUtils.resolverParametros(comprobante,complemento.nomina)
		params<<repParams
		params.FECHA=comprobante.fecha.getTime().format("yyyy-MM-dd'T'HH:mm:ss")
		//params['SALARIO_DIARIO_BASE']=nominaPorEmpleado.salarioDiarioBase
		params['SALARIO_DIARIO_INTEGRADO']=nominaPorEmpleado.salarioDiarioIntegrado
		//println 'Parametros enviados: '+params
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

