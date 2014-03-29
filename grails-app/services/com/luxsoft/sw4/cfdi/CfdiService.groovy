package com.luxsoft.sw4.cfdi

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject
import org.apache.xmlbeans.XmlOptions
import org.apache.xmlbeans.XmlValidationError
import org.bouncycastle.util.encoders.Base64

import com.luxsoft.sw4.cfdi.CFDIUtils
import com.luxsoft.sw4.cfdi.Cfdi
import com.luxsoft.sw4.cfdi.CfdiException
import com.luxsoft.sw4.cfdi.Folio
import com.luxsoft.sw4.rh.NominaPorEmpleado;

import mx.gob.sat.cfd.x3.ComprobanteDocument
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Complemento
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Conceptos
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Conceptos.Concepto
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Emisor
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Impuestos;
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Receptor
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.TipoDeComprobante
import mx.gob.sat.nomina.NominaDocument
import mx.gob.sat.nomina.NominaDocument.Nomina
import mx.gob.sat.nomina.NominaDocument.Nomina.Deducciones
import mx.gob.sat.nomina.NominaDocument.Nomina.Deducciones.Deduccion
import mx.gob.sat.nomina.NominaDocument.Nomina.Incapacidades
import mx.gob.sat.nomina.NominaDocument.Nomina.Incapacidades.Incapacidad
import mx.gob.sat.nomina.NominaDocument.Nomina.Percepciones
import mx.gob.sat.nomina.NominaDocument.Nomina.Percepciones.Percepcion
import grails.transaction.Transactional

import grails.transaction.Transactional

@Transactional
class CfdiService {
	
	def cfdiSellador
	
	def cfdiTimbrador
	
