package com.luxsoft.sw4.rh

import grails.transaction.Transactional

@Transactional
class PrestamoService {
	
	
	
	 @Transactional(readOnly=true)
	 //@grails.events.Listener(namespace='gorm')
	 def onSaveOrUpdate(Prestamo prestamo) {
		 log.info 'Procesando reglas de negocios para nomina: '+prestamo
		 //Se aplican los procesadores para cada
		 println 'Message: sveOrUpdate prestamo: '+prestamo
	 }
	

	
}
