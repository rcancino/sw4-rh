package com.luxsoft.sw4.rh

import java.sql.Time
import java.util.Date

import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode
import com.luxsoft.sw4.rh.Ubicacion
import org.grails.databinding.BindingFormat

@ToString(includes='fecha,entrada1,salida1,entrada2,salida2',includeNames=true,includePackage=false)
//@EqualsAndHashCode(includes="fecha,entrada1,entrada2")
class AsistenciaDet {
	
	@BindingFormat("dd/MM/yyyy")
	Date fecha

	Time entrada1
	Time salida1
	
	Time entrada2
	Time salida2

	Time entrada3
	Time salida3
	
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
	
	BigDecimal horasTrabajadas=0
	
	Ubicacion ubicacion 
	
	String comentario

	String tipo
	
	Boolean manual=false
	
	Boolean cancelarIncentivo=false
	
	Boolean pagarTiempoExtra=false
	
	//Boolean empleadoNuevo=false
	
	Boolean excentarChecadas=false
	
	TurnoDet turnoDet
	
	Date dateCreated
	Date lastUpdated
	
	static belongsTo = [asistencia:Asistencia]
	
	//static embedded = ['periodo']

    static constraints = {
		
		tipo inList:['ASISTENCIA'
			,'FALTA'
			,'DESCANSO'
			,'DIA_FESTIVO'
			,'VACACIONES'
			,'INCAPACIDAD'
			,'INCIDENCIA'
			,'INCIDENCIA_F']
		entrada1 nullable:true
		entrada2 nullable:true
		entrada3 nullable:true
		salida1 nullable:true
		salida2 nullable:true
		salida3 nullable:true
		ubicacion nullable:true
		comentario nullable:true
		horasTrabajadas nullable:true
		manual nullable:true
		turnoDet nullable:true
    }
	
	static mapping = {
		fecha type:'date',index:'ASISTENCIA_DET_IDX'
		entrada1 type:'time'
		entrada2 type:'time'
		entrada3 type:'time'

		salida1 type:'time'
		salida2 type:'time'
		salida3 type:'time'
	}
}
