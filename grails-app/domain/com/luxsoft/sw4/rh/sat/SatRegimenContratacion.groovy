package com.luxsoft.sw4.rh.sat

class SatRegimenContratacion {
    int clave
    String descripcion

    static constraints = {
        clave blank:false,unique:true,maxSize:3
        descripcion blank:false,maxSize:300
    }
}
