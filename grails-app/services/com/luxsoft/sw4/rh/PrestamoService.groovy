package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import grails.events.Listener

@Transactional
class PrestamoService {
	
	
	
	 @Transactional(readOnly=true)
	 //@grails.events.Listener(namespace='gorm')
	 def onSaveOrUpdate(Prestamo prestamo) {
		 log.info 'Procesando reglas de negocios para nomina: '+prestamo
		 //Se aplican los procesadores para cada
		 println 'Message: sveOrUpdate prestamo: '+prestamo
	 }
	 
	 
	 @Listener(namespace='gorm')
	 def afterInsert(NominaPorEmpleado ne){
		 log.info 'Insertando nomina por empleado  procesando prestamos: '+ne
	 }
	 
	 @Listener(namespace='gorm')
	 def afterUpdate(NominaPorEmpleado ne){
		 log.info 'Actualizando nomina por empleado  procesando prestamos: '+ne
		 ne.conceptos.each{ it->
			 
			 if(it.clave=='D004'){
				 log.info 'Abono a prestamo localizado...'
				 def found=Prestamo.find{empleado==ne.empleado && saldo>0}
				 if(found){
					 log.info 'Prestamo localizado...'
					 try {
					 //Buscar si tiene prestamo
					  	found.addToAbonos(fecha:new Date(),importe:it.total)
					 } catch (Exception ex) {
					 
					 	log.error ex
					 }
				 }
			 }
			 
		 }
		 
	 }
	 
	 @Listener(namespace='gorm')
	 def afterDelete(NominaPorEmpleado ne){
		 log.info 'Eliminando nomina por empleado procesando prestamos'+ne
	 }
	 
	 
	

	
}
