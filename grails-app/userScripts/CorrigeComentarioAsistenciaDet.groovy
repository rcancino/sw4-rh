import com.luxsoft.sw4.rh.*
  
  def rows=AsistenciaDet.where{
    tipo=='INCAPACIDAD'
  }

rows.each{

  def found=Incapacidad.find("from Incapacidad i where i.empleado=? and ? between date(i.fechaInicial) and date(i.fechaFinal)"
				,[it.asistencia.empleado,it.fecha])
  if(found){
      println 'Ajustando: '+it.id+ ' con Incapacidad: '+found
    if(found.tipo){
					
		switch(found.tipo.clave) {
						case 2:
							it.comentario+= ' EG'
							break
						case 3:
							it.comentario+= ' MAT'
							break
						case 1:
							if(found?.comentario?.toUpperCase().startsWith("TRAYECTO")){
								it.comentario+=' RTT'
							}else{
								it.comentario+=' RTE'
							}
		}
	}
    it.save flush:true
    
  }
}
  