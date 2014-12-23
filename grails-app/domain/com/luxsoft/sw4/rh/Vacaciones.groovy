package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Autorizacion
import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode

import org.grails.databinding.BindingFormat

@ToString(includes='empleado,fecha,comentario,autorizacion',includeNames=true,includePackage=false)
@EqualsAndHashCode(includes="empleado,fecha")
class Vacaciones {
	
	//static searchable = true
	
	Empleado empleado
	
	ControlDeVacaciones control
	
	@BindingFormat("dd/MM/yyyy")
	Date solicitud=new Date()
	
	Autorizacion autorizacion
	
	String comentario
	
	boolean pg=false
	
	boolean acreditada=false
	
	Set dias=new HashSet()
	
	int diasPagados=0

	CalendarioDet calendarioDet
	
	boolean cierreAnual

	Date dateCreated
	Date lastUpdated

	static constraints = {
		control nullable:true
		comentario nullable:true,maxSize:250
		autorizacion nullable:true
		acreditada nullable:true
		calendarioDet nullable:true
		cierreAnual nullable:true
	}
	
	static hasMany = [dias:Date]
	
	//static hasOne = [autorizacion:Autorizacion]

	
	
	static mapping = {
		hasMany joinTable: [name: 'vacaciones_dias',
                           key: 'vacacines_id',
                           column: 'fecha',
                           type: "date"]
		solicitud type:'date'
	}

	

    
}
