package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Dias;

import grails.transaction.Transactional
import org.joda.time.LocalTime

@Transactional
class TiempoExtraService {
	
	static int MAXIMO_MINUTOS_DIARIO_DOBLES=540
	
	static int MINUTOS_EXTRAS_POR_DIA=180
	
	
	
	def actualizar(Integer ejercicio,String tipo,Integer folio){
		
		
		String hql=" select distinct(a.id) from AsistenciaDet det join det.asistencia a  " +
		  "where det.pagarTiempoExtra=true and a.calendarioDet.calendario.ejercicio=? "+
		  "  and a.calendarioDet.calendario.tipo=? "+
		  "  and a.calendarioDet.folio=?"
		def asistencias=AsistenciaDet.executeQuery(hql,[ejercicio,tipo,folio])
		
		log.debug "Actualizando tiempo extra $ejercicio $tipo $folio para ${asistencias.size()} registros de asistencia"
		
		asistencias.each{
			def a=Asistencia.get(it)
			actualizarTiempoExtra(a,tipo)
		}
		
	}

    def actualizarTiempoExtra(Asistencia asistencia,String tipo) {
		
		def te=TiempoExtra.findOrSaveByAsistencia(asistencia)
		te.tipo=tipo
		te.empleado=asistencia.empleado
		te.ejercicio=asistencia.calendarioDet.calendario.ejercicio
		te.folio=asistencia.calendarioDet.folio
		if( te.partidas) 
			te.partidas.clear()
		
		def partidas=asistencia.partidas.sort{it.fecha}
		int semana=1
		
		def det=new TiempoExtraDet()
		det.semana=semana
		det.salarioDiario=te.empleado.salario.salarioDiario
		te.addToPartidas(det)
		
		partidas.each{ row->
			
			def dia=row.fecha[Calendar.DAY_OF_WEEK]
			def minutos=getMinutosExtras(row)
			
			log.debug "Procesando tiempo extra $asistencia.empleado semana:$semana dia:$dia  minutos:$minutos"
			switch (dia){
				case Calendar.MONDAY:
					det.lunes=minutos
					break
				case Calendar.TUESDAY:
					det.martes=minutos
					break
				case Calendar.WEDNESDAY:
					det.miercoles=minutos
					break
				case Calendar.THURSDAY:
					det.jueves=minutos
					break
				case Calendar.FRIDAY:
					det.viernes=minutos
					break
				case Calendar.SATURDAY:
					det.sabado=minutos
					break
				case Calendar.SUNDAY:
					det.domingo=minutos
					//preparamos cambio de semana
					semana++
					det=new TiempoExtraDet(semana:semana)
					det.salarioDiario=te.empleado.salario.salarioDiario
					te.addToPartidas(det)
					break
				default:
					break
			}
		}
		calcularMinutosDoblesTriples(te)
		calcularImportesMinutosDobles(te)
		calcularImportesMinutosTriples(te)
		calcularImportesImss(te)
		te.save failOnError:true
		
		
    }
	
	
	def getMinutosExtras(AsistenciaDet det){
		
		if(det.pagarTiempoExtra){
			
			LocalTime salidaOficial
			LocalTime salidaReal
			if(det.turnoDet.salida2){
				salidaOficial=det.turnoDet.salida2
				salidaReal=LocalTime.fromDateFields(det.salida2)
			}else if(det.turnoDet.salida1){
				salidaOficial=det.turnoDet.salida1
				salidaReal=LocalTime.fromDateFields(det.salida1)
			}
			assert salidaOficial, 'Debe haber salida oficial declarada en el turno del empleado para  '+det
			assert salidaReal,'Debe haber salida registrada  '+det
			
			def tiempoExtra=( ((salidaReal.getHourOfDay()*60)+salidaReal.getMinuteOfHour()) -
				((salidaOficial.getHourOfDay()*60)+salidaOficial.getMinuteOfHour())
			   )
			log.debug "Minutos extras ${det.fecha[Calendar.DAY_OF_WEEK]} De: $salidaReal a $salidaOficial res:$tiempoExtra"
			return tiempoExtra
		}
		else
			return 0
	}
	
	def calcularMinutosDoblesTriples(TiempoExtra te){
		def dias=['lunes','martes','miercoles','jueves','viernes','sabado','domingo']
		te.partidas.each{ det->
			def totalMinutos=0
			dias.each{
				totalMinutos+=det[it]
			}
			det.totalMinutos=totalMinutos	
			//Minutos dobles
			if(det.totalMinutos>MAXIMO_MINUTOS_DIARIO_DOBLES){
				det.minutosDobles=MAXIMO_MINUTOS_DIARIO_DOBLES
				det.minutosTriples=det.totalMinutos-MAXIMO_MINUTOS_DIARIO_DOBLES
			}else{
				det.minutosDobles=det.totalMinutos
				det.minutosTriples=0
			}
		}
		
		return te
	}
	
	def calcularImportesMinutosDobles(TiempoExtra te){
		def smg=67.29
		def maximo=smg*5
		def factor=(maximo<=te.empleado.salario.salarioDiario)?1.0:0.5
		
		te.partidas.each{det->
			def importeTotal=det.minutosDobles*det.getSalarioPorMinuto()*2
			//Calculando el importe excento
			def importeExcentoCalculado=(importeTotal*factor)
			if(importeExcentoCalculado>=maximo){
				det.importeDoblesExcentos=maximo
			}else{
				det.importeDoblesExcentos=importeExcentoCalculado
			}
			
			det.importeDoblesGravados=importeTotal-det.importeDoblesExcentos
		}	
	}
	
	def calcularImportesMinutosTriples(TiempoExtra te){
		te.partidas.each{det->
			def importeTotal=det.minutosTriples*det.getSalarioPorMinuto()*3
			det.importeTriplesGravados=importeTotal
		}
	}
	
	def calcularImportesImss(TiempoExtra te){
		
		def horaPorDia=9
		def diasPorSemana=3
		
		te.partidas.each{det->
			def found=det.tiempoExtraImss
			if(found==null){
				found=new TiempoExtraImss(tiempoExtraDet:det,semana:det.semana)
			}
			Dias.NOMBRES.each{dd->
				def dia=dd.toLowerCase()
				found[dia]=det[dia]*det.getSalarioPorMinuto()
			}
			det.tiempoExtraImss=found
		}
		
		//Calculo del integrado por criterio de 3 o mas dias
		te.partidas.each{det->
			def integraPorDias=0.0
			def dias=0
			def total=0.0
			Dias.NOMBRES.each{dd->
				
				def found=det.tiempoExtraImss
				def dia=dd.toLowerCase()
				if(found[dia])
					dias++
				if(dias>3){
					integraPorDias+=found[dia]
				}
				total+=found[dia]
				found.integra=integraPorDias
				log.info "Integra para la semana $found.semana: "+integraPorDias
			}
			det.tiempoExtraImss.total=total
		}
		
		//Calculo de integrado por criterio de 9 horas o mas por dia
		te.partidas.each{ det->
			def integraPorHoras=0.0
			def dias=0
			def total=det.tiempoExtraImss.total
			
			Dias.NOMBRES.each{dd->
				def minutos=det[dd.toLowerCase()]
				if(minutos>MINUTOS_EXTRAS_POR_DIA){
					log.info "Integra por horas excedidas: "+minutos+"  para:"+dd
					def ex=det.getSalarioPorMinuto()*(minutos-MINUTOS_EXTRAS_POR_DIA)
					integraPorHoras+=ex
				}
			}
			det.tiempoExtraImss.integra+=integraPorHoras
			
		}
	}
	
	
}
