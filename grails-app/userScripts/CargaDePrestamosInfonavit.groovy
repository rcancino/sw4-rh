import com.luxsoft.sw4.rh.acu.*
import com.luxsoft.sw4.rh.*
  
  
 def file=grailsApplication.mainContext.getResource("/WEB-INF/data/PrestamosINFONAVIT.csv").file
 file.eachLine{line,row ->
	if(row>1){
		def fields=line.split(",")
		def clave=fields[1].split(" ")
		def empleado=Empleado.findByClave(clave)
		def tipo=fields[2]
		def importe=fields[3] as BigDecimal
		def numeroDeCredito=fields[4]
	  if(importe>0){
		println 'Infonavit :'+empleado+ ' Cuota:' +importe
		new Infonavit(
		  empleado:empleado,
		  tipo:tipo,
		  numeroDeCredito:numeroDeCredito,
		  comentario:'IMPORTADO',
		  cuotaFija:importe
		  ).save(failOnError:true)
		
	  }
		  
	}
  }
