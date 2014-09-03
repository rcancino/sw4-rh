package com.luxsoft.sw4.rh


import groovy.transform.EqualsAndHashCode
import org.grails.databinding.BindingFormat

@EqualsAndHashCode(includes="empleado")
class TiempoExtraDet {
	
	@BindingFormat("dd/MM/yyyy")
	Date fecha

	static belongsTo = [tiempoExtra:TiempoExtra]
	
    static constraints = {
    }
}
