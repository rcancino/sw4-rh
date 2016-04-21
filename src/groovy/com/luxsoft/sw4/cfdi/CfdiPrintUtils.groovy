package com.luxsoft.sw4.cfdi


import com.luxsoft.sw4.rh.NominaPorEmpleado
import com.luxsoft.sw4.rh.Nomina
import java.security.Policy.Parameters;
import java.text.MessageFormat;

import javax.imageio.ImageIO;

import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante;
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Emisor;
import mx.gob.sat.cfd.x3.TUbicacion;
import mx.gob.sat.nomina.NominaDocument;
import mx.gob.sat.nomina.NominaDocument.Nomina;

import org.apache.commons.lang.StringUtils;





class CfdiPrintUtils {
	
	static resolverParametros(Comprobante comprobante,Nomina nomina,NominaPorEmpleado nominaPorEmpleado){
		//NominaDocument nominaDocto=NominaDocument.Factory.parse(comprobante.getCo)
		
		
		
		def parametros=[:]
		Emisor emisor=comprobante.getEmisor();
		parametros.put("EMISOR_NOMBRE", 	emisor.getNombre());
		parametros.put("EMISOR_RFC", 		emisor.getRfc());
		String pattern="{0} {1}  {2}  {3}" +
				"\n{4}  {5}  {6}";
		String direccionEmisor=MessageFormat.format(pattern
				,emisor.getDomicilioFiscal().getCalle()
				,emisor.getDomicilioFiscal().getNoExterior()
				,StringUtils.defaultIfEmpty(emisor.getDomicilioFiscal().getNoInterior(),"")
				
				,emisor.getDomicilioFiscal().getColonia()
				
				,emisor.getDomicilioFiscal().getMunicipio()
				
				,emisor.getDomicilioFiscal().getCodigoPostal()
				,emisor.getDomicilioFiscal().getEstado()
				);
		parametros.put("EMISOR_DIRECCION", direccionEmisor);
		parametros.put("EXPEDIDO_DIRECCION", direccionEmisor);
		parametros.put("REGIMEN",comprobante.getEmisor().getRegimenFiscalArray(0).regimen);
		parametros.put("METODO_DE_PAGO", 		comprobante.getMetodoDePago());
		
		// Datos tomados del Comprobante fiscal digital XML
		parametros.put("NOMBRE", 			comprobante.getReceptor().getNombre()); //Recibir como Parametro
		parametros.put("RFC", 				comprobante.getReceptor().getRfc());
		parametros['NUMERO_EMPLEADO']=nomina.numEmpleado
		parametros['NUMERO_IMSS']=nomina.numSeguridadSocial
		parametros['CURP']=nomina.CURP
		parametros['REGIMEN_TRABAJADOR']=nomina.tipoRegimen.toString()
		parametros['EMISOR_RFC']=comprobante.emisor.rfc
		parametros['TIPO_NOMINA']=nomina.tipo
		parametros['PERIOCIDAD_PAGO']=nomina.periodicidadPago
		
		
		parametros.put("SERIE", 			comprobante.getSerie());
		
		parametros.put("FOLIO", 			comprobante.getFolio());
		parametros.put("NUM_CERTIFICADO", 	comprobante.getNoCertificado());
		parametros.put("SELLO_DIGITAL", 	comprobante.getSello());
		parametros.put("IMP_CON_LETRA", 	ImporteALetra.aLetra(comprobante.getTotal()));
		
		parametros['REGISTRO_PATRONAL']=nomina.registroPatronal
		
		
		
		parametros.put("NFISCAL", 			comprobante.getSerie()+" - "+comprobante.getFolio());
		//parametros['PUESTO']=nomina.puesto
		//parametros['DEPARTAMENTO']=nomina.departamento
		//parametros['SUCURSAL']=nomina.departamento
		parametros['RIESGO_PUESTO']=''+nomina.riesgoPuesto
		parametros['TIPO_JORNADA']=nomina.tipoJornada
		//parametros['DEPARTAMENTO']=nomina.departamento
		parametros['FECHA_INGRESO_LABORAL']=nomina.fechaInicioRelLaboral?.format("yyyy-MM-dd")
		parametros['ANTIGUEDAD']=''+nomina.antiguedad
		parametros['TIPO_CONTRATO']=nomina.tipoContrato
		//parametros['SALARIO_DIARIO_BASE']=nomina.salarioBaseCotApor
		//parametros['SALARIO_DIARIO_INTEGRADO']=nomina.salarioDiarioIntegrado


		
		parametros['FECHA_INICIAL']=nomina.fechaInicialPago?.format("yyyy-MM-dd")
		parametros['FECHA_FINAL']=nomina.fechaFinalPago?.format("yyyy-MM-dd")
		parametros['DIAS_PAGADOS']=nomina.numDiasPagados
		parametros['CLABE']=nomina.CLABE
		parametros.put("TOTAL", comprobante.getTotal());
		parametros['COMENTARIO_NOM']='Nómina'
		
		//parametros['DIAS_HORAS_EXTRA']=nomina?.horasExtras?.horasExtraArray[0]?.dias
		
		
		
		/*
		//parametros.put("FECHA", 			comprobante.getFecha().getTime());
		parametros.put("NFISCAL", 			comprobante.getSerie()+" - "+comprobante.getFolio());
		parametros.put("IMPORTE", 			comprobante.getSubTotal());
		parametros.put("IVA", 			comprobante.getImpuestos().getTotalImpuestosTrasladados());
		parametros.put("TOTAL", 			comprobante.getTotal());
		parametros.put("RECEPTOR_DIRECCION", 		getDireccionEnFormatoEstandar(comprobante.getReceptor().getDomicilio()) );
		parametros.put("NUM_CTA_PAGO", 		comprobante.getNumCtaPago());
		
		//Datos tomado de la aplicacion
		
		parametros['FORMA_DE_PAGO']=comprobante.formaDePago
		parametros['PINT_IVA']='16 '
		parametros['TIPO_DE_COMPROBANTE']=comprobante.tipoDeComprobante.toString().toUpperCase()
		
		parametros.put("DESCUENTOS", 	comprobante.getDescuento()?:0.0);
		
		
		if(comprobante.getReceptor().rfc=='XAXX010101000'){
			parametros.put("IMPORTE", 			comprobante.getTotal());
		}
		
		
		
		if (emisor.getExpedidoEn() != null){
			TUbicacion expedido=emisor.getExpedidoEn();
		
			String pattern2="{0} {1}  {2}  {3}" +
				"\n{4}  {5}  {6}";
			String expedidoDir=MessageFormat.format(pattern2
				,expedido.getCalle()
				,expedido.getNoExterior()
				,StringUtils.defaultIfEmpty(expedido.getNoInterior(),"")
				,expedido.getColonia()
				,expedido.getMunicipio()
				,expedido.getCodigoPostal()
				,expedido.getEstado()
				);
			parametros.put("EXPEDIDO_DIRECCION", expedidoDir);
		}
		*/	
		
		//Especiales para CFDI
			
		
			//println 'Imagen generada: '+img
			def img=QRCodeUtils.generarQR(comprobante)
			//println 'Imagen generada: '+img
			parametros.put("QR_CODE",img);
			//parametros.put("QR_CODE",QRCodeUtils.getQCode(cfdi.getComprobante()))
			TimbreFiscal timbre=new TimbreFiscal(comprobante)
			parametros.put("FECHA_TIMBRADO", timbre.FechaTimbrado);
			parametros.put("FOLIO_FISCAL", timbre.UUID);
			parametros.put("SELLO_DIGITAL_SAT", timbre.selloSAT);
			parametros.put("CERTIFICADO_SAT", timbre.noCertificadoSAT);
			parametros.put("CADENA_ORIGINAL_SAT", timbre.cadenaOriginal());
		
			//parametros["EMPRESA_LOGO"]=new File("z://siipapex/etc/empresaFacLogo.jpg")
			
			
			parametros['SALARIO_DIARIO_BASE']=nomina.salarioBaseCotApor as String
			parametros['SALARIO_DIARIO_INTEGRADO']=nomina.salarioDiarioIntegrado

			parametros['PUESTO']=nomina.puesto
			parametros['DEPARTAMENTO']=nomina.departamento

		if(nominaPorEmpleado.asistencia){
			//parametros['DIAS_PAGADOS']=nomina.numDiasPagados as String
			
			
			
			def diasTrabajados=0
			def faltas=0
			if(nominaPorEmpleado){
				if(nominaPorEmpleado.asistencia){
					if(!nominaPorEmpleado.empleado.controlDeAsistencia){
					      diasTrabajados=nominaPorEmpleado.diasTrabajados+nominaPorEmpleado.vacaciones
				   		//diasTrabajados= nominaPorEmpleado.diasTrabajados+nominaPorEmpleado.vacaciones-(nominaPorEmpleado.asistencia.faltasManuales+(nominaPorEmpleado.asistencia.faltasManuales*0.167)+ nominaPorEmpleado.incapacidades)
				   		faltas=	(nominaPorEmpleado.asistencia.faltasManuales+(nominaPorEmpleado.asistencia.faltasManuales*0.167))
					}else{


				  		if(nominaPorEmpleado.empleado.alta<=nominaPorEmpleado.asistencia.calendarioDet.inicio){
							// diasTrabajados=nominaPorEmpleado.diasDelPeriodo-(nominaPorEmpleado.faltas+ nominaPorEmpleado.fraccionDescanso + nominaPorEmpleado.incapacidades)
							// faltas=(nominaPorEmpleado.faltas+ nominaPorEmpleado.fraccionDescanso + nominaPorEmpleado.incapacidades)
					 		diasTrabajados=nominaPorEmpleado.diasTrabajados+nominaPorEmpleado.vacaciones
				  	 		faltas=nominaPorEmpleado.diasDelPeriodo-nominaPorEmpleado.diasTrabajados-nominaPorEmpleado.vacaciones
				  		}else{
				  			diasTrabajados=nominaPorEmpleado.diasTrabajados //-(nominaPorEmpleado.asistencia.faltasManuales+nominaPorEmpleado.incapacidades)
					  		faltas= nominaPorEmpleado.faltas+nominaPorEmpleado.fraccionDescanso+nominaPorEmpleado.incapacidades //(nominaPorEmpleado.asistencia.faltasManuales+nominaPorEmpleado.incapacidades)
				  		}
					}
				}
			}
			parametros['DIAS_TRABAJADOS']=diasTrabajados

			parametros['SUB_EMPLEO_APLIC']=nominaPorEmpleado.subsidioEmpleoAplicado


//			if(nominaPorEmpleado?.asistencia?.diasTrabajados>0){
//				parametros['DIAS_TRABAJADOS']=(com.luxsoft.sw4.MonedaUtils.round(nominaPorEmpleado.asistencia.diasTrabajados)) as String
//			}else{
				   //(com.luxsoft.sw4.MonedaUtils.round(nominaPorEmpleado.diasDelPeriodo)) as String
//			}			

			parametros['FALTAS']= faltas //nominaPorEmpleado.faltas+nominaPorEmpleado.incapacidades
		}
		
		
		return parametros;
	}
	
