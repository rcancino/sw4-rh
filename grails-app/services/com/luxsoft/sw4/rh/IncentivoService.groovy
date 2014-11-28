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
	//def generarIncentivosMensuales(CalendarioDet calendarioDet,Mes mes) {
    def generarIncentivosMensuales(Integer ejercicio,Mes mes) {

    	//def asistencias=Asistencia.findAll{calendarioDet==calendarioDet}
		/*
    	def asistencias=Asistencia.executeQuery("from Asistencia a where a.calendarioDet=? and a.empleado.perfil.tipoDeIncentivo=?"
    		,[calendarioDet,'MENSUAL'])
    	if(!asistencias){
    		throw new RuntimeException("No se han generado y procesado asistencias para la quincena $calendarioDet.folio")
    	}
    	*/
    	if(mes==null){
    		throw new RuntimeException("Se requiere el mes para el calculo")	
    	}
		//def ejercicio=calendarioDet.calendario.ejercicio

		def periodo = Periodo.getPeriodoEnUnMes(mes.clave,ejercicio)
		def empleados=Empleado.findAll ("from Empleado e where e.perfil.tipoDeIncentivo=? and e.status in('ALTA','REINGRESO')"
			,['MENSUAL'])
    	empleados.each{ empleado->
    		if(empleado.perfil.tipoDeIncentivo=='MENSUAL'){
				
				boolean valido=validarEmpleadoParaIncentivoMensual(empleado,ejercicio,mes)
				if(valido){
					Incentivo inc=Incentivo.find{tipo==tipo && ejercicio==ejercicio && mes==mes.nombre && empleado==empleado}
					if(inc==null){
						log.info 'Generando incentivo mensuales : '+empleado
						inc=new Incentivo(
							tipo:'MENSUAL',
							empleado:empleado,
							ubicacion:empleado.perfil.ubicacion,
							ejercicio:ejercicio,
							mes:mes.nombre,
							fechaInicial:periodo.fechaInicial,
							fechaFinal:periodo.fechaFinal
						)
						
						
						inc.save failOnError:true
					}
					//calcularIncentivoMensual(inc)
				}
    		}
    	}
    }

    def calcularIncentivoMensual(Incentivo incentivo){
		log.debug 'Calculando bono mensual: '+incentivo.empleado
		
    	def bono1=incentivo.tasaBono1
		def bonoOriginal=bono1
		
		def per=new Periodo(incentivo.fechaInicial,incentivo.fechaFinal)
		//def per=new Periodo('01/07/2014','21/07/2014')
		def inicio=per.fechaInicial
		if(incentivo.empleado.alta>inicio)
			inicio=incentivo.empleado.alta
    	def rows=AsistenciaDet
    		.executeQuery("from AsistenciaDet a where a.asistencia.empleado=? and date(a.fecha) between ? and ?"
    	                                               ,[incentivo.empleado,inicio,per.fechaFinal])
    	def minutos=rows.sum 0.0,{(it.retardoMenor+it.retardoMayor+it.retardoComida+it.retardoMenorComida)}
    	def faltas=rows.sum 0.0,{it.tipo=='FALTA'?1:0}
    	def incapacidades=rows.sum 0.0,{it.tipo=='INCAPACIDAD'?1:0}
    	def incidenciaf=rows.sum 0.0,{it.tipo=='INCIDENCIA_F'?1:0}
    	
		def checadasFaltantes=calcularChecadasFaltantes(rows)
		log.info "Dias: ${rows.size()} Minutos: $minutos Faltas: $faltas Incapacidades: $incapacidades Incidencia_F: $incidenciaf Tasa bono 1:${bono1} Checadas faltantes:${checadasFaltantes}"
    	faltas+=(incapacidades+incidenciaf)
		
		incentivo.checadasFaltantes=checadasFaltantes
		incentivo.minutosNoLaborados=minutos
		incentivo.faltas=faltas
		println 'Incentivo manual: '+incentivo.manual
		if(incentivo.manual){
			//def proporcion=calcularProporcion(incentivo)
			//incentivo.tasaBono2=incentivo.tasaBono2*proporcion
			incentivo.ingresoBase=incentivo.empleado.salario.salarioDiario*30
			incentivo.incentivo=(incentivo.ingresoBase)*incentivo.tasaBono2
			log.info "Incentivo manual proporcion  tasa: ${incentivo.tasaBono2}"
			return incentivo
		}
		def bono2=0.0
		
		if(faltas>2){
			incentivo.tasaBono2=0.0
			incentivo.incentivo=0.0
			return incentivo;
		}
		
		if(incentivo.calificacion=='MALA'){
			incentivo.tasaBono2=0.0
			incentivo.incentivo=0.0
			return incentivo;
		}
		if(incentivo.calificacion=='REGULAR'){
			bono2=bono1/2
		}
		if(incentivo.calificacion=='BUENA'){
			bono2=bono1
		}
		
		def perdida=0.0
		
		if(faltas==2){
			perdida=0.125
		}
		if(checadasFaltantes>2){
			perdida+=0.05
		}
		if(minutos>49){
			perdida+=0.125
		}
		log.info 'Perdida: '+perdida
		bono2=bono2*(1-perdida)	
		
		def proporcion=calcularProporcion(incentivo)
		
    	incentivo.tasaBono2=bono2*proporcion
		incentivo.ingresoBase=incentivo.empleado.salario.salarioDiario*30
		//incentivo.incentivo=incentivo.ingresoBase*bono2
		incentivo.incentivo=(incentivo.ingresoBase*proporcion)*bono2
		
		//Es valido
		def mes=Mes.findMesByNombre(incentivo.mes)
		boolean valido=validarEmpleadoParaIncentivoMensual(incentivo.empleado,incentivo.ejercicio,mes)
		if(!valido){
			incentivo.tasaBono2=0.0
			incentivo.incentivo=0.0
			incentivo.comentario='NO PARTICIPO INVENTARIO'
		}
		
		log.info "Perdida: ${perdida} Proporcion: $proporcion Ingreso base:${incentivo.ingresoBase} Bono2 :${bono2}"
		return incentivo
    }
	
	def calcularChecadasFaltantes(List registros){
		def faltantes=0
		registros.each{ det->
			
			def diaFestivo=DiaFestivo.findByFecha(det.fecha)
			if(diaFestivo){
				log.info 'Evluando dia festivo: '+diaFestivo.fecha
				if(diaFestivo.parcial){
					if(det.entrada1 && det.salida1){
						faltantes++;
					}
				}
			}else{
			if(det.tipo=='ASISTENCIA' && (diaFestivo==null)){
				if(det.turnoDet.entrada1 && !det.entrada1)
					faltantes++
				if(det.turnoDet.salida1 && !det.salida1)
					faltantes++
				if(det.turnoDet.entrada2 && !det.entrada2)
					faltantes++
				if(det.turnoDet.salida2 && !det.salida2)
					faltantes++
			}
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
		if(bono.manual){
			return bono
		}
    	def asistencia=bono.asistencia
    	
    	//Aplicando reglas
    	bono.otorgado=true
    	
		def minutosNoLaborados=asistencia.minutosNoLaborados
    	def retardoMayor=asistencia.retardoMayor
    	def retardoMenor=asistencia.retardoMenor
		
		
		bono.tasaBono1=0.0
		def retardoComida=asistencia.retardoComida+asistencia.retardoMenorComida
		//def retardoTotal=retardoMayor+retardoMenor+retardoComida
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
		
		
		def rows=asistencia.partidas
		def checadasFaltantes=calcularChecadasFaltantes(rows)
		log.info 'Checadas faltantes: '+checadasFaltantes
		if(checadasFaltantes>0){
			
			bono.tasaBono2=0.0
			bono.comentario="CANCELADO POR $checadasFaltantes CHECADA(S) FALTANTES"
			log.info 'Checadas faltantes $checadasFaltantes cancelando bono2'
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
		if(bono.manual){
			return bono
		}
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
			bono.comentario+="B1 (CANCELADO POR $checadasFaltantesComida CHEC FALTANTES)"
		}
		def checadasFaltantes=calcularChecadasFaltantesPrincipales(asistencia.partidas)
		if(checadasFaltantes>0){
			bono.tasaBono1=0.0
			bono.comentario+=" B2 (CANCELADO POR $checadasFaltantes CHEC FALTANTES)"
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
	
	def boolean validarEmpleadoParaIncentivoMensual(Empleado e,Integer ejercicio,Mes mes){
		def alta=e.alta
		def inventario=CalendarioDet.find(
			"from CalendarioDet det  where det.calendario.ejercicio=? and det.mes=? and det.calendario.tipo=? and det.calendario.comentario=?",
			[ejercicio,mes.nombre,'ESPECIAL','INVENTARIO'])
		//println 'Validando participacion en inventario: '+inventario
		if(inventario){
			log.info 'Evaluando calendario de inventario: '+inventario
			return alta<=inventario.inicio
		}
		return true
	}
	
	def calcularProporcion(Incentivo incentivo){
		def alta=incentivo.empleado.alta
		if(alta<incentivo.fechaInicial)
			return 1.0;
		else if(alta>incentivo.fechaInicial && alta<=incentivo.fechaFinal){
			def diasTrabajados=incentivo.fechaFinal-alta
			def totalDias=30
			return diasTrabajados/totalDias //La parte proporcional
		}else
			return 0.0	
	}

}
