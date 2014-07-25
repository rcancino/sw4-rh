
import com.luxsoft.sw4.rh.*
  
def nomina=Nomina.get(235)
  
def list=nomina.partidas.sort{a,b ->
	a.ubicacion.clave<=>b.ubicacion.clave?:a.empleado.apellidoPaterno<=>b.empleado.apellidoPaterno
}

list.each{
	println it.ubicacion.clave+ ' '+it.empleado
}
for(int i=0;i<list.size();i++){
	def ne=list[i]
	ne.orden=i+1
	ne.save()
}
/** Metodo 2
import com.luxsoft.sw4.rh.*

def nomina=Nomina.get(237)

def list=nomina.partidas.sort{
a,b ->
a.empleado.perfil.ubicacion.clave <=> b.empleado.perfil.ubicacion.clave?:
a.empleado.apellidoPaterno<=>b.empleado.apellidoPaterno
						   }
def orden=1
list.each{
it.orden=orden++
println "Partida ${it.empleado.perfil.ubicacion.clave} ${it.empleado} ${it.orden}"
}
**/


/** Agregar nomina por empleado
 * 

import com.luxsoft.sw4.rh.*

def nomina=Nomina.get(237)

  def empleado=Empleado.get(141)
  println 'Empleado: '+empleado

 def ne=new NominaPorEmpleado(
					empleado:empleado,
					ubicacion:empleado.perfil.ubicacion,
					antiguedadEnSemanas:0,
					nomina:nomina,
					vacaciones:0,
					fraccionDescanso:0,
					orden:0
					)
				ne.antiguedadEnSemanas=ne.getAntiguedad()
				def res=nomina.addToPartidas(ne)

def asistencia=Asistencia.find{calendarioDet==nomina.calendarioDet && empleado==ne.empleado}
ne.asistencia=asistencia
**/