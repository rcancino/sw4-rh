import java.util.Date;

import com.luxsoft.sw4.Empresa
import com.luxsoft.sw4.Direccion
import com.luxsoft.sw4.rh.*
import com.luxsoft.sw4.rh.sat.*

import org.apache.commons.lang.exception.ExceptionUtils


def procesar(Closure task){
  def file=grailsApplication.mainContext.getResource("/WEB-INF/data/empleados3.csv").file
  file.eachLine{line,row ->
	if(row>1){
		def fields=line.split(",")
		def curp=fields[9]
		def empleado=Empleado.findWhere(curp:curp)
		  //if(empleado){
		  try{
			  task empleado,fields,row
		  }catch(Exception ex){
			  String msg=ExceptionUtils.getRootCauseMessage(ex)
			println "Error procesando registro ${fields} Exception: ${msg}"
		  }
		  //}
		  
	}
  }
}


def empleados(){
  procesar{empleado,fields,row ->
		if(!empleado){
		  def curp=fields[9]
			 empleado=new Empleado(
				  curp:curp,
				  rfc:fields[10],
				  apellidoPaterno:fields[2]?:'',
				  apellidoMaterno:fields[3]?:'',
				  nombres:fields[4],
				  status:fields[6].toUpperCase(),
				  alta:Date.parse("dd/MM/yy", fields[7]?.trim()),
				  sexo:'M',
				  fechaDeNacimiento:Date.parse("dd/MM/yy", fields[17]?.trim()),
				  clave:fields[1]
				  )
			  empleado.save(failOnError:true)
		  }
	}
}


def perfiles(){
	def empresa=Empresa.first()
	  def riesgoPuesto=SatRiesgoPuesto.findWhere(clave:2)
	  def regimenContratacion=SatRegimenContratacion.findWhere(clave:2)
	procesar{empleado,fields,row->
	  if(empleado){
		def perfil=new PerfilDeEmpleado()
		perfil.empresa=empresa
		perfil.tipo=fields[16].toUpperCase()
		perfil.numeroDeTrabajador=fields[0]
		perfil.puesto=Puesto.findWhere(clave:fields[21])
		  perfil.departamento=Departamento.findWhere(clave:fields[18])
		  perfil.ubicacion=Ubicacion.findWhere(clave:fields[19])
		perfil.tipoDeContrato='BASE'
		perfil.jornada=fields[22]=='MEDIA'?'MEDIA':'COMPLETA'
		perfil.riesgoPuesto=riesgoPuesto
		perfil.regimenContratacion=regimenContratacion
		empleado.perfil=perfil
		empleado.save(failOnError:true)
		perfil.validate()
		if(perfil.hasErrors())
			  println 'Error en perfil:' +perfil.errors
		
			  
	  }
		
	}
}

def salarios(){
	procesar{empleado,fields,row ->
		if(empleado){
			empleado.salario=new Salario(
				salarioDiario:fields[12].toBigDecimal(),
				salarioDiarioIntegrado:fields[13].toBigDecimal(),
				formaDePago:'TRANSFERENCIA',
				clabe:fields[14],
				periodicidad:fields[11]?.startsWith("S")?'SEMANAL':'QUINCENAL'
			)
			empleado.save(failOnError:true)
		}
	}
}

def securoSocial(){
	procesar{empleado,fields,row ->
		if(empleado){
			empleado.seguridadSocial=new SeguridadSocial(
				numero:fields[15],
				alta:empleado.alta,
				turno:'MATUTINO',
				comentario:'SE DEBE AJUSTAR EL ALTA EN EL IMSS'
				
			)
			empleado.save(failOnError:true)
		}
	}
}



empleados()
perfiles()
salarios()
securoSocial()

 