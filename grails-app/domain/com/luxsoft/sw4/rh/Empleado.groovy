package com.luxsoft.sw4.rh

import java.util.Date
import groovy.transform.EqualsAndHashCode

import groovy.transform.EqualsAndHashCode

import com.luxsoft.sw4.Empresa



@EqualsAndHashCode(includes='curp,rfc')
class Empleado  implements Serializable{
	
	static searchable = true

	String apellidoPaterno
	String apellidoMaterno
	String nombres
	String clave
	String curp
	String rfc
    Date alta
	String sexo
	String status
	Date fechaDeNacimiento
	Boolean activo
	
	String nombre
	//List contactos

    Date dateCreated
    Date lastUpdated
	
	static hasOne=[perfil:PerfilDeEmpleado
		,salario:Salario
		,seguridadSocial:SeguridadSocial
		,datosPersonales:DatosPersonales
		,contacto:EmpleadoContacto
		,baja:BajaDeEmpleado]

	/*
	static hasMany = [contactos:EmpleadoContacto]
	
	static mapping = {
		contactos cascade: "all-delete-orphan"
	}
	*/

    static constraints = {
	
    	apellidoPaterno nullable:true,maxSize:150
    	apellidoMaterno nullable:true,maxSize:150
    	nombres blank:false,maxSize:300
		clave nullable:true
    	curp size:1..25,unique:true
    	rfc  blank:false,minSize:12,maxSize:13
		sexo inList:['M','F']
		status inList:['ALTA','BAJA','LICENCIA','FINIQUITO','REINGRESO']
		perfil nullable:true
		salario nullable:true
		seguridadSocial nullable:true
		datosPersonales nullable:true
		contacto nullable:true
		baja nullable:true
		activo nullable:true
    }
	
	static transients = ['nombre']

	String getNombre() {
		return "$apellidoPaterno $apellidoMaterno $nombres"
	}
	
    String toString(){
    	return "$apellidoPaterno $apellidoMaterno $nombres "
    }
	
}