	Cfdi generarComprobante(def nominaEmpleadoId) {
		
		def fecha=new Date()
		def nominaEmpleado=NominaPorEmpleado.get(nominaEmpleadoId)
		def empresa=nominaEmpleado.nomina.empresa
		def empleado=nominaEmpleado.empleado
		Folio folio=Folio.findOrSaveWhere(empresa:empresa,serie:'NOMINA_CFDI')
		ComprobanteDocument document=ComprobanteDocument.Factory.newInstance()
		Comprobante comprobante=document.addNewComprobante()
		CFDIUtils.depurar(document)
		  comprobante.serie='NOMINA_CFDI'
		  comprobante.folio=folio.next().toString()
		  comprobante.setVersion("3.2")
		  comprobante.setFecha(CFDIUtils.toXmlDate(fecha).getCalendarValue())
		  comprobante.setFormaDePago("PAGO EN UNA SOLA EXHIBICION")
		  comprobante.setMetodoDePago(nominaEmpleado.nomina.formaDePago)
		  comprobante.setMoneda(Currency.getInstance(new Locale("es","mx")).currencyCode)
		  comprobante.setTipoCambio("1.0")
		
		comprobante.setTipoDeComprobante(TipoDeComprobante.EGRESO)
		comprobante.setLugarExpedicion(empresa.direccion.pais)
		comprobante.setNoCertificado(empresa.numeroDeCertificado)
		byte[] encodedCert=Base64.encode(empresa.getCertificado().getEncoded())
		comprobante.setCertificado(new String(encodedCert))
		  //comprobante.addNewEmisor()
		Emisor emisor=CFDIUtils.registrarEmisor(comprobante, empresa)
		Receptor receptor=CFDIUtils.registrarReceptor(comprobante, nominaEmpleado.empleado)
		
		//Importes
		comprobante.setSubTotal(nominaEmpleado.total)
		comprobante.setDescuento(nominaEmpleado.conceptos.sum{
			def vv=0.0
			if(it.concepto.tipo=='DEDUCCION' && it.concepto.claveSat!=2) {
			  vv+=it.importeGravado+it.importeExcento
			}
			return vv
		})
		comprobante.setMotivoDescuento("Deducciones nómina")
		comprobante.setTotal(nominaEmpleado.total)
	  
		//Conceptos
		Conceptos conceptos=comprobante.addNewConceptos()
		  Concepto c=conceptos.addNewConcepto();
		  c.setCantidad(1);
		  c.setUnidad("Servicio");
		  c.setNoIdentificacion('CARGO');
		  c.setDescripcion('Pago de Nómina');
		  c.setValorUnitario(nominaEmpleado.percepciones);
		  c.setImporte(nominaEmpleado.percepciones);
		
		//Complemento nomina
		NominaDocument nominaDocto=NominaDocument.Factory.newInstance()
		Nomina nomina=nominaDocto.addNewNomina()
		
		 nomina.version="1.1"
		  
		  nomina.with{
			registroPatronal=empresa.registroPatronal
			numEmpleado=empleado.perfil.numeroDeTrabajador
			CURP=empleado.curp
			tipoRegimen=empleado.perfil.regimenContratacion.clave
			numSeguridadSocial=empleado.seguridadSocial.numero
			setAntiguedad(nominaEmpleado.antiguedad)
			setFechaInicioRelLaboral(CFDIUtils.toXmlDate(empleado.alta).getCalendarValue())
			setFechaPago(CFDIUtils.toXmlDate(nominaEmpleado.nomina.pago).getCalendarValue())
			setFechaInicialPago(CFDIUtils.toXmlDate(nominaEmpleado.nomina.periodo.fechaInicial).getCalendarValue())
			setFechaFinalPago(CFDIUtils.toXmlDate(nominaEmpleado.nomina.periodo.fechaFinal).getCalendarValue())
			setNumDiasPagados(nominaEmpleado.nomina.diasPagados as BigDecimal)
			setDepartamento(empleado.perfil.departamento.clave)
			//setBanco(empleado.salario.banco?.clave)
			setTipoJornada(empleado.perfil.jornada)
			setPeriodicidadPago(empleado.salario.periodicidad)
			setRiesgoPuesto(empleado.perfil.riesgoPuesto.clave)
			setSalarioBaseCotApor(nominaEmpleado.salarioDiarioBase)
			setSalarioDiarioIntegrado(nominaEmpleado.salarioDiarioIntegrado)
		  }
		// Percepciones
		Percepciones per=nomina.addNewPercepciones()
		per.totalGravado=nominaEmpleado.percepcionesGravadas
		per.totalExento=nominaEmpleado.percepcionesExcentas
		nominaEmpleado.conceptos.each{
			if(it.concepto.tipo=='PERCEPCION') {
			  Percepcion pp=per.addNewPercepcion()
			  pp.setTipoPercepcion(StringUtils.leftPad(it.concepto.claveSat.toString(), 3, '0'))
			  pp.setClave(it.concepto.clave)
			  pp.setConcepto(it.concepto.descripcion)
			  pp.setImporteGravado(it.importeGravado)
			  pp.setImporteExento(it.importeExcento)
			}
		  }
		
		//Deducciones
		
		  Deducciones ded=nomina.addNewDeducciones()
		  ded.totalGravado=nominaEmpleado.deduccionesGravadas
		  ded.totalExento=nominaEmpleado.totalExcento
		  
		  nominaEmpleado.conceptos.each{
			if(it.concepto.tipo=='DEDUCCION') {
			  Deduccion dd=ded.addNewDeduccion()
			  dd.setTipoDeduccion(StringUtils.leftPad(it.concepto.claveSat.toString(), 3, '0'))
			  dd.setClave(it.concepto.clave)
			  dd.setConcepto(it.concepto.descripcion)
			  dd.setImporteGravado(it.importeGravado)
			  dd.setImporteExento(it.importeExcento)
			}
		  }
		
	   
		
		
		Complemento complemento=comprobante.addNewComplemento()
		def cursor=complemento.newCursor()
		cursor.toEndToken()
		def cn=nomina.newCursor()
		cn.moveXml(cursor)
		
		comprobante.sello=cfdiSellador.sellar(empresa.privateKey,document)
		
		//Impuestos
		Impuestos impuestos=comprobante.addNewImpuestos()
	   // impuestos.setTotalImpuestosRetenidos()
		
		 XmlOptions options = new XmlOptions()
		  options.setCharacterEncoding("UTF-8")
		  options.put( XmlOptions.SAVE_INNER )
		  options.put( XmlOptions.SAVE_PRETTY_PRINT )
		  options.put( XmlOptions.SAVE_AGGRESSIVE_NAMESPACES )
		  options.put( XmlOptions.SAVE_USE_DEFAULT_NAMESPACE )
		  options.put(XmlOptions.SAVE_NAMESPACES_FIRST)
		  ByteArrayOutputStream os=new ByteArrayOutputStream()
		  document.save(os, options)
	  
		
		
		def cfdi=new Cfdi(comprobante)
		cfdi.xml=os.toByteArray()
		cfdi.setXmlName("$cfdi.receptorRfc-$cfdi.serie-$cfdi.folio"+".xml")
		
		validarDocumento(document)
		//def timbrador=ctx.getBean('cfdiTimbrador')
		
		cfdi=cfdiTimbrador.timbrar(cfdi,"PAP830101CR3", "yqjvqfofb")
		//println cfdi.comprobante
		println cfdi.xmlName
		return cfdi
		
	}

