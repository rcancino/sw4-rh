package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import grails.transaction.NotTransactional

@Transactional
class IncentivoService {

	//@NotTransactional
    def generarIncentivosQuincenales(CalendarioDet calendarioDet) {

    	def asistencias=Asistencia.findAll{calendarioDet==calendarioDet} 
    	asistencias.each{ asistencia->

    		def empleado=asistencia.empleado
    		Incentivo inc=Incentivo.find{tipo==tipo  && empleado==empleado && asistencia==asistencia}
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
