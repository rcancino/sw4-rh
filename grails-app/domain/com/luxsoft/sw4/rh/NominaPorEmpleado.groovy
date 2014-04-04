package com.luxsoft.sw4.rh

import com.luxsoft.sw4.cfdi.Cfdi;

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString


@EqualsAndHashCode(includes='empleado')
@ToString(includePackage=false,includeNames=true,excludes='dateCreated,lastUpdated')
class NominaPorEmpleado {

	
	Empleado empleado 
	Ubicacion ubicacion
	BigDecimal salarioDiarioBase=0.0
	BigDecimal salarioDiarioIntegrado=0.0
	BigDecimal total=0.0
	BigDecimal totalGravado=0.0
	BigDecimal totalExcento=0.0
	
	BigDecimal baseGravable=0.0
	BigDecimal subsidioEmpleoAplicado=0.0
	BigDecimal impuestoSubsidio=0.0
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
		conceptos.sum 0.0 ,{
			return it.concepto.tipo=='PERCEPCION'?it.importeGravado+it.importeExcento:0.0
		}
	}
	BigDecimal getPercepcionesGravadas() {
		conceptos.sum 0.0, {
			return it.concepto.tipo=='PERCEPCION'?it.importeGravado:0.0
		}
	}
	BigDecimal getPercepcionesExcentas() {
		conceptos.sum 0.0, {
			return it.concepto.tipo=='PERCEPCION'?it.importeExcento:0.0
		}
	}
	
	BigDecimal getDeducciones() {
		conceptos.sum 0.0,{
			return it.concepto.tipo=='DEDUCCION'?it.importeGravado+it.importeExcento:0.0
		}
	}
	BigDecimal getDeduccionesGravadas() {
		conceptos.sum 0.0,{
			return it.concepto.tipo=='DEDUCCION'?it.importeGravado:0.0
		}
	}
	BigDecimal getDeduccionesExcentas() {
		conceptos.sum 0.0,{
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
