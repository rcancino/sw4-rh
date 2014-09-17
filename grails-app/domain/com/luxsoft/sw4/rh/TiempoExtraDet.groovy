package com.luxsoft.sw4.rh

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import org.grails.databinding.BindingFormat

import com.luxsoft.sw4.MonedaUtils

@EqualsAndHashCode(includes="semana")
@ToString(excludes='tiempoExtra',includeNames=true,includePackage=false)
class TiempoExtraDet {

	Integer semana

	BigDecimal salarioDiario

	int lunes

	int martes

	int miercoles

	int jueves

	int viernes

	int sabado
	
	int domingo
	
	int minutosDobles
	
	int minutosTriples

	int totalMinutos
	
	BigDecimal importeDoblesExcentos=0.0
	
	BigDecimal importeDoblesGravados=0.0

	BigDecimal importeTriplesGravados=0.0
	
	


	static belongsTo = [tiempoExtra:TiempoExtra]

	static transients = ['salarioPorMinuto','total']
	
	static hasOne = [tiempoExtraImss:TiempoExtraImss]
	
    static constraints = {
		
    }

	/*
    BigDecimal getSalarioDiario(){
    	return MonedaUtils.round( (salarioDiario/8/60),4)
    }
	*/
	
	def getSalarioPorMinuto(){
		return MonedaUtils.round( (salarioDiario/8/60),4)
	}

    def getTotal(){
    	return importeDoblesExcentos+importeDoblesGravados+importeTriplesGravados
    }
	
	
}
