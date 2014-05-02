import com.luxsoft.sw4.*
import com.luxsoft.sw4.rh.*
import org.apache.commons.lang.exception.ExceptionUtils

def generarNomina(def folio,def per,def pago,def dia){
  
	def nomina=new Nomina(
		empresa:Empresa.first(),
		folio:folio,
		periodo:per,
		diasPagados:15,
		pago:pago,
		tipo:'GENERAL',
		diaDePago:dia,
		formaDePago:'TRANSFERENCIA',
		  periodicidad:'QUINCENAL',
		total:0.0
		  ,status:'PENDIENTE'
		).save(failOnError:true)
	
}


def importar(def archivo, def folio){
	
		def nomina =Nomina.findWhere(folio:folio,periodicidad:'QUINCENAL')
  
	  println'nomina: '+ nomina.folio
	
	  File file=grailsApplication.mainContext.getResource("/WEB-INF/data/"+archivo).file
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
			ubicacion:empleado.perfil.ubicacion
			)
		  
		  println 'Importando nomina para: '+empleado
		  
		  (2..columnas.size()).each{ it-> //Agregamos las percepciones y deducciones
			def importeString=valores[it-1].trim()
			if(importeString ){
			  BigDecimal importe=importeString as BigDecimal
			  if(importe){
				def cve=columnas[it-1].trim().substring(0,4)
				def subsidioEmpleoAplicado=columnas[it-1].endsWith("A")
				 
				 if(!subsidioEmpleoAplicado){
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
				  }else{
				   nominaPorEmpleado.subsidioEmpleoAplicado=importe
				  }
				
				
			  }
			  
			}
			 
		  } //Terminan las percepciones/deducciones
		  nomina.addToPartidas(nominaPorEmpleado)
		}
		
		row++
		//nomina.addToPartidas(nominaPorEmpleado)
	  }
	nomina.save(failOnError:true)
	  
  }
  

  
  def actualizarNomina(def folio){
	  def nomina =Nomina.findWhere(folio:folio,periodicidad:'QUINCENAL')
	  nomina.partidas.each{ ne->
		
		def percepcionesG=ne.conceptos.sum 0.0,{ it.concepto.tipo=='PERCEPCION'?it.importeGravado:0.0}
		def percepcionesE=ne.conceptos.sum 0.0,{ it.concepto.tipo=='PERCEPCION'?it.importeExcento:0.0}
		
		def deduccionG=ne.conceptos.sum 0.0,{ it.concepto.tipo=='DEDUCCION'?it.importeGravado:0.0}
		def deduccionE=ne.conceptos.sum 0.0,{ it.concepto.tipo=='DEDUCCION'?it.importeExcento:0.0}
		
		ne.totalGravado= percepcionesG-deduccionG
		ne.totalExcento=percepcionesE-deduccionE
		
		ne.total=ne.totalGravado+ne.totalExcento
		
	  }
	  nomina.total=nomina.partidas.sum{it.total}
  }


 def timbrar(int folio){
	   def cfdiService=ctx.getBean('cfdiService')
	   cfdiService.cfdiTimbrador.timbradoDePrueba=false
	   
	def nomina =Nomina.findWhere(folio:folio,periodicidad:'QUINCENAL')
	
	for(NominaPorEmpleado ne:nomina.partidas){
		try{
		  if(ne.cfdi==null){
			println 'Timbrando Ne id:'+ne.id
			def res=cfdiService.generarComprobante(ne.id)
			println 'CFDI: '+res
		  }
			  
		}catch(Exception ex){
			println 'Error timbrando '+ExceptionUtils.getRootCauseMessage(ex)
			
		}
	}
}



def folio=7

//generarNomina(folio,new Periodo('01/04/2014','15/04/2014'),Date.parse('dd/MM/yyyy','14/04/2014'),'LUNES')
//importar("nomina_q${folio}.csv",folio)
//actualizarNomina(folio)
timbrar(folio)