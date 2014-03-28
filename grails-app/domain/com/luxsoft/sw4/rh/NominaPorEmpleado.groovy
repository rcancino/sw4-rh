package com.luxsoft.sw4.rh

import com.luxsoft.sw4.cfdi.Cfdi;

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString


@EqualsAndHashCode(includes='empleado')
@ToString(includePackage=false,includeNames=true,excludes='dateCreated,lastUpdated')
class NominaPorEmpleado {

	
	Empleado empleado 
	Ubicacion ubicacion
	BigDecimal salarioDiarioBase
	BigDecimal salarioDiarioIntegrado
	BigDecimal total
	BigDecimal totalGravado
	BigDecimal totalExcento
	
	BigDecimal baseGravable
	BigDecimal subsidioEmpleoAplicado
	BigDecimal impuestoSubsidio
	List conceptos
	Cfdi cfdi
	
	
	/* Tiempo extra ?? */
	String comentario
	
	Integer antiguedadEnSemanas

	Date dateCreated
	Date lastUpdated

    static constraints = {
    	comentario nullable:true,maxSize:200
		antiguedadEnSemanas nullable:false,minSize:1
		cfdi nullable:true 
    }
	
	static transients=['antiguedad'
		,'percepciones',
		,'deducciones',
		'prececionesGravadas',
		'percepcionesExcentas',
		'deduccionesGravadas',
		'deduccionesExcentas'
		]

    static belongsTo = [nomina: Nomina]

    static hasMany = [conceptos: NominaPorEmpleadoDet]
	
	Integer getAntiguedad(){
		return (int)Math.floor((nomina?.pago-empleado?.alta)/7)
	}
	
	BigDecimal getPercepciones() {
		conceptos.sum{
			return it.concepto.tipo=='PERCEPCION'?it.importeGravado+it.importeExcento:0.0
		}
	}
	BigDecimal getPercepcionesGravadas() {
		conceptos.sum{
			return it.concepto.tipo=='PERCEPCION'?it.importeGravado:0.0
		}
	}
	BigDecimal getPercepcionesExcentas() {
		conceptos.sum{
			return it.concepto.tipo=='PERCEPCION'?it.importeExcento:0.0
		}
	}
	
	BigDecimal getDeducciones() {
		conceptos.sum{
			return it.concepto.tipo=='DECUCCION'?it.importeGravado+it.importeExcento:0.0
		}
	}
	BigDecimal getDeduccionesGravadas() {
		conceptos.sum{
			return it.concepto.tipo=='DEDUCCION'?it.importeGravado:0.0
		}
	}
	BigDecimal getDeduccionesExcentas() {
		conceptos.sum{
			return it.concepto.tipo=='DEDUCCION'?it.importeExcento:0.0
		}
	}
	
	def actualizar() {
		total=percepciones-deducciones
		totalGravado=percepcionesGravadas-deduccionesGravadas
		totalExcento=percepcionesGravadas-percepcionesExcentas
	}
	
	def beforeInsert= {
		antiguedadEnSemanas=getAntiguedad()
		
	}
	
}
