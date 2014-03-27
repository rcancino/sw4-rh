package com.luxsoft.sw4.rh


import com.luxsoft.sw4.cfdi.CFDIUtils
import com.luxsoft.sw4.cfdi.Folio

import mx.gob.sat.cfd.x3.ComprobanteDocument
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Complemento;
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Conceptos;
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Conceptos.Concepto;
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Emisor;
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Receptor;
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.TipoDeComprobante;
import mx.gob.sat.nomina.NominaDocument;
import mx.gob.sat.nomina.NominaDocument.Nomina;
import mx.gob.sat.nomina.NominaDocument.Nomina.Deducciones;
import mx.gob.sat.nomina.NominaDocument.Nomina.Deducciones.Deduccion;
import mx.gob.sat.nomina.NominaDocument.Nomina.Incapacidades;
import mx.gob.sat.nomina.NominaDocument.Nomina.Incapacidades.Incapacidad;
import mx.gob.sat.nomina.NominaDocument.Nomina.Percepciones;
import mx.gob.sat.nomina.NominaDocument.Nomina.Percepciones.Percepcion;
import grails.transaction.Transactional

@Transactional
class ReciboDeNominaService {

    ReciboDeNomina generarRecibo(def nominaEmpleadoId) {
		
		def fecha=new Date()
    	def nominaEmpleado=NominaPorEmpleado.get(nominaEmpleadoId)
		def empresa=nominaEmpleado.nomina.empresa
		def empleado=nominaEmpleado.empleado
		
		Folio folio=Folio.findOrSaveWhere(empresa:empresa.clave,serie:'NOMINA_CFDI')
		def recibo=new ReciboDeNomina()
		recibo.folio=folio.folio
		
		final ComprobanteDocument document=ComprobanteDocument.Factory.newInstance()
		final Comprobante comprobante=document.addNewComprobante()
		CFDIUtils.depurar(document)
		
		comprobante.setVersion("3.2")
		comprobante.setFecha(CFDIUtils.toXmlDate(fecha).getCalendarValue())
		comprobante.setFormaDePago("PAGO EN UNA SOLA EXHIBICION")
		comprobante.setMetodoDePago(nominaEmpleado.nomina.formaDePago)
		comprobante.setMoneda(Currency.getInstance(new Locale("es","mx")))
		comprobante.setTipoCambio(1.0)
		
		comprobante.setTipoDeComprobante(TipoDeComprobante.EGRESO)
		comprobante.setLugarExpedicion(empresa.direccion.pais)
		//comprobante.addNewEmisor()
		Emisor emisor=CFDIUtils.registrarEmisor(comprobante, empresa)
		Receptor receptor=CFDIUtils.registrarReceptor(comprobante, nominaEmpleado.empleado)
		
		//Importes
		comprobante.setSubTotal(nominaEmpleado.total)
		comprobante.setDescuento(nominaEmpleado.conceptos.sum{ concepto->
			
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
		
		nomina.with{
			registroPatronal=empresa.registroPatronal
			numEmpleado=empleado.numeroDeTrabajador
			cURP=empleado.curp
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
				pp.setTipoPercepcion(it.concepto.claveSat)
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
		
		//Incapacidades
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
		}
		
		
		Complemento complemento=comprobante.addNewComplemento()
		complemento.set(nomina)

    }
}
