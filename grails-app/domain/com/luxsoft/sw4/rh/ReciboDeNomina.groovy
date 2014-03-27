package com.luxsoft.sw4.rh

import mx.gob.sat.cfd.x3.ComprobanteDocument
import mx.gob.sat.cfd.x3.ComprobanteDocument.Comprobante
import com.luxsoft.sw4.cfdi.TimbreFiscal

class ReciboDeNomina {
	
	Empleado empleado
	Byte[] cfdi
	String folio
	
	
	ComprobanteDocument comprobanteDocument
	
	static transients = ['comprobanteDocument','comprobante','timbreFiscal']
	
	Date dateCreated
	Date lastUpdated

    static constraints = {
		cfdi maxSize:(1024 * 512)  // 50kb para almacenar el xml
    }
	
	public ComprobanteDocument getComprobanteDocument(){
		if(this.comprobanteDocument==null){
			loadComprobante()
		}
		return this.comprobanteDocument
	}
	
	public Comprobante getComprobante(){
		return getComprobanteDocument().getComprobante()
	}
	
	void loadComprobante(){
		ByteArrayInputStream is=new ByteArrayInputStream(getXml())
		this.comprobanteDocument=ComprobanteDocument.Factory.parse(is)
	}
	
	TimbreFiscal getTimbreFiscal(){
		return new TimbreFiscal(getComprobante())
	}
	
}
