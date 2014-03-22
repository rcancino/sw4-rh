package com.luxsoft.sw4.rh



class ConceptoDeNomina {
	
	String clave
	String descripcion
	String tipo
	Integer claveSat
	String formula
	
	Date dateCreated
	Date lastUpdated

    static constraints = {
		clave nullable:false,maxSize:20,unique:true
		descripcion size:1..250
		tipo inList:['PERCEPCION','DEDUCCION']
		formula nullable:true, maxSize:(1024 * 512)  // 50kb para almacenar el xml
    }
}
