package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import net.sf.jasperreports.engine.type.CalculationEnum;

import org.apache.commons.lang.time.DateUtils

import grails.transaction.Transactional
import grails.transaction.NotTransactional

import com.luxsoft.sw4.rh.tablas.ZonaEconomica
import com.luxsoft.sw4.Periodo
import com.luxsoft.sw4.rh.imss.*
import com.luxsoft.sw4.rh.acu.*

import java.math.BigDecimal;
import java.math.RoundingMode

@Transactional
class CalculoAnualService {

	
	@NotTransactional
	def calcular(Integer ejercicio){
		//CalculoAnual.executeUpdate("delete from CalculoAnual c where c.ejercicio=?",[ejercicio])
		def ids=NominaPorEmpleado.executeQuery(
			"select distinct(n.empleado.id) from NominaPorEmpleado n where n.nomina.ejercicio=? and n.empleado.status!='BAJA'",[ejercicio])
		log.info "Generando calculando anual para $ids.size empleados del ejercicio $ejercicio"
		ids.each{ 
			def empleado=Empleado.get(it)
			try {
				calcular(empleado,ejercicio)
			}
			catch(Exception e) {
				log.error "Error calculando calculo de $empleado ($ejercicio)",e
	
			}
			
		}
	}
	
	def calcular(Empleado e,Integer ejercicio){
		CalculoAnual.executeUpdate("delete from CalculoAnual c where c.empleado=? and c.ejercicio=?",[e,ejercicio])
		def calculo=CalculoAnual.findOrCreateWhere(ejercicio: ejercicio,empleado:e)
		def periodo=Periodo.getPeriodoAnual(calculo.ejercicio)
		calculo.fechaInicial=DateUtils.addMonths(periodo.fechaInicial,-1)
		calculo.fechaFinal=DateUtils.addMonths(periodo.fechaFinal,-1)
		calculo.empleado=e
		calcular(calculo)
	}

	def calcular(CalculoAnual calculo) {
		log.info 'Actualizando :'+calculo+ 'Empleado.id:'+calculo.empleado.id
		def periodo=Periodo.getPeriodoAnual(calculo.ejercicio)
		def diasDelEjercicioReales=periodo.fechaFinal-periodo.fechaInicial+1
		def empleado =calculo.empleado
		calculo.fechaInicial=DateUtils.addMonths(periodo.fechaInicial,-1)
		calculo.fechaFinal=DateUtils.addMonths(periodo.fechaFinal,-1)
		if(empleado.alta>calculo.fechaInicial){
			
			calculo.fechaInicial=empleado.alta
			calculo.fechaFinal=periodo.fechaFinal
			
		}
		calculo.salario=calculo.empleado.salario.salarioDiario
		calculo.resultado=0.0
		actualizarConceptos(calculo)
		validarAplicacion(calculo)
		calculo.save failOnError:true
	}
	
	
	private actualizarConceptos(CalculoAnual anual){
		
		def partidas=NominaPorEmpleadoDet
		.findAll("from NominaPorEmpleadoDet d where d.parent.empleado=?"+
			" and d.concepto.id in(13,41,37,44,36,22,14,40,38,19,15,23,24,31,42,20,45,34,35,33,2,12,43) "+
			" and d.parent.nomina.ejercicio=? "
				,[anual.empleado,anual.ejercicio])
		
		
		
		partidas.each{it->
			
			switch(it.concepto.id){
				case 13:
					anual.sueldo+=it.importeGravado
					break
				case 41:
					anual.comisiones+=it.importeGravado
					break
				case 37:
					anual.vacaciones+=it.importeGravado
					break
				case 44:
					anual.vacacionesPagadas+=it.importeGravado
					break
				case 36:
					anual.primaVacacionalExenta+=it.importeExcento
					anual.primaVacacionalGravada+=it.importeGravado
					break
				case 22:
					anual.incentivo+=it.importeGravado
					break
				case 14:
					anual.aguinaldoExento+=it.importeExcento
					anual.aguinaldoGravable+=it.importeGravado
					break
				case 40:
					anual.indemnizacionExenta+=it.importeExcento
					anual.indemnizacionGravada+=it.importeGravado
					break
				case 38:
					anual.primaDeAntiguedadExenta+=it.importeExcento
					anual.primaDeAntiguedadGravada+=it.importeGravado
					break
				case 19:
					anual.compensacion+=it.importeGravado
					break
				case 15:
					anual.ptuExenta+=it.importeExcento
					anual.ptuGravada+=it.importeGravado
					break
				case 23:
					anual.bonoDeProductividad+=it.importeGravado
					break
				case 24:
					anual.bonoPorDesempeno+=it.importeGravado
					break
				case 31:
					anual.devISPT+=it.importeExcento					
					break
				case 42:
					anual.primaDominicalExenta+=it.importeExcento
					anual.primaDominicalGravada+=it.importeGravado
					break
				case 20:
					anual.gratificacion+=it.importeGravado
					break
				case 45:
				anual.permisoPorPaternidad+=it.importeGravado
					break
				case 34:
					anual.tiempoExtraDobleExento+=it.importeExcento
					anual.tiempoExtraDobleGravado+=it.importeGravado
					break
				case 35:
				anual.tiempoExtraTripleGravado+=it.importeGravado
					break
				case 33:
				anual.SubsEmpPagado+=it.importeExcento
					break
				case  2:
				anual.ISR+=it.importeExcento
					break
				case 12:
				anual.retardos+=it.importeExcento
					break

			}
			
		}
		def subEmpAplic=0.0
		
		def nominasEmpleado=NominaPorEmpleado.findAll("from NominaPorEmpleado n where empleado=? and n.nomina.ejercicio=? "+
			"  ",[anual.empleado,anual.ejercicio]).each {nom->
			subEmpAplic+=nom.subsidioEmpleoAplicado
		}
				
		anual.SubsEmpAplicado=subEmpAplic
		anual.totalGravado=0.0
		def netoGravado=0.0
		anual.with{
			totalGravado=sueldo+comisiones+vacaciones+vacacionesPagadas+primaVacacionalGravada+incentivo+aguinaldoGravable+indemnizacionGravada+primaDeAntiguedadGravada+compensacion+ptuGravada+bonoDeProductividad+bonoPorDesempeno+primaDominicalGravada+gratificacion+permisoPorPaternidad+tiempoExtraDobleGravado+tiempoExtraTripleGravado
			totalExento=primaVacacionalExenta+aguinaldoExento+indemnizacionExenta+primaDeAntiguedadExenta+ptuExenta+primaDominicalExenta+tiempoExtraDobleExento  
			total=totalGravado+totalExento
			netoGravado=totalGravado-retardos
			ingresoTotal=total-devISPTAnt-retardos

		}
		def diasProyectados=calcularDiasProyectados(anual)
		//def dias=anual.empleado.salario.periodicidad=='QUINCENAL'?16:0
		anual.proyectado=anual.salario*diasProyectados

		anual.impuestoDelEjercicio=calcularImpuesto(netoGravado+anual.proyectado)
		
		def ResISPT177=anual.impuestoDelEjercicio-anual.SubsEmpAplicado
		if(anual.impuestoDelEjercicio<anual.subsEmpAplicado){
			ResISPT177=0.0
		}		
		
		anual.resultado=anual.ISR-anual.devISPT-ResISPT177
	}
	
