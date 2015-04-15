package com.luxsoft.sw4.rh

import groovy.time.TimeCategory
import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode
import org.grails.databinding.BindingFormat

import com.luxsoft.sw4.Empresa
import com.luxsoft.sw4.Periodo

@ToString(includes='tipo,ejercicio,periodicidad,folio,formaDePago',includeNames=true,includePackage=false)
@EqualsAndHashCode(includes='empresa,tipo,folio')
class Nomina {
	
	Empresa empresa
	Integer ejercicio
	Integer folio  //??
	Periodo periodo
	Integer diasPagados
	
	
	
	@BindingFormat('dd/MM/yyyy')
	Date pago // fecha de pago aproximado
	
	/** Inician propiedades que podrian ir en una configuracion general**/
	//ConfiguracionDeNomina configuracion
	String tipo
	String diaDePago  // esto podria ser una bean de dias
	String periodicidad
	String formaDePago
	BigDecimal total //Valor de la nomina
	
	String status
	Date corte
	
	//SortedSet partidas
	CalendarioDet calendarioDet
	
	Date dateCreated
	Date lastUpdated
	
	static embedded = ['periodo']
	
	static transients=['diasPagados','totalCalculado']

    static constraints = {
		tipo inList:['GENERAL','ESPECIAL','AGUINALDO','PTU']
		diaDePago maxSize:20
		periodicidad inList:['SEMANAL','QUINCENAL','MENSUAL','ANUAL','ESPECIAL']
		formaDePago inList:['CHEQUE','TRANSFERENCIA']
		status inList:['PENDIENTE','REVISION','CERRADA']
		corte nullable:true
		calendarioDet nullable:true
		ejercicio nullable:true
    }

    static hasMany = [partidas: NominaPorEmpleado]
	
	static mapping = {
		partidas cascade: "all-delete-orphan"
	}
	
	def Integer getDiasPagados(){
	/*	use(TimeCategory){
			def duration= periodo.fechaFinal-periodo.fechaInicial+1.day
			return duration.days
		}*/
		def duration=(periodo.fechaFinal-periodo.fechaInicial)+1
		
	}

	def beforeUpdate() {
		total=partidas.sum 0.0,{it.total}
	}

	def beforeInsert() {
		total=partidas.sum 0.0,{it.total}
	}
	
	BigDecimal getTotalCalculado(){
		def tot= partidas.sum 0.0,{it.total}
		return tot
	}
	
	
}
