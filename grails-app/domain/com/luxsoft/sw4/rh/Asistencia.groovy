package com.luxsoft.sw4.rh

import java.sql.Time

import com.luxsoft.sw4.Periodo;

import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode

@ToString(includes='empleado,periodo',includeNames=true,includePackage=false)
@EqualsAndHashCode(includes='empleado,periodo')
class Asistencia {

	Empleado empleado
	
	Periodo periodo
	
	String tipo
	
	Integer asistencias=0
	
	Integer vacaciones=0
	
	Integer incapacidades=0
	
	/**
	 * Numero de faltas acumuladas en el periodo
	 */
	Integer faltas=0
	
	/**
	 * Es el acumulado de retardos diarios menores a 10 minutos
	 *  - Si en el periodo sobrepasa los 10 minutos pierde incentivo
	 *  - El retardo
	 */
	Integer retardoMenor=0
	
	/**
	 * Es el acumulado de retardos diarios mayores a 10 minutos
	 *  - Se pierde el incentivo
	 *  - Descuento de nomina por el acumulado
	 */
	Integer retardoMayor=0
	
	/**
	 * Es el acumulado del retrsaso diario al regreso de la comida
	 *  - Con un minuto se pierde el incentivo
	 *
	 */
	Integer retardoComida=0
	
	static hasMany = [partidas:AsistenciaDet]
	
	String comentario
	
	Date dateCreated
	Date lastUpdated

    static constraints = {
		
    	comentario nullable:true
		tipo inList:['SEMANAL','QUINCENAL']
    	
    }

	static mapping = {
		partidas cascade: "all-delete-orphan"
	}
	
	static embedded = ['periodo']
}
