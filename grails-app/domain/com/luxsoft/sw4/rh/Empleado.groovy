package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Empresa;

class Empleado  implements Serializable{

	String apellidoPaterno
	String apellidoMaterno
	String nombres
	
	String curp
	String rfc
    Date alta
	String sexo
	String status
	String tipo
	

    Date dateCreated
    Date lastUpdated
	
	static hasOne=[perfil:PerfilDeEmpleado]

    static constraints = {
	
    	apellidoPaterno blank:false,maxSize:150
    	apellidoMaterno blank:false,maxSize:150
    	nombres blank:false,maxSize:300
    	curp size:1..25,unique:true
    	rfc  blank:false,minSize:12,maxSize:13
		sexo inList:['M','F']
		status inList:['ALTA','BAJA','LICENCIA','FINIQUITO','REINGRESO']
		tipo inList:['CONFIANZA','SINDICALIZADO']
		perfil nullable:true

    }

    String toString(){
    	return "$nombres $apellidoPaterno $apellidoMaterno"
    }
}
