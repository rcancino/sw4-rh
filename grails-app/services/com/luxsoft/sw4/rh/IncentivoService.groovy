package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import grails.transaction.NotTransactional

@Transactional
class IncentivoService {

	//@NotTransactional
    def generarIncentivos(CalendarioDet ini,CalendarioDet fin) {

    	def tipo=ini.calendario.tipo=='SEMANA'?'SEMANAL':'QUINCENAL'
    	def empleados=Empleado.findAll(
			"from Empleado e where e.salario.periodicidad=? order by e.perfil.ubicacion.clave,e.apellidoPaterno asc",[tipo])
    	empleados.each{ empleado ->
    		if( (!empleado.baja) 
				|| (empleado.baja.fecha<empleado.alta)
				|| (empleado.baja.fecha>=ini.asistencia.fechaInicial) ){
    			
				Incentivo inc=Incentivo.find{tipo==tipo && calendarioIni==ini && calendarioFin==fin && empleado==empleado}
				
				if(inc==null){
					inc=new Incentivo(
							tipo:tipo,
							calendarioIni:ini,
							calendarioFin:fin,
							empleado:empleado,
							ubicacion:empleado.perfil.ubicacion,
							ejercicio:ini.calendario.ejercicio
						)
					//log.info 'Incentivo nuevo: '+inc
				}
					//calcular(inc)
				if(tipo=='QUINCENAL')
					inc=aplicarBonoQuncenal(inc)
				if(tipo=='SEMANAL')
					inc=aplicarBonoSemanal(inc)
				inc.save failOnError:true
				log.info 'Incentivo nuevo: '+inc


    			
    		}
    	}
    }

    //SI(MinNoLab>0,0,SI(G21>=1,0,SI(H21+I21>10,0,0.05)))

    def Incentivo aplicarBonoQuncenal(Incentivo bono){
    	log.debug 'Calculando bono quincenal '+bono
    	def asistencia=Asistencia.findByCalendarioDetAndEmpleado(bono.calendarioIni,bono.empleado)
    	if(asistencia==null){
			bono.tasaBono1=0.0
			bono.tasaBono2=0.0
    		bono.comentario='No hay registros de asistencia'
    		return bono
    	}
    	//Aplicando reglas
    	bono.otorgado=true
    	bono.faltas=asistencia.faltas
		bono.minutosNoLaborados=asistencia.minutosNoLaborados
    	bono.retardoMayor=asistencia.retardoMayor
    	bono.retardoMenor=asistencia.retardoMenor
    	bono.retardoComida=asistencia.retardoComida+asistencia.retardoMenorComida
    	bono.retardoTotal=bono.retardoMayor+bono.retardoMenor+bono.retardoComida
		
		bono.tasaBono1=0.0
		
		if(bono.minutosNoLaborados==0){
			if(bono.faltas==0){
				if(bono.retardoMenor+bono.retardoMayor<=10){
					bono.tasaBono1=0.05
				}
				
			}
		}
		
		if(bono.retardoComida==0){
			def incapacidades=asistencia.incapacidades
			if(bono.faltas+incapacidades==0){
				bono.tasaBono2=0.05
			}
		}
		
		
		
		//Para empleados nuevos
		if(asistencia.diasTrabajados>0){
			bono.tasaBono1=0.0
			bono.tasaBono2=0.0
		}
		
		//Casos especiales
		if(asistencia.empleado.id==271 || asistencia.empleado.id==255){
			bono.tasaBono1=0.05
			bono.tasaBono2=0.05
		}
		
		 return bono
    }
	
	def Incentivo aplicarBonoSemanal(Incentivo bono){
		log.debug 'Calculando bono quincenal '+bono
		
		def asistencia=Asistencia.findByCalendarioDetAndEmpleado(bono.calendarioIni,bono.empleado)
		if(asistencia==null){
			bono.tasaBono1=0.0
			bono.tasaBono2=0.0
    		bono.comentario='No hay registros de asistencia'
    		return bono
    	}

    	//Aplicando reglas
    	bono.otorgado=true
    	bono.faltas=asistencia.faltas
		bono.minutosNoLaborados=asistencia.minutosNoLaborados
    	bono.retardoMayor=asistencia.retardoMayor
    	bono.retardoMenor=asistencia.retardoMenor
    	bono.retardoComida=asistencia.retardoComida+asistencia.retardoMenorComida
    	bono.retardoTotal=bono.retardoMayor+bono.retardoMenor+bono.retardoComida
    	bono.tasaBono1=0.0
		return bono
		
		 
	}

}
