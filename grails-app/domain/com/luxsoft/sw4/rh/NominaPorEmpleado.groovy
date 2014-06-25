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
	List conceptos=[]
	Cfdi cfdi
	
	
	/* Tiempo extra ?? */
	String comentario
	
	Integer antiguedadEnSemanas
	
	Integer diasDelPeriodo=0
	
	Integer faltas=0
	
	double fraccionDescanso=0.0
	
	Integer vacaciones=0
	
	Integer incapacidades=0
	
	BigDecimal diasTrabajados=0.0
	
	Asistencia asistencia

	Date dateCreated
	
	Date lastUpdated

    static constraints = {
    	comentario nullable:true,maxSize:200
		antiguedadEnSemanas nullable:false,minSize:1
		cfdi nullable:true 
		faltas nullable:true
		incapacidades nullable:true
		asistencia nullable:true
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
	
	static mapping = {
		conceptos cascade: "all-delete-orphan"
	}
	
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

	/*
	@Override
	public int compareTo(NominaPorEmpleado o) {
		String a1=empleado.apellidoPaterno?:''
		String a2=o.empleado.apellidoPaterno?:''
		return a1.compareTo(a2)
	}*/
	
	/*
	def beforeInsert= {
		antiguedadEnSemanas=getAntiguedad()
		
	}*/
	
}
