package com.luxsoft.sw4



import grails.validation.Validateable;
import java.text.DateFormat
import java.text.SimpleDateFormat

@Validateable
class Periodo {
	
	
	Date fechaInicial
	
	Date fechaFinal
	
	static constraints = {
		fechaInicial()
		fechaFinal()
	}
	
	static DateFormat dateFormat=new SimpleDateFormat('dd/MM/yyyy')
	
	String toString(){
		"$dateFormat.format($fechaInicial) - $dateFormat.format($fechaFinal)"
	}

}
