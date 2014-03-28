package com.luxsoft.sw4

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;

import org.apache.commons.lang.exception.ExceptionUtils;


class Empresa implements Serializable{
	
	String clave
	String nombre
	String rfc
	Direccion direccion
	String registroPatronal
    String regimen
	
	String numeroDeCertificado
	byte[] certificadoDigital
	byte[] certificadoDigitalPfx
	byte[] llavePrivada
	String passwordPfx
    
	Date dateCreated
	Date lastUpdated
	
	X509Certificate certificado
	PrivateKey privateKey
	
	static embedded = ['direccion']

    static constraints = {
		clave(blank:false,minSize:3,maxSize:15,unique:true)
		nombre(blank:false,maxSize:255,unique:true)
		rfc(blank:false,minSize:12,maxSize:13)
		direccion(nullable:false)
		registroPatronal(size:1..20)
		regimen (blank:false,maxSize:300)
		numeroDeCertificado(nullable:true,minSize:1,maxSize:20)
		certificadoDigital(nullable:true,maxSize:1024*1024*2)
		certificadoDigitalPfx(nullable:true,maxSize:1024*1024*2)
		llavePrivada(nullable:true,maxSize:1024*1024*2)
		passwordPfx nullable:true 
    }
	
	static mapping = {
		
	}
	static transients = ['certificado','certificadoPfx','privateKey']
	
	X509Certificate getCertificado(){
		
		if(certificadoDigital && !certificado){
			//assert certificadoDigital,'Debe cargar el binario del certificado '
			try {
				
				log.info('Cargando certificado digital en formato X509')
				CertificateFactory fact= CertificateFactory.getInstance("X.509","BC")
				InputStream is=new ByteArrayInputStream(certificadoDigital)
				certificado = (X509Certificate)fact.generateCertificate(is)
				certificado.checkValidity()
					//is.closeQuietly();
				is.close();
				this.certificado=certificado
			} catch (Exception e) {
				e.printStackTrace()
				println 'Error tratando de leer certificado en formato X509 :'+ExceptionUtils.getRootCauseMessage(e)
			}
			
			
		}
		
		return certificado;
	}
	
	String getCertificadoInfo(){
		return "$certificado?.subjectX500Principal"
	}
	
	PrivateKey getPrivateKey(){
		if(!privateKey && llavePrivada){
			try {
				final byte[] encodedKey=llavePrivada
				PKCS8EncodedKeySpec keySpec=new PKCS8EncodedKeySpec(encodedKey)
				final  KeyFactory keyFactory=KeyFactory.getInstance("RSA","BC")
				this.privateKey=keyFactory.generatePrivate(keySpec)
			} catch (Exception e) {
				e.printStackTrace()
				println 'Error tratando de leer llave privada :'+ExceptionUtils.getRootCauseMessage(e)
			}
			
		}
		return privateKey;
	}
	
	String toString(){
		return "$nombre ($rfc)"
	}
}
