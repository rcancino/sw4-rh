import com.luxsoft.sw4.*
import com.luxsoft.sw4.rh.*
import org.apache.commons.lang.exception.ExceptionUtils

def generarNomina(def folio,def per,def pago,def dia){
  
	def nomina=new Nomina(
		empresa:Empresa.first(),
		folio:folio,
		periodo:per,
		diasPagados:5,
		pago:pago,
		tipo:'GENERAL',
		diaDePago:dia,
		formaDePago:'TRANSFERENCIA',
		  periodicidad:'SEMANAL',
		total:0.0
		).save(failOnError:true)
	
}


def importar(def archivo, def folio){
	
		def nomina =Nomina.findWhere(folio:folio,periodicidad:'SEMANAL')
  
	  println'nomina: '+ nomina.folio
	
	  File file=grailsApplication.mainContext.getResource("/WEB-INF/data/kio/"+archivo).file
		def row=0
		 def columnas
		file.eachLine { line ->
	   
		if(row==0){
		  columnas=line.split(",")
		  println 'Columnas agregadas: '+ columnas.size()
		}
		if(row>0){
		  
		  def valores=line.split(",")
		 
		  def empleado=Empleado.findWhere(clave:valores[0])
		  
			  
		  def ubicacion=Ubicacion.findWhere(clave:valores[1])
		  assert ubicacion,'No existe la ubicacion: '+valores[1]
		  assert empleado,"No existe el empleado: "+valores[0]
		  //Generando nomina porempleado
		  def nominaPorEmpleado=new NominaPorEmpleado(
			empleado:empleado,
			salarioDiarioBase:empleado.salario.salarioDiario,
			salarioDiarioIntegrado:empleado.salario.salarioDiarioIntegrado,
			totalGravado:0.0,
			totalExcento:0.0,
			total:0.0,
			comentario:'IMPORTACION INICIAL',
			antiguedadEnSemanas:0,
			baseGravable:0.0,
			impuestoSubsidio:0.0,
			subsidioEmpleoAplicado:0.0,
			ubicacion:ubicacion
			)
		  
		  println 'Importando nomina para: '+empleado
		  
		  (3..columnas.size()).each{ it-> //Agregamos las percepciones y deducciones
			def col=columnas[it-1]
			def importeString=valores[it-1].trim()
		   
		   
			
			if(importeString ){
			   
			  BigDecimal importe=importeString as BigDecimal
			  if(importe){
				
				def subsidioEmpleoAplicado=columnas[it-1].endsWith("A")
				
				if(subsidioEmpleoAplicado){
				  nominaPorEmpleado.subsidioEmpleoAplicado=importe
				}else{
				  
				  def cve=columnas[it-1].trim().substring(0,4)
				  
				  if(col=='P021'){
					  println 'Procesando: '+col+"  :"+importeString
					cve='D013'
					if(importe<0){
					  cve='P021'
					  importe=importe.abs()
					}
				  }
				  def excento=columnas[it-1].contains("E")
				  def concepto=ConceptoDeNomina.findWhere(clave:cve)
				  def nominaEmpDet=new NominaPorEmpleadoDet(
						concepto:concepto,
						comentario:'IMPORTACION INICIAL',
						importeGravado:!excento?importe:0.0,
							importeExcento:excento?importe:0.0
					)
				   nominaPorEmpleado.addToConceptos(nominaEmpDet)
				   println " Agregando ${nominaEmpDet.concepto}   para: "+it +' '+cve+ " Importe: "+importe
					
				}
			 }
			  
			}
			
			 
		  } //Termina procesar columnas
			
		  nomina.addToPartidas(nominaPorEmpleado)
		}
		
		row++
		
	  }
	nomina.save(failOnError:true)
	  
  }
  

  
  def actualizarNomina(def folio){
	  
	  def nomina =Nomina.findWhere(folio:folio,periodicidad:'SEMANAL')
	  nomina.partidas.each{ ne->
		
		def percepcionesG=ne.conceptos.sum 0.0,{ it.concepto.tipo=='PERCEPCION'?it.importeGravado:0.0}
		def percepcionesE=ne.conceptos.sum 0.0,{ it.concepto.tipo=='PERCEPCION'?it.importeExcento:0.0}
		
		def deduccionG=ne.conceptos.sum 0.0,{
		  it.concepto.tipo=='DEDUCCION'?it.importeGravado:0.0
		}
		def deduccionE=ne.conceptos.sum 0.0,{ it.concepto.tipo=='DEDUCCION'?it.importeExcento:0.0}
		//println percepcionesG-deduccionG
		//println percepcionesE-deduccionE
		ne.totalGravado= percepcionesG-deduccionG
		ne.totalExcento=percepcionesE-deduccionE
		
		ne.total=ne.totalGravado+ne.totalExcento
		
	  }
	  nomina.total=nomina.partidas.sum{it.total}
  }







//generarNomina(13,new Periodo('24/03/2014','30/03/2014'),Date.parse('dd/MM/yyyy','29/03/2014'),'JUEVES')
//importar("nomina_s13.csv",13)
actualizarNomina(13)