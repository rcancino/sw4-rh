package com.luxsoft.sw4.cfdi

import mx.gob.sat.cfd.x3.ComprobanteDocument;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

/**
 * Generador de cadena original
 * 
 * @author Ruben Cancino 
 *
 */
class CfdiCadenaBuilder implements ResourceLoaderAware{
	
	File xsltFile
	
	
	
	/**
	 * Genera la cadean original de un comprobante fiscal digital
	 * 
	 * @return La cadena original
	 */
	String generarCadena(ComprobanteDocument document){
		TransformerFactory factory=TransformerFactory.newInstance()
		xsltFile=resourceLoader.getResource("sat/cadenaoriginal_3_2.xslt").getFile()
		assert xsltFile.exists(),"No existe el archivo xslt para la cadena del sat: "+xsltFile.getPath()
		StreamSource source=new StreamSource(xsltFile);
		Transformer transformer=factory.newTransformer(source);
		Writer writer=new StringWriter();
		StreamResult out=new StreamResult(writer);
		Source so=new DOMSource(document.getDomNode());
		transformer.transform(so, out);
		return writer.toString();
		
	}
	
	ResourceLoader resourceLoader

	@Override
	public void setResourceLoader(ResourceLoader arg0) {
		resourceLoader=arg0
		
	}

}
