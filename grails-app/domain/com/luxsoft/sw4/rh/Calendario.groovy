package com.luxsoft.sw4.rh

import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode

@ToString(includes='ejercicio,tipo,periodo',includeNames=true,includePackage=false)
@EqualsAndHashCode(includes='ejercicio,tipo')
class Calendario {

	Integer ejercicio
	String tipo
	Date inicio
	Date fin
	
	String comentario
	List partidas

	Date dateCreated
	Date lastUpdated

    static constraints = {
		tipo inList:['SEMANA','QUINCENA','MES','CATORCENA','BIMESTRE','ESPECIAL']
    	comentario nullable:true,maxSize:200
    }

    

    //static hasMany = [festivos: DiasFestivos]
	static hasMany=[partidas:CalendarioDet]
	
	static mapping = {
		partidas cascade: "all-delete-orphan"
		ejercicio type:'calendar'
		inicio type:'date'
		fin type:'date'
	}

}
