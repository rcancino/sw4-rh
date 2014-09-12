package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import grails.plugin.springsecurity.annotation.Secured


@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
//@Transactional(readOnly = true)
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
    	flash.message="Crédito FONACOT generado: ${fonacotInstance.id}"
    	redirect action:'show',params:[id:fonacotInstance.id]
    }

    def update(Fonacot fonacotInstance){
        fonacotInstance.validate()
        if(fonacotInstance.hasErrors()){
            render   view:'create', model:[fonacotInstance:fonacotInstance]
            return 
        }
        fonacotInstance=fonacotService.salvar fonacotInstance
        flash.message="Crédito FONACOT actualizado ${fonacotInstance.id}"
        redirect action:'show',params:[id:fonacotInstance.id]
    }

    def agregarAbono(Fonacot fonacotInstance){
        [fonacotInstance:fonacotInstance,fonacotAbonoInstance:new FonacotAbono(fecha:new Date())]
        
    }

    @Transactional
    def salvarAbono(FonacotAbono abono){
        log.info 'Salvando abono: '+abono
        def fonacot=Fonacot.get(params.fonacot.id)
        // abono.validate()
        // if(abono.hasErrors()){
        //     flash.message="Error de validacion en abono"
        //     println abono.errors
        //     render view:'agregarAbono',model:[fonacotInstance:fonacot,fonacotAbonoInstance:abono]
        // }

        fonacot.addToAbonos(abono)
        fonacot.save(flush:true)
        flash.message="Abono de $abono.importe agregado"
        redirect action:'edit',params:[id:fonacot.id]
    }

    @Transactional
    def eliminarPartida(Long id){
        def abono=FonacotAbono.get(id)
        def fonacot=abono.prestamo
        fonacot.removeFromAbonos(abono)
        fonacot.save failOnError:true
        flash.message="Abono eliminado $id"
        redirect action:'edit',params:[id:fonacot.id]
    }

    @Transactional
    def delete(Long id){
        def prestamo=Fonacot.get(id)
        prestamo.delete flush:true
        flash.message="Crédito FONACOT eliminado $id"
        redirect action:'index'
    }
}
