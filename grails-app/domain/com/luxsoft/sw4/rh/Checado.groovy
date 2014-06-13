package com.luxsoft.sw4.rh

class Checado {
	
	Integer lector
	Date fecha
	Date hora
	String numeroDeEmpleado

	Date dateCreated
	
	Date lastUpdated
	

    static constraints = {
    }

    static mapping = {
    	sort fecha:"desc"
    	lector index:"CHECADO_IDX"
		fecha type:'date',index:'CHECADO_IDX'
		hora type:'time',index:'CHECADO_HORA_IDX'
	}
}
