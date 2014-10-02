package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import grails.events.Listener

@Transactional
class PrestamoService {
	
	
	static String CONCEPTO='D004'
	
	def beforeDelete(NominaPorEmpleado ne){
		log.info 'Evaluando la eliminacion de nomina por empleado: '+ne
		ne.conceptos.each{
			beforeDelete(it)
		}
	}
	
	//@Listener(namespace='gorm')
	def beforeDelete(NominaPorEmpleadoDet neDet){
		log.debug 'Evaluando la eliminacion de CONCEPTO DE nomina por empleado: '+neDet
		if(neDet.concepto.clave==CONCEPTO){
			log.debug 'Cancelando abono en prestamo'
			def abono=PrestamoAbono.findByNominaPorEmpleadoDet(neDet)
			if(abono){
				
				def prestamo=abono.prestamo
				log.debug 'Eliminando abono de prestamo existente '+prestamo
				prestamo.removeFromAbonos(abono)
				
				
			}
		}
	}
	
	/*
	
	 @Listener(namespace='gorm')
	 def afterUpdate(NominaPorEmpleado ne){
		 log.debug 'UPDATE Evaluando nomina por empleado: '+ne
	 }
	 
	 @Listener(namespace='gorm')
	 def afterInsert(NominaPorEmpleado ne){
		 log.debug 'INSERT Evaluando nomina por empleado: '+ne
	 }
	 
	
	 
	 @Listener(namespace='gorm')
	 def afterUpdate(NominaPorEmpleadoDet neDet){
		 log.info 'UPDATE Evaluando nomina por empleado: '+neDet
		 
	 }
	 
	 */
	 
	  /*
	 @Listener(namespace='gorm')
	 def afterInsert(NominaPorEmpleado ne){
		 log.info 'Insertando nomina por empleado  procesando prestamos: '+ne
	 }
	 
	
	 @Listener(namespace='gorm')
	 def afterUpdate(NominaPorEmpleado ne){
		 log.info 'Actualizando nomina por empleado  procesando prestamos: '+ne
		 /*
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
	 
	 
	*/

	
}
