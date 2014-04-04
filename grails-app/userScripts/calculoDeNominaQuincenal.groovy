//Alta de nomina

import com.luxsoft.sw4.*
import com.luxsoft.sw4.rh.*
import org.apache.commons.lang.exception.*


/** Siguiente nomina Quincenal
def nomina=new Nomina(folio:7,periodo:new Periodo('01/04/2014','15/04/2014'),
						pago:Date.parse('dd/MM/yyyy','14/04/2014'),
						diasPagados:15,tipo:'GENERAL',periodicidad:'QUINCENAL',
						formaDePago:'TRANSFERENCIA',diaDePago:'JUEVES',
						total:0.0,
						empresa:Empresa.first()).save(failOnError:true)
**/
//Procesar las percepciones generales
def nomina=Nomina.where{folio==7 && tipo=='GENERAL' && periodicidad=='QUINCENAL'}.find()

def percepciones=ConceptoDeNomina.where{tipo=='PERCEPCION' && general==true}.findAll()

percepciones.each{ per ->
  //Iteramos sobre todos los empleados de la nomina
  nomina.partidas.each{ ne->
	
	// Buscamos el concepto
	NominaPorEmpleadoDet det=ne.conceptos.find{ it.concepto.id=per.id}
	//Si no la encuentra lo agregamos la percepcion
	if(!det){
	  det=new NominaPorEmpleadoDet(concepto:per,importeGravado:0.0,importeExcento:0.0,comentario:'PRUEBAS')
	  ne.addToConceptos(det)
	}else{ println 'Ya existe el registro...'}
	//Si es salario lo calculamos
	if(per.clave=='P001'){
	  println 'Procesando P001'
	  formula(ne.empleado,ne,det)
	}
	
	
  }
  nomina.save(failOnError:true)
}

//Formula para el calculo del concepto Salario
def formula( empleado,nominaEmpleado,nominaEmpleadoDet) {
  def salarioDiario=empleado.salario.salarioDiario
  def diasTrabajados=nominaEmpleado.nomina.getDiasPagados()
  def importeGravado=salarioDiario*diasTrabajados
  nominaEmpleadoDet.importeGravado=importeGravado
}

