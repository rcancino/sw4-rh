package com.luxsoft.sw4.rh.procesadores



import org.apache.commons.logging.LogFactory

import com.luxsoft.sw4.*
import com.luxsoft.sw4.rh.*


class AcumuladorDePrimaVacacional {
	
	private static final log=LogFactory.getLog(this)
	
	def actualizar(Integer ejercicio){
		
		def acumulados=NominaPorEmpleadoDet.executeQuery(hql,[ejercicio])
		
	
	    def impuestoAcumulado=NominaPorEmpleadoDet
			.executeQuery("select sum(det.importeGravado+det.importeExcento) from NominaPorEmpleadoDet det "
					+" where det.parent.empleado=? and det.parent.nomina.calendarioDet.mes=? "
					+" and det.concepto.clave=?",[ne.empleado,mes,'D002'])[0]?:0.0
  
	    def subsidioAcumulado=NominaPorEmpleadoDet
			.executeQuery("select sum(det.importeGravado+det.importeExcento) from NominaPorEmpleadoDet det "
					+" where det.parent.empleado=? and det.parent.nomina.calendarioDet.mes=? "
					+" and det.concepto.clave=?",[ne.empleado,mes,'P021'])[0]?:0.0
  
	
  
	    
		 
	}

}
