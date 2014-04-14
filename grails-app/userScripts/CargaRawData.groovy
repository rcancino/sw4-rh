

import com.luxsoft.sw4.rh.*

def file=new File("C://basura/nomina/20140409.chk")
int lector
def fecha
file.eachLine{line,row ->
  def fields=line.split(',')
  
  if(fields.length==2){
	lector=fields[0].toInteger()
	println 'Lector: '+lector
  }else if(fields.length==4){
	fecha=Date.parse('yyyyMMdd',fields[2])
	  println 'Fecha: '+fecha
  }else{
	//
	def hora=Date.parse('hhmmss',fields[0])
	println "registrando evento empleado: ${fields[2]} hora:$hora  Fecha:$fecha"
	def r=new Checado(lector:lector,fecha:fecha,hora:hora,numeroDeEmpleado:fields[2])
	r.save(failOnError:true)
  }
 
  
}
