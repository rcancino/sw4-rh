package com.luxsoft.sw4.rh

import groovy.transform.EqualsAndHashCode
import org.grails.databinding.BindingFormat

@EqualsAndHashCode(includes="fecha,tipo")
class IncidenciaDet {

	@BindingFormat("dd/MM/yyyy")
	Date fecha
	BigDecimal horas=8.0
	BigDecimal horasJornada=8.0
	BigDecimal importe
	BigDecimal importeProporcional
	BigDecimal total
	String tipo
	String comentario

	static belongsTo = [incidencia: Incidencia]

    static constraints = {
    	tipo inList:['FALTA','RETARDO','PERMISO']
    }

    static mapping = {
    	fecha type:'date'
    }

    String toString(){
    	return "${fecha.format('dd/MM/yyyy')} $tipo $importe"
    }

    public IncidenciaDet actualizar(def salario,def diasLaborables){
		this.importe=(salario/horasJornada)*this.horas
		return this
    }

    public IncidenciaDet actualizarImporteProporcional(def salario,def diasLaborables,def diasDeDescanso){
		this.importeProporcional=(salario*this.horas)/ ((diasLaborables-diasDeDescanso)*horasJornada)
		return this
    }

}
