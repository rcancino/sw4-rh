package com.luxsoft.sw4.rh

import java.math.BigDecimal;
import java.sql.Time

import com.luxsoft.sw4.Periodo

import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode

@ToString(includes='empleado,periodo',includeNames=true,includePackage=false)
@EqualsAndHashCode(includes='empleado,periodo')
class Asistencia {

	Empleado empleado
	
	Periodo periodo
	
	CalendarioDet calendarioDet
	
	String tipo
	
	Integer asistencias=0
	
	Integer vacaciones=0
	
	Integer vacacionesp=0
	
	Integer incapacidades=0
	
	/**
	 * Icidencias no pagadas
	 */
	Integer incidencias=0
	
	BigDecimal diasTrabajados=0.0
	
	/**
	 * Numero de faltas ausencias e incidencias_f (no pagadas) en el periodo
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
	
	Integer retardoMenorComida=0
	
	/**
	 * El total de minutos no laborados, esto al compara las checadas con los horarios establecidos 
	 *
	 */
	Integer minutosNoLaborados=0
	
	Integer minutosPorDescontar=0
	
	BigDecimal horasTrabajadas
	
	
	
	List partidas
	
	static hasMany = [partidas:AsistenciaDet]
	
	String comentario
	
	Integer paternidad=0
	
	Date dateCreated
	Date lastUpdated

    static constraints = {
		
    	comentario nullable:true
		tipo inList:['SEMANAL','QUINCENAL']
		calendarioDet nullable:true
    	horasTrabajadas nullable:true
		paternidad nullable:true
    }

	static mapping = {
		partidas cascade: "all-delete-orphan"
	}
	
	static embedded = ['periodo']
}
