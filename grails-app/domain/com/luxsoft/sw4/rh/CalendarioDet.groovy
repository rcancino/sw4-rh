package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Periodo
import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode

@ToString(includes='folio,inicio,fin',includeNames=true,includePackage=false)
@EqualsAndHashCode(includes='folio')
class CalendarioDet {
	
	//static searchable = true
	
	Integer folio
	
	Date fechaDePago
	
	Date inicio
	
	Date fin
	
	Periodo asistencia
	
	static embedded = ['asistencia']
	
	static belongsTo =[calendario:Calendario]

    static constraints = {
		asistencia nullable:true
		fechaDePago nullable:true
    }
	
	static mapping = {
		inicio type:'date'
		fin type:'date'
		fechaDePago:'date'
		sort "inicio"
	}
}
