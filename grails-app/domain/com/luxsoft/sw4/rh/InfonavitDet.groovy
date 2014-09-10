package com.luxsoft.sw4.rh

import org.grails.databinding.BindingFormat
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.time.TimeCategory

@EqualsAndHashCode(includes='ejercicio,bimestre')
@ToString(excludes="dateCreated,lastUpdated,infonavit",includeNames=true,includePackage=false)
class InfonavitDet {
	
	Integer ejercicio

	Integer bimestre

	@BindingFormat('dd/MM/yyyy')
	Date fechaInicial

	@BindingFormat('dd/MM/yyyy')
	Date fechaFinal

	Integer diasDelBimestre

	BigDecimal cuota=0.0 //Puede ser vsm,%,cuotaFija

	BigDecimal salarioMinimoGeneral //Salario minimo general

	BigDecimal salarioDiarioIntegrado

	BigDecimal cuotaDiaria=0.0

	Integer faltas=0

	Integer incapacidades=0

	BigDecimal seguroDeVivienda=0.0

	BigDecimal importeBimestral=0.0
	
	BigDecimal cuotaBimestral=0.0

	BigDecimal acumulado=0.0

	BigDecimal saldo=0.0

	Date dateCreated
	Date lastUpdated

	static belongsTo = [infonavit: Infonavit]
	

    static constraints = {
    	ejercicio range:2014..2020
    	bimestre range:1..6
    }

    static mapping = {
    	fechaInicial type:'date'
    	fechaFinal type:'date'
    }

    public Integer dias(){
		use(TimeCategory){
			def duration= fechaFinal-fechaInicial+1.day
			return duration.days
		}
	}

	
}
