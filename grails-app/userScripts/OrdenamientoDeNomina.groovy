
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