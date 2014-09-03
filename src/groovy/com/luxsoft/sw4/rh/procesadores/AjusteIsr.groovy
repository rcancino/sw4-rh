package com.luxsoft.sw4.rh.procesadores


import java.math.RoundingMode
import org.apache.commons.logging.LogFactory

import com.luxsoft.sw4.rh.*
import com.luxsoft.sw4.rh.imss.*
import com.luxsoft.sw4.rh.acu.*

class AjusteIsr {
	
	private static final log=LogFactory.getLog(this)
	
	def ajusteMensual(NominaPorEmpleado ne){
		
		
		def mes=ne.nomina.calendarioDet.mes
		def ejercicio=ne.nomina.calendarioDet.calendario.ejercicio
		println 'Ajuste mensual de ISR para '+ne.id  +' Mes: '+mes
		println 'Ajuste mensual de ISR para '+ne.id  +' Mes: '+mes

		
		def baseGravable=NominaPorEmpleadoDet
		.executeQuery("select sum(d.baseGravable) from NominaPorEmpleado d "
					+" where d.empleado=? and d.nomina.calendarioDet.mes=?",[ne.empleado,mes])[0]
		
					
		log.debug  'Base gravable: ' +baseGravable
		if(baseGravable<=0.0)
			return
		def tarifa =TarifaIsr.buscar(baseGravable)
	    log.debug  'Tarifa: ' +tarifa
	    def subsidio=Subsidio.buscar(baseGravable)
	    log.debug 'Subsidio: '+subsidio
  
	    def importeGravado=baseGravable-tarifa.limiteInferior
	    def impuestoMensual=(importeGravado*tarifa.porcentaje)/100
	
	    impuestoMensual+=tarifa.cuotaFija
	    impuestoMensual=impuestoMensual.setScale(2,RoundingMode.HALF_EVEN)
	
	    log.debug 'Impuesto Mensual :'+impuestoMensual
  
	    def subsidioMensual=subsidio.subsidio
	
	    def impuestoAcumulado=NominaPorEmpleadoDet
			.executeQuery("select sum(det.importeGravado+det.importeExcento) from NominaPorEmpleadoDet det "
					+" where det.parent.empleado=? and det.parent.nomina.calendarioDet.mes=? "
					+" and det.concepto.clave=?",[ne.empleado,mes,'D002'])[0]?:0.0
  
	    def subsidioAcumulado=NominaPorEmpleadoDet
			.executeQuery("select sum(det.importeGravado+det.importeExcento) from NominaPorEmpleadoDet det "
					+" where det.parent.empleado=? and det.parent.nomina.calendarioDet.mes=? "
					+" and det.concepto.clave=?",[ne.empleado,mes,'P021'])[0]?:0.0
  
	
  
	    def diferenciaMensual=impuestoMensual-subsidioMensual
	    log.debug "Impuesto acumulado: $impuestoAcumulado  Subsidio acumulado: $subsidioAcumulado Impuesto mensual: $impuestoMensual Subsidio Mensual: $subsidioMensual"
  
	    def subsidioFinal=diferenciaMensual<=0?diferenciaMensual:0.0
  
	    def impuestoFinal=diferenciaMensual>0?diferenciaMensual:0.0
  
	    log.debug "Subsidio final: $subsidioFinal  Impuesto final: $impuestoFinal"
  
	    def diferenciaAcumulada=impuestoAcumulado-subsidioAcumulado
  
	    def subsidioAcumuladoFinal=diferenciaAcumulada<=0?diferenciaAcumulada:0.0
	    def impuestoAcumuladoFinal=diferenciaAcumulada>0?diferenciaAcumulada:0.0
  
	    log.debug "Subsidio acu final: $subsidioAcumuladoFinal  Impuesto acu final: $impuestoAcumuladoFinal"
  
	    def resultadoImpuesto=impuestoFinal-impuestoAcumuladoFinal
	    def resultadoSubsidio=subsidioFinal-subsidioAcumuladoFinal
  
        println " Resultado impuesto final $resultadoImpuesto subsidio final: $resultadoSubsidio"
  
	     def istpMensual=IsptMensual
		 .find("from IsptMensual i where i.empleado=? and i.mes=? and i.ejercicio=?"
          ,[ne.empleado,mes,ejercicio])
		
		 //Calcular el subsidio aplicado
		 def subsidioAplicado=NominaPorEmpleado
				.executeQuery("select sum(ne.subsidioEmpleoAplicado) from NominaPorEmpleado ne "
					+" where ne.empleado=? and ne.nomina.calendarioDet.mes=? "
					,[ne.empleado,mes])[0]?:0.0
				
		 def resultadoSubsidioAplicado=subsidioMensual-subsidioAplicado
		 
		 if(istpMensual==null){
			istpMensual=new IsptMensual()
			istpMensual.empleado=ne.empleado
			istpMensual.ejercicio=ejercicio
			istpMensual.mes=mes
			istpMensual.nominaPorEmpleado=ne
			istpMensual.baseGravable=baseGravable
			istpMensual.limiteInferior=tarifa.limiteInferior
			istpMensual.limiteSuperior=tarifa.limiteSuperior
			istpMensual.cuotaFija=tarifa.cuotaFija
			istpMensual.tarifa=tarifa.porcentaje
			istpMensual.impuestoMensual=impuestoMensual
			istpMensual.subsidioMensual=subsidioMensual
			istpMensual.impuestoFinal=impuestoFinal
			istpMensual.subsidioFinal=subsidioFinal
			istpMensual.impuestoAcumulado=impuestoAcumulado
			istpMensual.subsidioAcumulado=subsidioAcumulado
			istpMensual.impuestoAcumuladoFinal=impuestoAcumuladoFinal
			istpMensual.subsidioAcumuladoFinal=subsidioAcumuladoFinal
			istpMensual.resultadoImpuesto=resultadoImpuesto
			istpMensual.resultadoSubsidio=resultadoSubsidio
			istpMensual.subsidioAplicado=subsidioAplicado
			istpMensual.resultadoSubsidioAplicado=resultadoSubsidioAplicado
			istpMensual.save failOnError:true
		}		
		 
	}

}
