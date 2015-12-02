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

	Boolean excluir = false

	Date dateCreated

	Date lastUpdated

    static constraints = {
    	tipo inList:['INCAPACIDAD','FALTA']
    	cambio nullable: true
    	subTipo nullable:true,maxSize:200
    }

    static mapping = {
		fecha type:'date'
		cambio type:'date'
	}

	static belongsTo = [asistenciaImss:AsistenciaImss]
}