	/**
	 * @Todo Falta implementar la logica para determinar los dias de proyectado
	 * @param anual
	 * @return
	 */
	private calcularDiasProyectados(CalculoAnual anual){
		return anual.empleado.salario.periodicidad=='QUINCENAL'?0:0
	}
	
	public void validarAplicacion(CalculoAnual anual){
		if(anual.ingresoTotal>=400000.00){
			anual.calculoAnual=false
			return
		}
		if(anual.empleado.perfil.declaracionAnual==true){
			anual.calculoAnual=false
			return
		}
		def periodo=Periodo.getPeriodoAnual(anual.ejercicio)
		def diciembre=Periodo.getPeriodoEnUnMes(11,anual.ejercicio)
		if(anual.empleado.alta>periodo.fechaInicial ){
			anual.calculoAnual=false
			return
		
		}
		if(anual.empleado.baja && anual.empleado.baja<diciembre.fechaInicial){
			anual.calculoAnual=false
			return
		}
	}
	
	private BigDecimal calcularImpuesto(BigDecimal percepciones){
		def tarifa =TarifaAnualIsr.buscar(percepciones)
		println 'Tarifa seleccionada: '+tarifa+ ' Para un ingreso de: '+percepciones
		def importeGravado=percepciones-(tarifa.limiteInferior)
		importeGravado*=tarifa.porcentaje
		importeGravado/=100
		importeGravado+=(tarifa.cuotaFija)
		importeGravado=importeGravado.setScale(2,RoundingMode.HALF_EVEN)
		return importeGravado
	}


	public aplicar(NominaPorEmpleado ne) {
		
		def ajuste=IsptMensual.findByNominaPorEmpleado(ne)
		if(!ajuste) 
			return

		def calculo=CalculoAnual.findByEmpleadoAndEjercicio(ne.empleado,ne.nomina.ejercicio)
		if(calculo && calculo.calculoAnual){
			log.info 'Aplicando calculo anual: '+calculo+ " Resultado: "+calculo.resultado
			def resultado=calculo.resultado
			def impuestoDet=ne.conceptos.find(){ 
				it.concepto.clave=='D002'
			}
			if(impuestoDet){
				def importeExcento=0.0
				def concepto=ConceptoDeNomina.findByClave('P033')
				if(resultado>impuestoDet.importeExcento){
					importeExcento=impuestoDet.importeGravado
				}else{
					importeExcento=resultado
				}
				def ca=new NominaPorEmpleadoDet(
					concepto:concepto
					,importeGravado:0.0
					,importeExcento:importeExcento
					,comentario:'CALCULO ANUAL')
				ne.addToConceptos(ca)
			}
		}
		ne.actualizar()
	}
	
	
    
}
