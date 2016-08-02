package com.luxsoft.sw4.rh.procesadores

import com.luxsoft.sw4.*
import com.luxsoft.sw4.rh.Asistencia
import com.luxsoft.sw4.rh.AsistenciaDet
import com.luxsoft.sw4.rh.Checado
import com.luxsoft.sw4.rh.DiaFestivo;
import com.luxsoft.sw4.rh.Empleado
import com.luxsoft.sw4.rh.TurnoDet

import java.sql.Time

import org.joda.time.Duration;
import org.joda.time.LocalTime;
import org.joda.time.Minutes

class ProcesadorDeChecadasFaltantes {
	
	
	def procesar(Asistencia asistencia) {
		log.debug 'Procesando checadas faltantes para: '+asistencia.empleado+"  Periodo: "+asistencia.periodo
		asistencia.partidas.findAll{it.tipo!='DESCANSO' && it.tipo!= 'FALTA'}.each{ it -> //Iteracion dia por dia

			def turnoDet=it.turnoDet

			def diaFestivo=DiaFestivo.findByFecha(it.fecha)

			def checadasRequeridas = 4
			def minimo = 3
			def checadas = 0
			

			if(diaFestivo && diaFestivo.parcial) {

				checadasRequeridas = 2
				minimo = 2
				if(it.entrada1) checadas ++
				if(it.salida1) checadas ++
					
			} else if(turnoDet.salida2){  // Entre semana

				if(it.entrada1) checadas ++
				if(it.salida1) checadas ++
				if(it.entrada2) checadas ++
				if(it.salida2) checadas ++
			} else { // Sabado
				checadasRequeridas = 2
				minimo = 2
				if(it.entrada1) checadas ++
				if(it.salida1) checadas ++
			}

			def faltantes = checadasRequeridas - checadas

			if(checadas == checadasRequeridas){
				log.info "OK ${asistencia.empleado} sin checadas faltantes el ${it.fecha.text()}"

			} else if(checadas < minimo){
				log.info "${asistencia.empleado} excede el minimo de checadas ${minimo} para  ${it.fecha.text()} acredita FALTA"
				it.comentario="FALTA POR ${faltantes} CHECADAS FALTANTES "
				it.tipo='FALTA'
			} else {
				log.info " Procesando minutos no laborados por ${faltantes} checadas faltantes"
				actualizarMinutosNolaborados(it)
			}
		}
		return asistencia
	}

	def actualizarMinutosNolaborados(AsistenciaDet det, int faltantes ) {
		def turno=det.turnoDet
		
		LocalTime entrada1 = det.entrada1 ? LocalTime.fromDateFields(det.entrada1) : null
		LocalTime salida1 = det.salida1 ? LocalTime.fromDateFields(det.salida1) : null
		LocalTime entrada2 = det.entrada2 ? LocalTime.fromDateFields(det.entrada2) : null
		LocalTime salida2 = det.salida2 ? LocalTime.fromDateFields(det.salida2) : null
		
		if(faltantes == 2){
			
			if(!det.entrada1 && !det.salida2){
				det.minutosNoLaborados = getMinutos(turno.entrada1, turno.salida2)
				return
			}
			if(!det.salida2) {
				det.minutosNoLaborados = getMinutos(turno.entrada2, turno.salida2) 
				return
			}
			if(!det.entrada2 && !det.salida2 ){
				det.minutosNoLaborados = getMinutos(salida1,turno.salida2)
				return
			}
			if(!det.entrada1 && !det.salida1){
				det.minutosNoLaborados = getMinutos(turno.entrada1, entrada2) 
				return	
			}
			if(!det.entrada2 && !det.salida1){
				det.minutosNoLaborados = 60
				return		
			}
		}
		if(faltantes == 1) {

			if(!det.salida1 || !det.entrada2){
				det.minutosNoLaborados = 60
				return		
			}

			if(!det.entrada1) {
				det.minutosNoLaborados = getMinutos(turno.entrada1, salida1) 
				return	
			}
			if(!det.salida2) {
				det.minutosNoLaborados = getMinutos(entrada2, turno.salida2) 
				return		
			}
		}
		
	}

	det getMinutos(LocalTime start, LocalTime end) {
		return Minutes.minutesBetween(start, end).getMinutes() 
	}
}



