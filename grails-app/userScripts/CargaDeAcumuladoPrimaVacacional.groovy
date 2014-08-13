import com.luxsoft.sw4.rh.acu.*
import com.luxsoft.sw4.rh.*
  
  def acu=AcumuladoPorConcepto.list()
  def concepto=ConceptoDeNomina.findByClave('P024')
  println concepto
 def file=grailsApplication.mainContext.getResource("/WEB-INF/data/PRIMAVACEXENTAAGO2014.csv").file
 file.eachLine{line,row ->
	if(row>1){
		def fields=line.split(",")
		def clave=fields[0]
		def empleado=Empleado.findByClave(clave)
		def excento=fields[2] as BigDecimal
		println 'Empleado: '+empleado +'Acu: '+excento
		new AcumuladoPorConcepto(empleado:empleado,concepto:concepto,ejercicio:2014,acumuladoExcento:excento,acumuladoGravado:0.0).save()
	  
		  
	}
  }
