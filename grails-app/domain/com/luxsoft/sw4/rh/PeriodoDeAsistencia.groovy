package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Periodo

class PeriodoDeAsistencia {
	
	
	Empleado empleado
	Periodo periodo
	String tipo
	Integer asistencias=0
	Integer vacaciones=0
	Integer incapacidades=0
	Integer faltas=0
	
	Integer retardoMenor
	Integer retardoMayor
	Integer retardoComida
	
	static embedded = ['periodo']
	
	static hasMany = [partidas:Asistencia]

    static constraints = {
		tipo inList:['SEMANAL','QUINCENAL']
    }
	
	static mapping = {
		partidas cascade: "all-delete-orphan"
		
	}
	
}
