package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import grails.transaction.NotTransactional
import com.luxsoft.sw4.Mes
import com.luxsoft.sw4.Periodo

@Transactional
class IncentivoService {

	@NotTransactional
    def generarIncentivosSemanales(CalendarioDet calendarioDet) {
    	
    	//def asistencias=Asistencia.findAll{calendarioDet==calendarioDet && empleado.perfil.tipoDeIncentivo=='SEMANAL'} 
    	def asistencias=Asistencia.executeQuery("from Asistencia a where a.calendarioDet=? and a.empleado.perfil.tipoDeIncentivo=?"
    		,[calendarioDet,'SEMANAL'])
    	if(!asistencias){
    		throw new RuntimeException("No se hay asistencias y/o empleados  con bono en la semanal $calendarioDet.folio")
    	}
		
		asistencias=asistencias.sort{a,b ->
			a.empleado.perfil.ubicacion.clave<=>b.empleado.perfil.ubicacion.clave?:a.empleado.nombre<=>b.empleado.nombre
		}

    	asistencias.each{ asistencia->
    		log.debug 'Generando incentivo semanal para: '+asistencia.empleado
    		
    		def empleado=asistencia.empleado
    		Incentivo inc=Incentivo.find{asistencia==asistencia}
    		if(inc==null){
				inc=new Incentivo(
					tipo:'SEMANAL',
					asistencia:asistencia,
					empleado:empleado,
					ubicacion:empleado.perfil.ubicacion,
					ejercicio:calendarioDet.calendario.ejercicio
				)
			}
			calcularIncentivoSemanal(inc)

			inc.save failOnError:true
			log.info 'Incentivo generado/actualizado : '+inc
			
    	}
    }

	@NotTransactional
    def generarIncentivosQuincenales(CalendarioDet calendarioDet) {

    	//def asistencias=Asistencia.findAll{calendarioDet==calendarioDet} 
    	def asistencias=Asistencia.executeQuery("from Asistencia a where a.calendarioDet=? and a.empleado.perfil.tipoDeIncentivo=?"
    		,[calendarioDet,'QUINCENAL'])
    	if(!asistencias){
    		throw new RuntimeException("No hay asistencias y/o empleados para bono en la quincena $calendarioDet.folio")
    	}
		
		asistencias=asistencias.sort{a,b ->
			a.empleado.perfil.ubicacion.clave<=>b.empleado.perfil.ubicacion.clave?:a.empleado.nombre<=>b.empleado.nombre
		}

    	asistencias.each{ asistencia->
    		
    		log.info 'Generando incentivos para asistencia: '+asistencia

    		def empleado=asistencia.empleado
    		Incentivo inc=Incentivo.find{asistencia==asistencia}
    		if(inc==null){
				inc=new Incentivo(
					tipo:'QUINCENAL',
					asistencia:asistencia,
					empleado:empleado,
					ubicacion:empleado.perfil.ubicacion,
					ejercicio:calendarioDet.calendario.ejercicio
				)
				
			}
			calcularIncentivoQuincenal(inc)
			inc.save failOnError:true
			log.info 'Incentivo generado/actualizado : '+inc

    	}
    }

