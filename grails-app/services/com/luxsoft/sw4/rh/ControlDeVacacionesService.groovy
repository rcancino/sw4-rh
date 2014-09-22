package com.luxsoft.sw4.rh

import grails.transaction.Transactional

@Transactional
class ControlDeVacacionesService {

    def actualizarAcumulados(NominaPorEmpleado ne) {
		
    }
	
	def actualizarAcumulados(Empleado empleado,Integer ejercicio){
		def control=ControlDeVacaciones.findByEmpleadoAndEjercicio(empleado,ejercicio)
		if(control){
			def hql="from NominaPorEmpleadoDet n where n.parent.empleado=? "
				+" and n.concepto.clave=? and n.parent.nomina.ejercicio=?"
			def impores=NominaPorEmpleadoDet.executeQuery(hql,[empleado,'P024',ejercicio])
		}
	}
}
