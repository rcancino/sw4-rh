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
		//def factor=(maximo<=te.empleado.salario.salarioDiario)?1.0:0.5
		def salarioDiario=te.empleado.salario.salarioDiario
		def factor=(salarioDiario<=smg)?1.0:0.5
		
		
		def gravarUltimaSemana=true
		
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
			if(det.semana!=3 && det.total<=0.0){
				gravarUltimaSemana=false
			}
			if(det.semana==3 && gravarUltimaSemana){
				det.importeDoblesExcentos=0.0
				det.importeDoblesGravados=importeTotal
			}
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
		
		
		
		te.partidas.each{det->
			def found=det.tiempoExtraImss
			if(found==null){
				found=new TiempoExtraImss(tiempoExtraDet:det,semana:det.semana)
			}
			def minutosMax=540
			def minutosAcumulados=0
			def diasTrabajados=0
			def importeIntegra=0.0
			def minutosIntegra=0.0
			def minutosIntegraTripes=0.0
			
			Dias.NOMBRES.each{dd->
				
				def dia=dd.toLowerCase()
				def minutos=det[dia]
				
				if(minutos>0){
					diasTrabajados++
					if(diasTrabajados>3){
						
						if( (minutosAcumulados+minutos)>540){
							def dobles=540-minutosAcumulados
							def triples=(minutosAcumulados+minutos)-540
							minutosIntegra+=dobles
							minutosIntegraTripes+=triples
							//println dia+' Dobles: '+dobles
							//println dia+' Triples: '+triples
						}else{
							minutosIntegra+=minutos
						}
					}else{
						if(minutos>180){
							if( (minutosAcumulados+minutos)>540){
								//def dobles=540-minutosAcumulados
								//def triples=(minutosAcumulados+minutos)-540
								
								//minutosIntegra+=dobles
								minutosIntegraTripes+=(minutos-180)                        
								
							}else{
								minutosIntegra+=(minutos-180)
							}
						}
					}
					
					minutosAcumulados+=minutos
					found[dia]=minutos
				}
				
			}
			//println "Semana: ${det.semana} Dias:${diasTrabajados}  MinutosIntegra: ${minutosIntegra} MinIntegra Tribles:${ minutosIntegraTripes}"
			found.integra=minutosIntegra*det.getSalarioPorMinuto()*2
			found.integraTriples= minutosIntegraTripes*det.getSalarioPorMinuto()*3
			det.tiempoExtraImss=found
		}
		
		
		te.partidas.each{det->
			def found=det.tiempoExtraImss
			def minutosAcumulados=0
			
			
			Dias.NOMBRES.each{dd->
				
				def minutosDobles=0.0
				def minutosTriples=0.0
				
				def dia=dd.toLowerCase()
				def minutos=det[dia]
				
				if(minutos>0){
					if( (minutosAcumulados+minutos)>540){
							def dobles=540-minutosAcumulados
							def triples=(minutosAcumulados+minutos)-540
							minutosDobles+=dobles
							minutosTriples+=triples
							
					}else{
						minutosDobles+=minutos
					}
					minutosAcumulados+=minutos
					//found[dia]=minutos
					println "Semana: ${det.semana} ${dia} MinutosDobles: ${minutosDobles} MinTribles:${ minutosTriples}"
					found[dia]=(minutosDobles*det.getSalarioPorMinuto()*2)+(minutosTriples*det.getSalarioPorMinuto()*3)
					
				}
				
			}
			
			
		}
		
	}
	
	
}
