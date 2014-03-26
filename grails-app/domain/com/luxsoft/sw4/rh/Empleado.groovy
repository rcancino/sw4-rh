package com.luxsoft.sw4.rh

import java.util.Date
import groovy.transform.EqualsAndHashCode
import groovy.transform.EqualsAndHashCode
import com.luxsoft.sw4.Empresa


@EqualsAndHashCode(includes='curp,rfc')
class Empleado  implements Serializable{

	String apellidoPaterno
	String apellidoMaterno
	String nombres
	
	String curp
	String rfc
    Date alta
	String sexo
	String status
	Date fechaDeNacimiento
	

    Date dateCreated
    Date lastUpdated
	
	static hasOne=[perfil:PerfilDeEmpleado,salario:Salario,seguridadSocial:SeguridadSocial,datosPersonales:DatosPersonales]

	

    static constraints = {
	
    	apellidoPaterno blank:true,maxSize:150
    	apellidoMaterno blank:true,maxSize:150
    	nombres blank:false,maxSize:300
    	curp size:1..25,unique:true
    	rfc  blank:false,minSize:12,maxSize:13
		sexo inList:['M','F']
		status inList:['ALTA','BAJA','LICENCIA','FINIQUITO','REINGRESO']
		perfil nullable:true
		salario nullable:true
		seguridadSocial nullable:true
		datosPersonales nullable:true

    }

    String toString(){
    	return "$nombres $apellidoPaterno $apellidoMaterno"
    }
}