    @NotTransactional
    def generarIncentivosMensuales(CalendarioDet calendarioDet,Mes mes) {

    	//def asistencias=Asistencia.findAll{calendarioDet==calendarioDet} 
    	def asistencias=Asistencia.executeQuery("from Asistencia a where a.calendarioDet=? and a.empleado.perfil.tipoDeIncentivo=?"
    		,[calendarioDet,'MENSUAL'])
    	if(!asistencias){
    		throw new RuntimeException("No se han generado y procesado asistencias para la quincena $calendarioDet.folio")
    	}
    	if(mes==null){
    		throw new RuntimeException("Se requiere el mes para el calculo")	
    	}
		
		asistencias=asistencias.sort{a,b ->
			a.empleado.perfil.ubicacion.clave<=>b.empleado.perfil.ubicacion.clave?:a.empleado.nombre<=>b.empleado.nombre
		}
		def ejercicio=calendarioDet.calendario.ejercicio

		def periodo = Periodo.getPeriodoEnUnMes(mes.clave,ejercicio)
    	asistencias.each{ asistencia->

    		def empleado=asistencia.empleado
    		if(empleado.perfil.tipoDeIncentivo=='MENSUAL'){
	    		Incentivo inc=Incentivo.find{tipo==tipo && ejercicio==ejercicio && mes==mes.nombre && empleado==empleado}
	    		if(inc==null){
					log.info 'Generando/actualizando incentivos mensuales usando asistencia: '+asistencia
					inc=new Incentivo(
						tipo:'MENSUAL',
						asistencia:asistencia,
						empleado:empleado,
						ubicacion:empleado.perfil.ubicacion,
						ejercicio:ejercicio,
						mes:mes.nombre,
						fechaInicial:periodo.fechaInicial,
						fechaFinal:periodo.fechaFinal
					)
					
					inc.save failOnError:true
				}
				calcularIncentivoMensual(inc)
				
    		}
    		

    	}
    }

    def calcularIncentivoMensual(Incentivo incentivo){
		log.debug 'Calculando bono mensual: '+incentivo.empleado
    	def bono1=incentivo.tasaBono1
		def per=new Periodo(incentivo.fechaInicial,incentivo.fechaFinal)
		//def per=new Periodo('01/07/2014','21/07/2014')
    	def rows=AsistenciaDet
    		.executeQuery("from AsistenciaDet a where a.asistencia.empleado=? and date(a.fecha) between ? and ?"
    	                                               ,[incentivo.empleado,per.fechaInicial,per.fechaFinal])
    	def minutos=rows.sum 0.0,{(it.retardoMenor+it.retardoMayor+it.retardoComida+it.retardoMenorComida)}
    	def faltas=rows.sum 0.0,{it.tipo=='FALTA'?1:0}
    	def incapacidades=rows.sum 0.0,{it.tipo=='INCAPACIDAD'?1:0}
    	def incidenciaf=rows.sum 0.0,{it.tipo=='INCIDENCIA_F'?1:0}
    	log.info "Dias: $rows.size Minutos: $minutos Faltas: $faltas Incapacidades: $incapacidades Incidencia_F: $incidenciaf"
		def checadasFaltantes=calcularChecadasFaltantes(rows)

    	faltas+=(incapacidades+incidenciaf)

    	if(!incentivo.otorgado){
    	  bono1=0.0
    	 
    	}else if(faltas>2){
    	  bono1=0.0
    	}else if(faltas==2){
    	  bono1=bono1/2
    	}

    	if(incentivo.calificacion=='REGULAR'){
    		bono1=bono1*0.5
    	}else if(incentivo.calificacion=='MALA'){
    		bono1=0.0
    	}

    	def bono2=0.0

    	if(bono1>0.0){
    	  if(minutos>49){
    	    bono2=bono1*0.875
    	  }else
    	  	bono2=bono1
    	}
		
		if(checadasFaltantes>2){
			bono2=0.0
			incentivo.comentario="CANCELADO POR $checadasFaltantes CHECADAS FALTANTES"
		}
    	incentivo.tasaBono2=bono2
		return incentivo
    }
	
	def calcularChecadasFaltantes(List registros){
		def faltantes=0
		registros.each{ det->
			
			if(det.tipo=='ASISTENCIA'){
				if(det.turnoDet.entrada1 && !det.entrada1)
					faltantes++
				if(det.turnoDet.salida1 && !det.salida1)
					faltantes++
				if(det.turnoDet.entrada2 && !det.entrada2)
					faltantes++
				if(det.turnoDet.salida2 && !det.salida2)
					faltantes++
			}
			/*
			if(det.asistencia.empleado.id==95){
				println 'Procesando Dia: '+det.fecha+ ' Checadas faltantes: '+faltantes  
			}
			*/
		}
		return faltantes
	}
	
	
    
