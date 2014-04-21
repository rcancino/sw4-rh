package com.luxsoft.sw4.rh.sat

class SatBanco implements Serializable{
    
    int clave
    String nombre
    String nombreCorto

    static constraints = {
        clave blank:false,unique:true,maxSize:3
        nombre blank:false,maxSize:300
        nombreCorto blank:false,maxSize:40
    }

    String toString(){
    	return "${nombre}"
    }
}
