import java.util.Date;

import com.luxsoft.sw4.Empresa
import com.luxsoft.sw4.Direccion
import com.luxsoft.sw4.rh.*
import com.luxsoft.sw4.rh.sat.*

import org.apache.commons.lang.exception.ExceptionUtils

def cargarEmpresa(){
	def empresa=Empresa.findWhere(clave:'PAPEL')
	if(!empresa){
		empresa=new Empresa(clave:'GASOC',nombre:'OPERADORA Y ADMINISTRADOR GASOC S.A. DE C.V.',
			rfc:'OAG100209GN8',
			regimen:'REGIMEN GENERAL DE LEY PERSONAS MORALE',
            registroPatronal:'0')
		empresa.direccion=new Direccion(
			calle:'AVENIDA CUAUHTEMOC',
			numeroExterior:'1461',
			colonia:'SANTA CRUZ ATOYAC',
			municipio:'BENITO JUAREZ',
			codigoPostal:'03310',
			estado:'DISTRITO FEDERAL',
			pais:'MEXICO'
        )
		empresa.save(failOnError:true)
      
		println "Empresa inicial generada: $empresa"
	}else{
      println "Empresa $empresa ya generada .."
    }
  
	empresa.numeroDeCertificado='00001000000201478375'
	empresa.certificadoDigital=grailsApplication.mainContext
  		.getResource("/WEB-INF/data/kio/gasoc.cer").file.readBytes()
  	//empresa.certificadoDigitalPfx=grailsApplication.mainContext
  		//.getResource("/WEB-INF/data/kio/PAPEL_CFDI_CERT.pfx").file.readBytes()	
  	empresa.llavePrivada=grailsApplication.mainContext
  		.getResource("/WEB-INF/data/kio/gasoc.key").file.readBytes()	
}

def procesar(Closure task){
  def file=grailsApplication.mainContext.getResource("/WEB-INF/data/kio/empleadosKio.csv").file
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

def catalogos(){
	procesar{empleado,fields,row->
		def empresa=Empresa.first()
		def puesto=Puesto.findOrSaveWhere(clave:fields[21],descripcion:fields[21])
		def departamento=Departamento.findOrSaveWhere(clave:fields[18],descripcion:fields[18])
		def ubicacion=Ubicacion.findOrSaveWhere(clave:fields[19],descripcion:fields[19],empresa:empresa)
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
def bajas(){
	procesar {empleado,fields ,row->
		def baja=fields[8].trim()
		if(baja && empleado){
		  def bajaDate=Date.parse('dd/MM/yy',baja)
		  def bajaDeEmpleado=new BajaDeEmpleado()
		  bajaDeEmpleado.empleado=empleado
		  bajaDeEmpleado.fecha=bajaDate
		  bajaDeEmpleado.comentario='BAJA IMPORTADA'
		  bajaDeEmpleado.motivo='OTROS'
		  bajaDeEmpleado.save(failOnError:true)
		  println "Baja PROCESADA: ${empleado.curp}  baja:${baja} registro: ${row} "
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

def conceptos(){
	def file=grailsApplication.mainContext.getResource("/WEB-INF/data/conceptos.csv").file
	file.eachLine{line,row ->
		def fields=line.split(',')
		def concepto=ConceptoDeNomina.findOrCreateWhere(clave:fields[1])
		concepto.with{
			descripcion=fields[2]
			tipo=fields[0]
			claveSat=fields[3].toInteger()
			clase=fields[4]
		}
		
		concepto.save(failOnError:true)
	}
}
cargarEmpresa()
catalogos()
empleados()
bajas()
perfiles()
salarios()
securoSocial()
conceptos()
 