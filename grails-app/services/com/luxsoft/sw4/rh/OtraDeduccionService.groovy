package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import grails.events.Listener

@Transactional
class OtraDeduccionService {
	
	static String CONCEPTO='D005'
	
	@Listener(namespace='gorm')
	def beforeDelete(NominaPorEmpleado ne){
		ne.partidas.each{
			beforeDelete(it)
		}
	}

    @Listener(namespace='gorm')
	def beforeDelete(NominaPorEmpleadoDet neDet){
		if(neDet.concepto.clave==CONCEPTO){
			OtraDeduccionAbono.withNewSession{
				def abono=OtraDeduccionAbono.findByNominaPorEmpleadoDet(neDet)
				def od=abono.otraDeduccion
				od.removeFromAbonos(abono)
				od.actualizarSaldo()
				od.save flush:true
				log.info "Abono de OtrasDeducciones ${abono.id} eliminado al eliminar detalle de nomina por empleado ${neDet.id}"
			}
		}
		
	}
}
