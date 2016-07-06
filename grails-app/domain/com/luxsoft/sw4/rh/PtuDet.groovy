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
	CalendarioDet calendarioDet

	Periodo periodo

	Date dateCreated
	Date lastUpdated

	//
	Long faltas=0
	Long incapacidades=0
	Long permisosP=0	
	
	
	Long diasPtu=0
	BigDecimal montoDias=0.0
	BigDecimal montoSalario=0.0
	BigDecimal montoPtu=0.0

	BigDecimal ptuExcento=0.0
	BigDecimal ptuGravado=0.0

	BigDecimal salarioDiario=0.0
	BigDecimal salarioMensual=0.0
	BigDecimal incentivo=0.0
	BigDecimal totalMensualGravado=0.0

	//Impuestos del Total mensual gravado
	BigDecimal tmgIsr=0.0
	BigDecimal tmgSubsidio=0.0
	BigDecimal tmgResultado=0.0

	// Impuestos de salarioMensual+incentivo
	BigDecimal smiIsr=0.0 
	BigDecimal smiSubsidio=0.0
	BigDecimal smiResultado=0.0

	BigDecimal isrPorRetener=0.0

	BigDecimal porPagarBruto=0.0

	BigDecimal isrAcreditable=0.0

	BigDecimal pensionA=0.0
	BigDecimal otrasDed=0.0
	BigDecimal prestamo=0.0

	BigDecimal porPagarNeto=0.0

	Integer tipo
	Date alta
	Date baja
	String status


    static constraints = {
		nominaPorEmpleado nullable:true
		calendarioDet nullable:true
		noAsignadoComentario nullable:true,maxSize:100
		noAsignadoComentario nullable:true
		montoDias scale:6
		montoSalario scale:6
		montoPtu sacale:6
		tipo nullable:true
		alta nullable:true
		baja nullable:true
		status nullable:true, maxSize:10
    }

    static mappings = {
    	alta type:'date'
    	baja type:'date'
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
			periodo=Periodo.getPeriodoAnual(ptu.ejercicio)
		return periodo
	}

	def getSalarioNeto(){
		return (salario+vacaciones)-retardos
	}

	//def getMontoPtu(){
	//	return montoDias+montoSalario
	//}    


}
