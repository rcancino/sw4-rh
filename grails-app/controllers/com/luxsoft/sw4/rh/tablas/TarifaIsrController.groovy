package com.luxsoft.sw4.rh.tablas

class TarifaIsrController {
    static scaffold = true


    def index(){
    	params.max = 20
		def list=TarifaIsr.findAll("from TarifaIsr t order by t.ejercicio desc,t.limiteInferior asc",[],params)
    	[tarifaIsrInstanceList:list,tarifaIsrInstanceCount:TarifaIsr.count()]
    }

    def delete(TarifaIsr tarifa){
    	println 'Eliminando tarifa: '+tarifa
    	tarifa.delete flush:true
    	flash.message="Tarifa eliminada "+tarifa.id
    	redirect action:'index'
    }
}