	static String getDireccionEnFormatoEstandar(TUbicacion u){
		if(u==null) return ""
		String pattern="{0} {1} {2} {3}" +
				" {4} {5} {6}" +
				" {7} {8}";
		//StringUtils.
		return MessageFormat.format(pattern
				,u.getCalle() !=null?u.getCalle():""
				,(u.getNoExterior()!=null && !u.getNoExterior().equals(".") )?"NO."+u.getNoExterior():""
				,(u.getNoInterior()!=null && !u.getNoInterior().equals(".") )?"INT."+u.getNoInterior():""
				,u.getColonia()!=null?","+u.getColonia():""
				,u.getCodigoPostal() !=null?","+u.getCodigoPostal():""
				,u.getMunicipio()!=null?","+u.getMunicipio():""
				,u.getLocalidad()!=null?","+u.getLocalidad():""
				,u.getEstado()!=null?","+u.getEstado()+",":""
				,u.getPais()!=null?u.getPais():""
				);
		
	}
	
	
	static resolverParametros2(Comprobante comprobante,Nomina nomina,NominaPorEmpleado nominaPorEmpleado){
		
		//NominaDocument nominaDocto=NominaDocument.Factory.parse(comprobante.getCo)
		
		def parametros=[:]
		Emisor emisor=comprobante.getEmisor();
		parametros.put("EMISOR_NOMBRE", 	emisor.getNombre());
		parametros.put("EMISOR_RFC", 		emisor.getRfc());
		String pattern="{0} {1}  {2}  {3}" +
				"\n{4}  {5}  {6}";
		String direccionEmisor=MessageFormat.format(pattern
				,emisor.getDomicilioFiscal().getCalle()
				,emisor.getDomicilioFiscal().getNoExterior()
				,StringUtils.defaultIfEmpty(emisor.getDomicilioFiscal().getNoInterior(),"")
				,emisor.getDomicilioFiscal().getColonia()
				,emisor.getDomicilioFiscal().getMunicipio()
				,emisor.getDomicilioFiscal().getCodigoPostal()
				,emisor.getDomicilioFiscal().getEstado()
				);
		parametros.put("EMISOR_DIRECCION", direccionEmisor);
		parametros.put("EXPEDIDO_DIRECCION", direccionEmisor);
		parametros.put("REGIMEN",comprobante.getEmisor().getRegimenFiscalArray(0).regimen);
		parametros.put("METODO_DE_PAGO", 		comprobante.getMetodoDePago());
		
		// Datos tomados del Comprobante fiscal digital XML
		parametros.put("NOMBRE", 			comprobante.getReceptor().getNombre()); //Recibir como Parametro
		parametros.put("RFC", 				comprobante.getReceptor().getRfc());
		parametros['NUMERO_EMPLEADO']=nomina.numEmpleado
		parametros['NUMERO_IMSS']=nomina.numSeguridadSocial
		parametros['CURP']=nomina.CURP
		parametros['REGIMEN_TRABAJADOR']=nomina.tipoRegimen.toString()
		parametros['EMISOR_RFC']=comprobante.emisor.rfc
		parametros['PERIOCIDAD_PAGO']=nomina.periodicidadPago
		parametros.put("SERIE", 			comprobante.getSerie());
		parametros.put("FOLIO", 			comprobante.getFolio());
		parametros.put("NUM_CERTIFICADO", 	comprobante.getNoCertificado());
		parametros.put("SELLO_DIGITAL", 	comprobante.getSello());
		parametros.put("IMP_CON_LETRA", 	ImporteALetra.aLetra(comprobante.getTotal()));
		parametros['REGISTRO_PATRONAL']=nomina.registroPatronal
		parametros.put("NFISCAL", 			comprobante.getSerie()+": "+nominaPorEmpleado.nomina.folio+" - "+comprobante.getFolio());
		
		parametros['RIESGO_PUESTO']=''+nomina.riesgoPuesto
		parametros['TIPO_JORNADA']=nomina.tipoJornada
		
		parametros['FECHA_INGRESO_LABORAL']=nomina.fechaInicioRelLaboral?.format("yyyy-MM-dd")
		parametros['ANTIGUEDAD']=''+nomina.antiguedad
		parametros['TIPO_CONTRATO']=nomina.tipoContrato
		
		parametros['FECHA_INICIAL']=nomina.fechaInicialPago?.format("yyyy-MM-dd")
		parametros['FECHA_FINAL']=nomina.fechaFinalPago?.format("yyyy-MM-dd")
		
		parametros['CLABE']=nomina.CLABE
		parametros['TOTAL']=comprobante.getTotal() //as String
		parametros['COMENTARIO_NOM']='N�mina'
		def img=QRCodeUtils.generarQR(comprobante)
		parametros.put("QR_CODE",img);
		TimbreFiscal timbre=new TimbreFiscal(comprobante)
		parametros.put("FECHA_TIMBRADO", timbre.FechaTimbrado);
		parametros.put("FOLIO_FISCAL", timbre.UUID);
		parametros.put("SELLO_DIGITAL_SAT", timbre.selloSAT);
		parametros.put("CERTIFICADO_SAT", timbre.noCertificadoSAT);
		parametros.put("CADENA_ORIGINAL_SAT", timbre.cadenaOriginal());
		
		parametros['SALARIO_DIARIO_BASE']=nomina.salarioBaseCotApor as String
		parametros['SALARIO_DIARIO_INTEGRADO']=nomina.salarioDiarioIntegrado as String
		
		parametros['TIPO_NOMINA']=nominaPorEmpleado.nomina.tipo
		parametros['SUCURSAL']=nominaPorEmpleado.empleado.perfil.ubicacion.clave
		parametros['PUESTO']=nomina.puesto
		parametros['DEPARTAMENTO']=nomina.departamento

		if(nominaPorEmpleado.asistencia){
			//parametros['DIAS_PAGADOS']=nomina.numDiasPagados as String
			//println 'Agregando parametros por asistencia....'
			//parametros['SUCURSAL']=nominaPorEmpleado.empleado.perfil.ubicacion.clave
			//parametros['PUESTO']=nomina.puesto
			//parametros['DEPARTAMENTO']=nomina.departamento
/*
			if(nominaPorEmpleado?.asistencia?.diasTrabajados>0){
				parametros['DIAS_TRABAJADOS']=(com.luxsoft.sw4.MonedaUtils.round(nominaPorEmpleado.asistencia.diasTrabajados)) as String
			}else{
				parametros['DIAS_TRABAJADOS']=(com.luxsoft.sw4.MonedaUtils.round(nominaPorEmpleado.diasDelPeriodo)) as String
			}			

			def faltas=(com.luxsoft.sw4.MonedaUtils.round(nominaPorEmpleado.faltas+nominaPorEmpleado.incapacidades)) as String
			parametros['FALTAS']=faltas
			*/
			def diasTrabajados=0
			def faltas=0
			if(nominaPorEmpleado){
				if(nominaPorEmpleado.asistencia){
					if(!nominaPorEmpleado.empleado.controlDeAsistencia){
				   		diasTrabajados=nominaPorEmpleado.diasTrabajados+nominaPorEmpleado.vacaciones
				   	//	diasTrabajados= nominaPorEmpleado.diasTrabajados+nominaPorEmpleado.vacaciones-(nominaPorEmpleado.asistencia.faltasManuales+(nominaPorEmpleado.asistencia.faltasManuales*0.167)+ nominaPorEmpleado.incapacidades)
				   		faltas=	(nominaPorEmpleado.asistencia.faltasManuales+(nominaPorEmpleado.asistencia.faltasManuales*0.167)+nominaPorEmpleado.incapacidades)
					}else{
				  		if(nominaPorEmpleado.empleado.alta<=nominaPorEmpleado.asistencia.calendarioDet.inicio){
					  		//diasTrabajados=nominaPorEmpleado.diasDelPeriodo-(nominaPorEmpleado.faltas+ nominaPorEmpleado.fraccionDescanso + nominaPorEmpleado.incapacidades)
					  		//faltas=(nominaPorEmpleado.faltas+ nominaPorEmpleado.fraccionDescanso + nominaPorEmpleado.incapacidades)
					   		diasTrabajados=nominaPorEmpleado.diasTrabajados+nominaPorEmpleado.vacaciones
				  			faltas=nominaPorEmpleado.diasDelPeriodo-nominaPorEmpleado.diasTrabajados-nominaPorEmpleado.vacaciones
				  		}else{
					  		diasTrabajados=nominaPorEmpleado.diasTrabajados //-(nominaPorEmpleado.asistencia.faltasManuales+nominaPorEmpleado.incapacidades)
					  		faltas=nominaPorEmpleado.faltas+nominaPorEmpleado.fraccionDescanso+nominaPorEmpleado.incapacidades  //(nominaPorEmpleado.asistencia.faltasManuales+nominaPorEmpleado.incapacidades)
				  		}
					}
				}			
			}
			parametros['FALTAS']=com.luxsoft.sw4.MonedaUtils.round(faltas,2) as String
			parametros['DIAS_TRABAJADOS']=com.luxsoft.sw4.MonedaUtils.round(diasTrabajados,2) as String
		}	
			parametros['SUB_EMPLEO_APLIC']=nominaPorEmpleado.subsidioEmpleoAplicado

			
		
		
		return parametros;
	}

}
