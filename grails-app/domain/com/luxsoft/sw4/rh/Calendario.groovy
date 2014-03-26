package com.luxsoft.sw4.rh

class Calendario {

	Integer year
	String comentario
	//Calendar calendario

	Date dateCreated
	Date lastUpdated

    static constraints = {
    	comentario nullable:true,maxSize:200
    }

    //static embedded = ['calendario']

    static hasMany = [festivos: DiasFestivos]
/*
    Calendario getCalendario(){
    	if(!calendario){
    		calendario=Calendar.instance
    	}
    }*/
}
