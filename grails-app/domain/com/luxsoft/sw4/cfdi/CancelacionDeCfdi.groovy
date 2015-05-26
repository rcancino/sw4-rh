package com.luxsoft.sw4.cfdi

class CancelacionDeCfdi {

    String comentario
	//Cfdi cfdi
	byte[] aka
	//byte[] message

	Date dateCreated
	Date lastUpdated

	static belongsTo = [cfdi: Cfdi]

    static constraints = {
    	comentario nullable:true
    	aka maxSize:(1024 * 512)  // 50kb para almacenar el xml
    	//message maxSize:(1024 * 512)  // 50kb para almacenar el xml
    }
}
