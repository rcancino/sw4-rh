package com.luxsoft.sw4.rh

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode(includes="semana")
@ToString(excludes='tiempoExtraDet',includeNames=true,includePackage=false)
class TiempoExtraImss {
	
	
	Integer semana
	BigDecimal lunes=0.0
	BigDecimal martes=0.0
	BigDecimal miercoles=0.0
	BigDecimal jueves=0.0
	BigDecimal viernes=0.0
	BigDecimal sabado=0.0
	BigDecimal domingo=0.0
	
	BigDecimal total=0.0
	BigDecimal integra=0.0
	
	TiempoExtraDet tiempoExtraDet
	
	static belongsTo = [tiempoExtraDet:TiempoExtraDet]

    static constraints = {
    }
}
