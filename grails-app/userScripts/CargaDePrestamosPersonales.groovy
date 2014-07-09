import com.luxsoft.sw4.rh.acu.*
import com.luxsoft.sw4.rh.*
  
  def acu=AcumuladoPorConcepto.list()
  def concepto=ConceptoDeNomina.findByClave('P024')
  println concepto
 def file=grailsApplication.mainContext.getResource("/WEB-INF/data/PrestamosPersonales.csv").file
 file.eachLine{line,row ->
	if(row>1){
		def fields=line.split(",")
		def nombres=fields[1].split(" ")
		def empleado=Empleado.findByApellidoPaternoAndApellidoMaterno(nombres[0],nombres[1])
		  def importe=fields[3] as BigDecimal
	  if(importe>0){
		println 'Prestamo :'+empleado+ ' Saldo:' +importe
		new Prestamo(
		  empleado:empleado,
		  tipo:'DESCUENTO_POR_NOMINA',
		  autorizo:'PENDIENTE',
		  alta:new Date(),
		  fechaDeAutorizacion:new Date(),
		  comentario:'Datos de prestamo importado',
		  importe:importe,
		  saldo:0.0,
		  totalAbonos:0.0).save(failOnError:true)
		
	  }
		  
	}
  }
