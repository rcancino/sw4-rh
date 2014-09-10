package com.luxsoft.sw4.rh

import org.grails.databinding.BindingFormat
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.time.TimeCategory

@EqualsAndHashCode(includes='ejercicio,bimestre')
@ToString(includes='ejercicio,bimestre,numero,importeBimestral,acumulado,saldo,fechaInicial,fechaFinal',includeNames=true,includePackage=false)
class InfonavitDet {
	
	Integer ejercicio

	Integer bimestre

	@BindingFormat('dd/MM/yyyy')
	Date fechaInicial

	@BindingFormat('dd/MM/yyyy')
	Date fechaFinal

	Integer diasDelBimestre

	BigDecimal cuota //Puede ser vsm,%,cuotaFija

	BigDecimal salarioMinimoGeneral //Salario minimo general

	BigDecimal salarioDiarioIntegrado

	BigDecimal cuotaDiaria

	Integer faltas

	Integer incapacidades

	BigDecimal seguroDeVivienda

	BigDecimal importeBimestral

	BigDecimal acumulado

	BigDecimal saldo

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

	def beforeInsert() {
		diasDelBimestre=dias()
	}
	def beforeUpdate() {
		diasDelBimestre=dias()
	}
}
