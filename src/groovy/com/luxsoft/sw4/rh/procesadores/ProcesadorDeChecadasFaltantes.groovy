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

class ProcesadorDeChecadasFaltantes {
	
	
	def procesar(Asistencia asistencia) {
		log.info 'Procesando checadas faltantes para: '+asistencia.empleado+"  Periodo: "+asistencia.periodo
		asistencia.partidas.each{ it -> //Iteracion dia por dia

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
					
			} else {

				if(it.entrada1) checadas ++
				if(it.salida1) checadas ++
				if(it.entrada2) checadas ++
				if(it.salida2) checadas ++
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

	def actualizarMinutosNolaborados(AsistenciaDet det) {
		
		//it.minutosNoLaborados=0
	}
}



