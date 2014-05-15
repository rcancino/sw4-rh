package com.luxsoft.sw4.rh

import org.grails.databinding.BindingFormat;

import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode

@ToString(includes='ejercicio,tipo,periodo',includeNames=true,includePackage=false)
@EqualsAndHashCode(includes='ejercicio,tipo')
class Calendario {

	Integer ejercicio
	
	
	String tipo
	
	@BindingFormat("dd/MM/yyyy")
	Date inicio
	
	@BindingFormat("dd/MM/yyyy")
	Date fin
	
	String comentario
	
	List partidas

	Date dateCreated
	Date lastUpdated

    static constraints = {
		ejercicio size:2014..2030
		tipo inList:['SEMANA','QUINCENA','MES','CATORCENA','BIMESTRE','ESPECIAL']
    	comentario nullable:true,maxSize:200
    }

    

    //static hasMany = [festivos: DiasFestivos]
	static hasMany=[partidas:CalendarioDet]
	
	static mapping = {
		partidas cascade: "all-delete-orphan"
		inicio type:'date'
		fin type:'date'
	}

}
