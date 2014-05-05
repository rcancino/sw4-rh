package com.luxsoft.sw4.rh

import java.sql.Time
import java.util.Date

import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode

import org.grails.databinding.BindingFormat

@ToString(includes='fecha',includeNames=true,includePackage=false)
@EqualsAndHashCode(includes='fecha')
class AsistenciaDet {
	
	@BindingFormat("dd/MM/yyyy")
	Date fecha

	Time entrada1
	Time salida1
	
	Time entrada2
	Time salida2

	Time entrada3
	Time salida3
	
	String comentario
	
	Date dateCreated
	Date lastUpdated
	
	static belongsTo = [asistencia:Asistencia]
	
	static embedded = ['periodo']

    static constraints = {
		comentario nullable:true
		entrada1 nullable:true
		entrada2 nullable:true
		entrada3 nullable:true
		salida1 nullable:true
		salida2 nullable:true
		salida3 nullable:true
    }
	static mapping = {
		fecha type:'date'
		entrada1 type:'time'
		entrada2 type:'time'
		entrada3 type:'time'

		salida1 type:'time'
		salida2 type:'time'
		salida3 type:'time'
	}
}
