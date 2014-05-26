package com.luxsoft.sw4.rh



class DiaFestivo {

	Integer ejercicio
	Date fecha
	String descripcion
	boolean parcial=false
	Date salida

	

	static constraints = {
		ejercicio size:2014..2020
		descripcion blank:false
		salida nullable:true
	}
	
	static mapping = {
		fecha type:'date'
		salida type:'time'
	}
	
}
