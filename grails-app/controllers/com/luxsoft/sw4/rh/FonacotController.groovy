package com.luxsoft.sw4.rh

class FonacotController {
    static scaffold = true

    def fonacotService

    def save(Fonacot fonacotInstance){
    	log.info 'Salvando  prestamo fonacot: '+fonacotInstance
    	fonacotInstance.retencionDiaria=0.0
    	fonacotInstance.validate()
    	if(fonacotInstance.hasErrors()){
    		render   view:'create', model:[fonacotInstance:fonacotInstance]
    		return 
    	}
    	fonacotInstance=fonacotService.salvar fonacotInstance
    	flash.message="Prestamo FONACOT actualizado ${fonacotInstance.id}"
    	redirect action:'show',params:[id:fonacotInstance.id]
    }
}