    Cfdi generarComprobanteOld(def nominaEmpleadoId) {
		def fecha=new Date()
		def nominaEmpleado=NominaPorEmpleado.get(nominaEmpleadoId)
		def empresa=nominaEmpleado.nomina.empresa
		def empleado=nominaEmpleado.empleado
		
		Folio folio=Folio.findOrSaveWhere(empresa:empresa,serie:'NOMINA_CFDI')
		folio.next()
		
		final ComprobanteDocument document=ComprobanteDocument.Factory.newInstance()
		final Comprobante comprobante=document.addNewComprobante()
		CFDIUtils.depurar(document)
		comprobante.serie='NOMINA_CFDI'
		comprobante.folio=folio.next().toString()
		comprobante.setVersion("3.2")
		comprobante.setFecha(CFDIUtils.toXmlDate(fecha).getCalendarValue())
		comprobante.setFormaDePago("PAGO EN UNA SOLA EXHIBICION")
		comprobante.setMetodoDePago(nominaEmpleado.nomina.formaDePago)
		comprobante.setMoneda(Currency.getInstance(new Locale("es","mx")).currencyCode)
		comprobante.setTipoCambio(1.0)
		
		comprobante.setTipoDeComprobante(TipoDeComprobante.EGRESO)
		comprobante.setLugarExpedicion(empresa.direccion.pais)
		//comprobante.addNewEmisor()
		Emisor emisor=CFDIUtils.registrarEmisor(comprobante, empresa)
		Receptor receptor=CFDIUtils.registrarReceptor(comprobante, nominaEmpleado.empleado)
		
		//Importes
		comprobante.setSubTotal(nominaEmpleado.total)
		comprobante.setDescuento(nominaEmpleado.conceptos.sum{
			def vv=0.0
			if(it.concepto.tipo=='DEDUCCION' && it.concepto.claveSat!=2) {
				vv+=it.importeGravado+it.importeExcento
			}
			return vv
		})
		comprobante.setMotivoDescuento("Deducciones nómina")
		comprobante.setTotal(nominaEmpleado.total)
		
		//Conceptos
		Conceptos conceptos=comprobante.addNewConceptos()
		Concepto c=conceptos.addNewConcepto();
		c.setCantidad(1);
		c.setUnidad("Servicio");
		c.setNoIdentificacion('CARGO');
		c.setDescripcion(nominaEmpleado.comentario);
		c.setValorUnitario(nominaEmpleado.percepciones);
		c.setImporte(nominaEmpleado.percepciones);
		
		//Impuestos
		
		comprobante.setNoCertificado(empresa.numeroDeCertificado)
		
		NominaDocument nominaDocto=NominaDocument.Factory.newInstance()
		Nomina nomina=nominaDocto.addNewNomina()
		
		//nomina.set
		XmlCursor nominaCursor=nomina.newCursor()
		
		nomina.with{
			registroPatronal=empresa.registroPatronal
			numEmpleado=empleado.numeroDeTrabajador
			cURP=empleado.curp
			setCURP("")
			tipoRegimen=empleado.perfil.regimenContratacion.clave
			numSeguridadSocial=empleado.seguridadSocial.numero
			setAntiguedad(nominaEmpleado.antiguedad)
			setFechaInicioRelLaboral(CFDIUtils.toXmlDate(nominaEmpleado.alta).getCalendarValue())
			setFechaPago(CFDIUtils.toXmlDate(nominaEmpleado.nomina.pago).getCalendarValue())
			setFechaInicialPago(CFDIUtils.toXmlDate(nominaEmpleado.nomina.periodo.fechaInicial).getCalendarValue())
			setFechaFinalPago(CFDIUtils.toXmlDate(nominaEmpleado.nomina.periodo.fechaFinal).getCalendarValue())
			setNumDiasPagados(nominaEmpleado.nomina.diasPagados as BigDecimal)
			setDepartamento(empleado.departamento.clave)
			setBanco(empleado.salario.banco?.clave)
			setTipoJornada(empleado.perfil.jornada)
			setPeriodicidadPago(empleado.salario.periodicidad)
			setRiesgoPuesto(empleado.perfil.riesgoPuesto.clave)
			setSalarioBaseCotApor(nominaEmpleado.salarioDiarioBase)
			setSalarioDiarioIntegrado(nominaEmpleado.salarioDiarioIntegrado)
		}
		
		// Percepciones
		Percepciones per=nomina.addNewPercepciones()
		per.totalGravado=nominaEmpleado.percepcionesGravadas
		per.totalExento=nominaEmpleado.percepcionesExcentas
		
		nominaEmpleado.conceptos.each{
			if(it.concepto.tipo=='PERCEPCION') {
				Percepcion pp=per.addNewPercepcion()
				pp.setTipoPercepcion((int)it.concepto.claveSat)
				
				pp.setClave(it.concepto.clave)
				pp.setConcepto(it.concepto.descripcion)
				pp.setImporteGravado(it.importeGravado)
				pp.setImporteExento(it.importeExcento)
			}
		}
		
		// Deducciones
		Deducciones ded=nomina.addNewDeducciones()
		ded.totalGravado=nominaEmpleado.deduccionesGravadas
		ded.totalExento=nominaEmpleado.totalExcento
		
		nominaEmpleado.conceptos.each{
			if(it.concepto.tipo=='DEDUCCION') {
				Deduccion dd=per.addNewPercepcion()
				dd.setTipoPercepcion(it.concepto.claveSat)
				dd.setClave(it.concepto.clave)
				dd.setConcepto(it.concepto.descripcion)
				dd.setImporteGravado(it.importeGravado)
				dd.setImporteExento(it.importeExcento)
			}
		}
		
		/*Incapacidades PENDIENTE
		Incapacidades incapacidades=null
		nominaEmpleado.conceptos.each {
			if([14,6].contains(it.concepto.claveSat)) {
				
				if(incapacidades==null)
					incapacidades=nomina.addNewIncapacidades()
					
				Incapacidad incapacidad=incapacidades.addNewIncapacidad()
				//incapacidad.setTipoIncapacidad(0)
				//incapacidad.setDiasIncapacidad(null)
				incapacidad.setDescuento(it.total)
			}
		}*/
		
		
		Complemento complemento=comprobante.addNewComplemento()
		//complemento.set(nomina)
		complemento.newCursor()
		//nomina.domNode.parentNode.textContent
		
		//complemento.
		comprobante.setSello(cfdiSellador.sellar(empresa.getPrivateKey(),document))
		byte[] encodedCert=Base64.encode(empresa.getCertificado().getEncoded())
		comprobante.setCertificado(new String(encodedCert))
		
		//Impuestos
		Impuestos impuestos=comprobante.addNewImpuestos()
		impuestos.setTotalImpuestosRetenidos(nomina.getDeducciones().getTotalExento()+nomina.getDeducciones().getTotalGravado())
		
		XmlOptions options = new XmlOptions()
		options.setCharacterEncoding("UTF-8")
		options.put( XmlOptions.SAVE_INNER )
		options.put( XmlOptions.SAVE_PRETTY_PRINT )
		options.put( XmlOptions.SAVE_AGGRESSIVE_NAMESPACES )
		options.put( XmlOptions.SAVE_USE_DEFAULT_NAMESPACE )
		options.put(XmlOptions.SAVE_NAMESPACES_FIRST)
		ByteArrayOutputStream os=new ByteArrayOutputStream()
		document.save(os, options)
		
		Cfdi cfdi=new Cfdi(comprobante)
		cfdi.setXml(os.toByteArray())
		cfdi.setXmlName("$cfdi.receptorRfc-$cfdi.serie-$cfdi.folio"+".xml")
		//Validacion del comprobante
		StringUtils.leftPad("1", 3, '0')
		validarDocumento(document)
		cfdi.save(failOnError:true)
		
    }
	
	void validarDocumento(ComprobanteDocument document) {
		List<XmlValidationError> errores=findErrors(document);
		if(errores.size()>0){
			StringBuffer buff=new StringBuffer();
			for(XmlValidationError e:errores){
				buff.append(e.getMessage()+"\n");
			}
			throw new CfdiException(message:"Datos para generar el comprobante fiscal (CFD) incorrectos "+buff.toString());
		}
	}
	
	List findErrors(final XmlObject node){
		final XmlOptions options=new XmlOptions();
		final List errors=new ArrayList();
		options.setErrorListener(errors);
		node.validate(options);
		return errors;
		
	}
}
