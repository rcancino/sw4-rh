package com.luxsoft.sw4.rh.sat

class SatIncapacidad implements Serializable{
    int clave
    String descripcion

    static constraints = {
        clave blank:false,unique:true,maxSize:3
        descripcion blank:false,maxSize:300
    }
	
	
	String toString() {
		return "($clave) $descripcion"
	}

}
