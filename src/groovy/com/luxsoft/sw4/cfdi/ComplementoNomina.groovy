package com.luxsoft.sw4.cfdi

import java.text.MessageFormat;

import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante;
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante.Complemento;
import mx.gob.sat.nomina.NominaDocument;
import mx.gob.sat.nomina.NominaDocument.Nomina;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.xmlbeans.XmlObject;

class ComplementoNomina {
	
	Nomina nomina
	NominaDocument nominaDocument
	
	public ComplementoNomina(Comprobante cfdi){
		
		
		
		Complemento complemento=cfdi.getComplemento();
		if(complemento!=null){
			//println complemento
			String queryExpression ="declare namespace nomina='http://www.sat.gob.mx/nomina';" +
					"\$this/nomina:Nomina"
			XmlObject[] res=complemento.selectPath(queryExpression);
			if(res.length>0){
				XmlObject n1=res[0];
				nominaDocument=NominaDocument.Factory.parse(n1.domNode)
				nomina=nominaDocument.getNomina()
				
			}
		}
	}
	
	
	
	

}
