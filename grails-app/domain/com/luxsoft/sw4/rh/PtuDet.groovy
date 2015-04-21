package com.luxsoft.sw4.rh


import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import com.luxsoft.sw4.Periodo

@EqualsAndHashCode(includes='empleado,total')
@ToString(includePackage=false,includeNames=true,excludes='dateCreated,lastUpdated')
class PtuDet {

	Empleado empleado
	BigDecimal salario =0.0//Percepcion
	BigDecimal vacaciones=0.0 //Pecepcion
	BigDecimal comisiones=0.0 //Percepcion
	BigDecimal retardos=0.0  //Deduccion
	BigDecimal total=0.0
	BigDecimal topeAnual=0.0
	
	Integer antiguedad=0
	Integer diasDelEjercicio=0

	Boolean noAsignado
	String noAsignadoComentario
	Boolean noEntregar=false

	BigDecimal salarioNeto

	NominaPorEmpleado nominaPorEmpleado

	Periodo periodo

	Date dateCreated
	Date lastUpdated

	//
	Long faltas=0
	Long incapacidades=0
	Long permisosP=0	
	

    static constraints = {
		nominaPorEmpleado nullable:true
		noAsignadoComentario nullable:true,maxSize:100
		noAsignadoComentario nullable:true
    }

    static transients = ['antiguedad','salarioNeto','periodo']

    static belongsTo = [ptu: Ptu]

	public Integer getAntiguedad(){

    	if(!antiguedad && empleado){
			
			def fecha=getPeriodo().fechaFinal
			if(empleado.baja && (empleado.alta<empleado.baja.fecha)){
				if(getPeriodo().fechaFinal>empleado.baja.fecha)
					fecha=empleado.baja.fecha
			}
			return (fecha-empleado.alta)+1
    	}
    	return antiguedad
		
	}

	Periodo getPeriodo(){
		if(!periodo)
			periodo=Periodo.getPeriodoAnual(this.ejercicio)
		return periodo
	}

	def getSalarioNeto(){
		return (salario+vacaciones)-retardos
	}    


}
