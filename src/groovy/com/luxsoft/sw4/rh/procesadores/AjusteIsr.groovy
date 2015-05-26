package com.luxsoft.sw4.rh.procesadores


import java.math.RoundingMode
import org.apache.commons.logging.LogFactory

import com.luxsoft.sw4.rh.*
//import com.luxsoft.sw4.rh.imss.*
import com.luxsoft.sw4.rh.acu.*
import com.luxsoft.sw4.rh.tablas.*

class AjusteIsr {
	
	private static final log=LogFactory.getLog(this)
	
	def ajusteMensual(NominaPorEmpleado ne){
		
	
		
		def mes=ne.nomina.calendarioDet.mes
		def ejercicio=ne.nomina.calendarioDet.calendario.ejercicio
		
		
		
		
		def baseGravable=NominaPorEmpleadoDet
		.executeQuery("select sum(d.baseGravable) from NominaPorEmpleado d "
					+" where d.empleado=? and d.nomina.calendarioDet.mes=?",[ne.empleado,mes])[0]
		
					
		
		if(baseGravable<=0.0){
			//println "la base gravable es menor a cero"
			return
			
		}
		def baseGravableTotal=baseGravable
			
		def permisoRetardoAcu=NominaPorEmpleadoDet
			.executeQuery("select sum(det.importeGravado+det.importeExcento) from NominaPorEmpleadoDet det "
					+" where det.parent.empleado=? and det.parent.nomina.calendarioDet.mes=? "
					+" and det.concepto.clave=?  ",[ne.empleado,mes,'D012'])[0]?:0.0
		
		 baseGravable-=permisoRetardoAcu
		
		log.info "Base gravable $baseGravable"
		def tarifa =TarifaIsr.buscar(ne.nomina.ejercicio,'MENSUAL',baseGravable)
		log.info "Tariafa ISR localizada $tarifa"
	    def subsidio=SubsidioEmpleo.buscar(ne.nomina.ejercicio,baseGravable)
		log.info "Subsidio $subsidio "
  
	    def importeGravado=baseGravable-tarifa.limiteInferior
	    def impuestoMensual=(importeGravado*tarifa.porcentaje)/100
	
	    impuestoMensual+=tarifa.cuotaFija
	    impuestoMensual=impuestoMensual.setScale(2,RoundingMode.HALF_EVEN)
  
	    def subsidioMensual=subsidio.subsidio
	
	    def impuestoAcumulado=NominaPorEmpleadoDet
			.executeQuery("select sum(det.importeGravado+det.importeExcento) from NominaPorEmpleadoDet det "
					+" where det.parent.empleado=? and det.parent.nomina.calendarioDet.mes=? "
					+" and det.concepto.clave=? and det.parent.cfdi is not null ",[ne.empleado,mes,'D002'])[0]?:0.0
  
	    def subsidioAcumulado=NominaPorEmpleadoDet
			.executeQuery("select sum(det.importeGravado+det.importeExcento) from NominaPorEmpleadoDet det "
					+" where det.parent.empleado=? and det.parent.nomina.calendarioDet.mes=? "
					+" and det.concepto.clave=? and det.parent.cfdi is not null ",[ne.empleado,mes,'P021'])[0]?:0.0
  
	
  
	    def diferenciaMensual=impuestoMensual-subsidioMensual
		
		
  
	    def subsidioFinal=diferenciaMensual<=0?diferenciaMensual:0.0
  
	    def impuestoFinal=diferenciaMensual>0?diferenciaMensual:0.0
		
		
  
	    def diferenciaAcumulada=impuestoAcumulado-subsidioAcumulado
  
	    def subsidioAcumuladoFinal=diferenciaAcumulada<=0?diferenciaAcumulada:0.0
	    def impuestoAcumuladoFinal=diferenciaAcumulada>0?diferenciaAcumulada:0.0
  
		
  
	   
		def resultadoImpuesto=0.0
		def resultadoSubsidio=0.0
		def resultadoDevolucion=0.0
		
		def d1=(impuestoFinal+subsidioFinal)+subsidioAcumulado
		def d2=(impuestoFinal+subsidioFinal)+subsidioAcumulado-impuestoAcumulado 
		
		if(impuestoFinal>0.0){
			if(impuestoFinal>0.0){
				resultadoImpuesto=d2
			}else if(subsidioAcumulado==0.0){
				resultadoImpuesto=d2
			}
			
		}else{
			if(d2.abs()>impuestoAcumulado && impuestoAcumulado>0.0){
				resultadoImpuesto=-impuestoAcumulado
				resultadoSubsidio=d1
			}else if(d2<0.0 && impuestoAcumulado>0.0){
				resultadoImpuesto=d2
			}else if(d2<0.0 && impuestoAcumulado==0.0){
				resultadoSubsidio=d2
			}else if(d1>0.0 && impuestoAcumulado==0.0){
				resultadoSubsidio=subsidioFinal+subsidioAcumulado
				if(resultadoSubsidio>0.0){
					resultadoImpuesto=resultadoSubsidio.abs()
					resultadoSubsidio=0.0
				}
					
			}
		
		}
		
		/*if(subsidioFinal>0.0 && impuestoAcumulado>0.0){
			resultadoImpuesto=d1
		}else if(d1<0.0){
			resultadoSubsidio=d1
		}else{
			resultadoImpuesto=d1
		}
  */
        //println " Resultado impuesto final $resultadoImpuesto subsidio final: $resultadoSubsidio"
  
	     def istpMensual=IsptMensual
		 .find("from IsptMensual i where i.empleado=? and i.mes=? and i.ejercicio=?"
          ,[ne.empleado,mes,ejercicio])
		
		 //Calcular el subsidio aplicado
		 def subsidioAplicado=NominaPorEmpleado
				.executeQuery("select sum(ne.subsidioEmpleoAplicado) from NominaPorEmpleado ne "
					+" where ne.empleado=? and ne.nomina.calendarioDet.mes=? and ne.cfdi is not null"
					,[ne.empleado,mes])[0]?:0.0
			log.info "Aplicado"+subsidioAplicado +"Mensual"+subsidioMensual
				
		 def resultadoSubsidioAplicado=subsidioMensual-subsidioAplicado
		 
		 log.info "Base gravable: "+baseGravable
		 log.info "Impuesto Mensual :"+impuestoMensual
		 log.info "Subsidio mensual :"+subsidioMensual
		 log.info "ImpuestoFinal: " +impuestoFinal
		 log.info "Subsidio Final: "+subsidioFinal
		 log.info "Impuesto acumulado:"+impuestoAcumulado 
		 log.info "Subsidio acumulado:"+subsidioAcumulado 
		 log.info "Impuesto acumulado final: "+impuestoAcumuladoFinal
		 log.info "Subsidio acumulado final: "+subsidioAcumuladoFinal
		 log.info "Resultado impuesto: "+resultadoImpuesto  
		 log.info "Resultado subsidio: "+resultadoSubsidio
		 log.info "Resultado subsidio aplicado: "+resultadoSubsidioAplicado
		 
		 //Impuesto acu final: $impuestoAcumuladoFinal"
		 
		 if(istpMensual==null){
			istpMensual=new IsptMensual()
			istpMensual.empleado=ne.empleado
			istpMensual.ejercicio=ejercicio
			istpMensual.mes=mes
			istpMensual.nominaPorEmpleado=ne
			istpMensual.baseGravable=baseGravableTotal
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
			istpMensual.permisoRetardoAcu=permisoRetardoAcu
			istpMensual.save failOnError:true
		}		
		 
	}

}
