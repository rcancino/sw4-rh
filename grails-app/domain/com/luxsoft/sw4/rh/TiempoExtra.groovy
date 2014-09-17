package com.luxsoft.sw4.rh



import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode


@ToString(excludes='asistencia,dateCreated,lastUpdated',includeNames=true,includePackage=false)
@EqualsAndHashCode(includes="empleado,ejercicio,id")
class TiempoExtra {
	
	Asistencia asistencia
	
	Empleado empleado
	
	Integer ejercicio
	
	String tipo
	
	Integer folio
	
	NominaPorEmpleado nominaPorEmpleado
	
	BigDecimal doblesExcentos
	
	BigDecimal doblesGravados

	BigDecimal triplesGravados
	
	Date dateCreated
	
	Date lastUpdated
	
	

	static constraints = {
		asistencia unique:true
		nominaPorEmpleado nullable:true
	}
	
	static transients =['doblesExcentos','doblesGravados','triplesGravados']
	
	static hasMany = [partidas:TiempoExtraDet]
	
	static mapping = {
		partidas cascade: "all-delete-orphan"
	}
	
	BigDecimal getDoblesExcentos(){
		return partidas.sum (0.0,{it.importeDoblesExcentos})
	}
	
	BigDecimal getDoblesGravados(){
		return partidas.sum (0.0,{it.importeDoblesGravados})
	}
    
	BigDecimal getTriplesGravados(){
		return partidas.sum (0.0,{it.importeTriplesGravados})
	}
}
