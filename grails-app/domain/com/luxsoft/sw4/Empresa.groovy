package com.luxsoft.sw4


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
	
	
	static embedded = ['direccion']

    static constraints = {
		clave(blank:false,minSize:3,maxSize:15,unique:true)
		nombre(blank:false,maxSize:255,unique:true)
		rfc(blank:false,minSize:12,maxSize:13)
		direccion(nullable:false)
		registroPatronal(size:1..20)
		regimen (blank:false,maxSize:300)
		numeroDeCertificado(blank:true,minSize:1,maxSize:20)
		certificadoDigital(nullable:true,maxSize:1024*1024*2)
		certificadoDigitalPfx(nullable:true,maxSize:1024*1024*2)
		llavePrivada(nullable:true,maxSize:1024*1024*2)
    }
	
	static mapping = {
		
	}
	
	String toString(){
		return "$nombre ($rfc)"
	}
}
