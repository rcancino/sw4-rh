package com.luxsoft.sw4.rh


import com.luxsoft.sw4.Periodo
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames=true,includePackage=false)
@EqualsAndHashCode(includes="tipo,periodicidad,activo")
class BrNomina {

	String tipo
	String periodicidad
	Periodo periodo
	String formula
	boolean activo=true


	static hasMany = [conceptos: ConceptoDeNomina]

	static embedded = ['periodo']

    static constraints = {

    	importFrom Nomina
    }
}
