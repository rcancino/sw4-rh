// Groovy Code here

// Implicit variables include:
//     ctx: the Spring application context
//     grailsApplication: the Grails application
//     config: the Grails configuration
//     request: the HTTP request
//     session: the HTTP session

// Shortcuts:
//     Execute: Ctrl-Enter
//     Clear: Esc
import com.luxsoft.sw4.Empresa
import com.luxsoft.sw4.Direccion
import com.luxsoft.sw4.rh.*
import com.luxsoft.sw4.rh.sat.*
import org.apache.commons.lang.exception.ExceptionUtils

def file=grailsApplication.mainContext.getResource("/WEB-INF/data/DatosPersonalesEmpleados.csv").file
   file.eachLine{line,row ->
	 
	
	 if(row>1){
		def fields=line.split(",")
		def empleado=Empleado.findWhere(clave:fields[0])
		if(empleado){
			  try{
			  //empleado.datosPersonales.direccion=direccion
			  def direccion=new Direccion(
				calle:fields[1],
				colonia:fields[2],
				numeroExterior:'0',
				municipio:fields[3],
				codigoPostal:fields[4],
				estado:fields[5],
				pais:'MEXICO'
				
			  )
			  def estadoCivil='SOLTERO'
			  if(fields[8].startsWith("C")) estadoCivil='CASADO'
			  if(fields[8].startsWith("U")) estadoCivil='UNION_LIBRE'
			  
			  
			  def datos=new DatosPersonales(
				empleado:empleado,
				telefono1:fields[6],
				telefono2:fields[7],
				direccion:direccion,
				estadoCivil:estadoCivil
			  )
			  empleado.datosPersonales=datos
			  empleado.sexo=fields[9].startsWith("M")?'M':'F'
			  
			  
			  def valid=datos.validate()
			  if(datos.hasErrors()){
				println 'Errores de validacion: '+dato.errors
			  }
			  
			  def contacto=new EmpleadoContacto(
				nombre:fields[11],
				parentesco:fields[12],
				telefono1:fields[13],
				empleado:empleado
			  )
			  empleado.contacto=contacto
			  //datos.save(failOnError:true)
			  println 'Procesando datos personales para '+empleado+ 'Direccion: '+datos.id
			  }catch(Exception ex){
			  String msg=ExceptionUtils.getRootCauseMessage(ex)
				println "Error procesando registro ${fields} Exception: ${msg}"
			  }
		}
	}
	
  }