    def Incentivo calcularIncentivoQuincenal(Incentivo bono){
    	log.info 'Calculando bono quincenal '+bono

    	def asistencia=bono.asistencia
    	
    	//Aplicando reglas
    	bono.otorgado=true
    	
		def minutosNoLaborados=asistencia.minutosNoLaborados
    	def retardoMayor=asistencia.retardoMayor
    	def retardoMenor=asistencia.retardoMenor
		
		bono.tasaBono1=0.0
		def retardoComida=asistencia.retardoComida+asistencia.retardoMenorComida
		def retardoTotal=retardoMayor+retardoMenor+retardoComida
		if(asistencia.minutosNoLaborados==0){
			if(asistencia.faltas+asistencia.incapacidades==0){
				if( (asistencia.retardoMenor+asistencia.retardoMayor)<=10){
					bono.tasaBono1=0.05
				}
				
			}
		}
		
		if(retardoComida==0){
			if(asistencia.faltas+asistencia.incapacidades==0){
				bono.tasaBono2=0.05
			}
		}
		
		//Para empleados nuevos
		if(asistencia.diasTrabajados>0){
			bono.tasaBono1=0.0
			bono.tasaBono2=0.0
		}
		
		//Casos especiales
		if([271l,255l].contains(asistencia.empleado.id)){
			bono.tasaBono1=0.05
			bono.tasaBono2=0.05
		}
		return bono
    }
	
	def Incentivo calcularIncentivoSemanal(Incentivo bono){
		log.debug 'Calculando bono semanal '+bono

    	def asistencia=bono.asistencia
    	
    	//Aplicando reglas
    	bono.otorgado=true
    	
		def minutosNoLaborados=asistencia.minutosNoLaborados
    	def retardoMayor=asistencia.retardoMayor
    	def retardoMenor=asistencia.retardoMenor
		
    	
		
		bono.tasaBono1=0.0
		def retardoComida=asistencia.retardoComida+asistencia.retardoMenorComida
		def retardoTotal=retardoMayor+retardoMenor+retardoComida
		if(asistencia.minutosNoLaborados==0){
			
			if(asistencia.faltas+asistencia.incapacidades==0){
				if( (asistencia.retardoMenor+asistencia.retardoMayor)<=5){
					bono.tasaBono1=0.05
				}
			}
		}
		
		if(retardoComida==0){
			if(asistencia.faltas+asistencia.incapacidades==0){
				bono.tasaBono2=0.05
			}
		}
		
		//Para empleados nuevos
		if(asistencia.diasTrabajados>0){
			bono.tasaBono1=0.0
			bono.tasaBono2=0.0
		}
		
		
		def checadasFaltantesComida=calcularChecadasFaltantesComida(asistencia.partidas)
		if(checadasFaltantesComida>0){
			bono.tasaBono2=0.0
			bono.comentario="CANCELADO POR $checadasFaltantesComida CHECADAS FALTANTES"
		}
		def checadasFaltantes=calcularChecadasFaltantesPrincipales(asistencia.partidas)
		if(checadasFaltantes>0){
			bono.tasaBono1=0.0
			bono.comentario="CANCELADO POR $checadasFaltantes CHECADAS FALTANTES"
		}
		
		 return bono
		
		 
	}
	
	def calcularChecadasFaltantesComida(List registros){
		def faltantes=0
		registros.each{ det->
			
			if(!det.excentarChecadas){
				if(det.turnoDet.salida1 && !det.salida1)
					faltantes++
				if(det.turnoDet.entrada2 && !det.entrada2)
					faltantes++
			}
			
			
				
		}
		return faltantes
	}
	
	def calcularChecadasFaltantesPrincipales(List registros){
		def faltantes=0
		registros.each{ det->
			if(!det.excentarChecadas){
				if(det.turnoDet.entrada1 && !det.entrada1)
					faltantes++
				if(det.turnoDet.salida2 && !det.salida2)
					faltantes++
			}
			
				
		}
		return faltantes
	}

}
