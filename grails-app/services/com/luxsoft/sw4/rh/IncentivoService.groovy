package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import grails.transaction.NotTransactional
import com.luxsoft.sw4.Mes
import com.luxsoft.sw4.Periodo

@Transactional
class IncentivoService {

	@NotTransactional
    def generarIncentivosQuincenales(CalendarioDet calendarioDet) {

    	def asistencias=Asistencia.findAll{calendarioDet==calendarioDet} 
    	if(!asistencias){
    		throw new RuntimeException("No se han generado y procesado asistencias para la quincena $calendarioDet.folio")
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
				inc.save failOnError:true
			}

    	}
    }

    @NotTransactional
    def generarIncentivosMensuales(CalendarioDet calendarioDet,Mes mes) {

    	def asistencias=Asistencia.findAll{calendarioDet==calendarioDet} 
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
		log.info 'Calculando bono mensual: '+incentivo.empleado
    	def bono1=0.1
		//def per=new Periodo(incentivo.fechaInicial,incentivo.fechaFinal)
		def per=new Periodo('01/07/2014','21/07/2014')
    	def rows=AsistenciaDet
    		.executeQuery("from AsistenciaDet a where a.asistencia.empleado=? and date(a.fecha) between ? and ?"
    	                                               ,[incentivo.empleado,per.fechaInicial,per.fechaFinal])
    	def minutos=rows.sum 0.0,{it.retardoMenor+it.retardoMenorComida}
    	def faltas=rows.sum 0.0,{it.tipo=='FALTA'?1:0}
    	def incapacidades=rows.sum 0.0,{it.tipo=='INCAPACIDAD'?1:0}
    	def incidenciaf=rows.sum 0.0,{it.tipo=='INCIDENCIA_F'?1:0}
    	log.debug "Dias: $rows.size Minutos: $minutos Faltas: $faltas Incapacidades: $incapacidades Incidencia_F: $incidenciaf"

    	faltas+=(incapacidades+incidenciaf)

    	if(!incentivo.otorgado){
    	  bono1=0.0
    	 
    	}else if(faltas>2){
    	  bono1=0.0
    	}else if(faltas==2){
    	  bono1=bono1/2
    	}

    	def bono2=0.0

    	if(bono1>0.0){
    	  if(minutos>49){
    	    bono2=bono1*0.875
    	  }else
    	  	bono2=bono1
    	}
    	incentivo.tasaBono1=bono1
    	incentivo.tasaBono2=bono2

    }

    def calcularIncentivo(Integer ejercicio,String tipo){
    	def incentivos=Incentivo.findAll{tipo==tipo && ejercicio==ejercicio}
    }

    //SI(MinNoLab>0,0,SI(G21>=1,0,SI(H21+I21>10,0,0.05)))

    def Incentivo aplicarBonoQuncenal(Incentivo bono){
    	log.debug 'Calculando bono quincenal '+bono
    	def asistencia=bono.asistencia
    	
    	//Aplicando reglas
    	bono.otorgado=true
    	bono.faltas=asistencia.faltas
		bono.minutosNoLaborados=asistencia.minutosNoLaborados
    	bono.retardoMayor=asistencia.retardoMayor
    	bono.retardoMenor=asistencia.retardoMenor
    	
    	
		
		bono.tasaBono1=0.0
		def retardoComida=retardoComida=asistencia.retardoComida+asistencia.retardoMenorComida
		def retardoTotal=bono.retardoMayor+bono.retardoMenor+bono.retardoComida
		if(asistencia.minutosNoLaborados==0){
			if(asistencia.faltas==0){
				if(asistencia.retardoMenor+asistencia.retardoMayor<=10){
					bono.tasaBono1=0.05
				}
				
			}
		}
		
		if(retardoComida==0){
			def incapacidades=asistencia.incapacidades
			if(asistencia.faltas+incapacidades==0){
				bono.tasaBono2=0.05
			}
		}
		
		//Para empleados nuevos
		if(asistencia.diasTrabajados>0){
			bono.tasaBono1=0.0
			bono.tasaBono2=0.0
		}
		
		 return bono
    }
	
	def Incentivo aplicarBonoSemanal(Incentivo bono){

		return bono
		
		 
	}

}
