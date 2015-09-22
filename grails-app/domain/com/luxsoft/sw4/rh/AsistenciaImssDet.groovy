package com.luxsoft.sw4.rh

import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode
import org.grails.databinding.BindingFormat

@ToString(includeNames=true,includePackage=false)
@EqualsAndHashCode(includes='fecha')
class AsistenciaImssDet {

	@BindingFormat('dd/MM/yyyy')
	Date fecha

	String tipo

	String subTipo

	@BindingFormat('dd/MM/yyyy')
	Date cambio

    static constraints = {
    	tipo inList:['INCAPACIDAD','FALTA']
    	subTipo nullable:true,maxSize:20
    }

    static mapping = {
		fecha type:'date'
	}

	static belongsTo = [asistenciaImss:AsistenciaImss]
}
