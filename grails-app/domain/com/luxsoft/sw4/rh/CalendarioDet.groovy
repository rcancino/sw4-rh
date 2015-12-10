package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Periodo
import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode
import org.grails.databinding.BindingFormat

@ToString(includes='folio,inicio,fin,calendario',includeNames=true,includePackage=false)
@EqualsAndHashCode(includes='folio')
class CalendarioDet {
	
	//static searchable = true
	
	Integer folio
	
	@BindingFormat('dd/MM/yyyy')
	Date fechaDePago
	
	@BindingFormat('dd/MM/yyyy')
	Date inicio
	
	@BindingFormat('dd/MM/yyyy')
	Date fin
	
	Periodo asistencia
	
	Integer bimestre=0

	String mes
	
	static embedded = ['asistencia']
	
	static belongsTo =[calendario:Calendario]

    static constraints = {
		asistencia nullable:true
		fechaDePago nullable:true
		bimestre nullable:true,range:1..6
		mes nullable:true,maxSize:15
    }
	
	static mapping = {
		inicio type:'date'
		fin type:'date'
		fechaDePago:'date'
		sort "inicio"
		calendario lazy : false
	}

	def periodo(){
		return new Periodo(inicio,fin)
	}

	String toString(){
	return "$calendario.tipo $folio  (${inicio.format('dd/MM/yyyy')} - ${fin.format('dd/MM/yyyy')}) ${calendario.comentario?:'NOMINA'}"
    
	}
	
	String toString2(){
	return "$calendario.tipo $folio  $calendario.ejercicio $calendario.comentario?:'NOMINA'"
	}
	
}
