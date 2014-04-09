package com.luxsoft.sw4.rh

import groovy.time.TimeCategory
import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode
import org.grails.databinding.BindingFormat

import com.luxsoft.sw4.Empresa
import com.luxsoft.sw4.Periodo

@ToString(includes='tipo,periodicidad,folio,periodo',includeNames=true,includePackage=false)
@EqualsAndHashCode(includes='empresa,tipo,folio')
class Nomina {
	
	Empresa empresa
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
	
	Date dateCreated
	Date lastUpdated
	
	static embedded = ['periodo']
	
	static transients=['diasPagados']

    static constraints = {
		tipo inList:['GENERAL','ESPECIAL','AGINALDO','UTILIDADES']
		diaDePago inList:['LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO']
		periodicidad inList:['SEMANAL','QUINCENAL','MENSUAL','ANUAL','ESPECIAL']
		formaDePago inList:['CHEQUE','TRANSFERENCIA']
    }

    static hasMany = [partidas: NominaPorEmpleado]
	
	static mapping = {
		partidas cascade: "all-delete-orphan"
	}
	
	public Integer getDiasPagados(){
		use(TimeCategory){
			def duration= periodo.fechaFinal-periodo.fechaInicial+1.day
			return duration.days
		}
	}

	
	
	
}
