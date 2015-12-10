package com.luxsoft.sw4.rh


import com.luxsoft.sw4.Empresa
import grails.plugin.springsecurity.annotation.Secured;
import grails.transaction.Transactional
import grails.validation.Validateable
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Map;
import org.apache.commons.lang.StringUtils
import java.io.BufferedWriter
import com.luxsoft.sw4.*
import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef
import org.apache.commons.lang.WordUtils

@Secured(['ROLE_ADMIN','RH_USER'])
@Transactional(readOnly = true)
class ExportadorController {

	def jasperService
    def index() { }


    def nominaBanamex(){
    	
    }

    def generarNominaBanamex(Nomina nomina){
    	def temp = File.createTempFile('temp', '.txt') 
		
		temp.with{
			Empresa emp=Empresa.first()
			Nomina n=nomina
			 def totalNomina=0
			 n.partidas.each{
				 totalNomina=totalNomina+it.total
				
			 }
			 

			SimpleDateFormat df = new SimpleDateFormat("ddMMyy")

			// Creacion del registro de control
			def registroControl

			def tipoRegistro="1"
			def numIdentCte="000005139464"
			def fechaPago= df.format(n.getPago()+1)
			def secuenciaArch="0001"
			def nombreEmpresa=emp.getNombre().padRight(36)
			def descripcion=StringUtils.rightPad("Pago de nomina",20)
			def naturaleza ="05"
			def instrucciones=StringUtils.leftPad("",40)
			def vers="B"
			def volumen="0"
			def caracteristicas="0"

			registroControl= tipoRegistro+numIdentCte+fechaPago+secuenciaArch+nombreEmpresa+descripcion+naturaleza+instrucciones+vers+volumen+caracteristicas
			append(registroControl+"\r\n")

			// Creacion del registro global

			def registroGlobal

			def tipoReg="2"
			def tipoOp="1"
			def claveMoneda="001"
			def formato = new DecimalFormat("###")
			int importe	= totalNomina //Math.floor(n.total)


			def importeCargos =formato.format(importe).padLeft(16,"0")
			def formatoDec = new DecimalFormat(".##")
			def decimalCargo=formatoDec.format(totalNomina-importe).replace('.','').padRight(2,"0")

			def importeCargo=importeCargos+decimalCargo
			def tipoCta="01"
			def claveSuc="0270"
			def numCta="00000000000001858193"
			def blanco=StringUtils.leftPad("",20)

			registroGlobal=tipoReg+tipoOp+claveMoneda+importeCargo+tipoCta+claveSuc+numCta+blanco
			append(registroGlobal+"\n")

			def numAbonos=0
			n.partidas.sort{it.empleado.apellidoPaterno}.each{

				// Creacion de registro abono individual
			
				def registroIndividual
				def tipoRegInd="3"
				def tipoOpInd="0"
				int importeInd=Math.floor(it.total)
				def importeAbonos =formato.format(importeInd).padLeft(16,"0");
				def decimalAbono=formatoDec.format(it.total-importeInd).replace('.','').padRight(2,"0")
				def importeAbono= importeAbonos+""+decimalAbono
				def tipoCtaInd="03"
				if(it.empleado.id==280 || it.empleado.id==260 ||  it.empleado.id==245)
				 	tipoCtaInd="01"
				def claveSucInd="0270"
				
				if(it.empleado.id==260 )
					claveSucInd="0269"
				
				if(it.empleado.id==245 )
					claveSucInd="0515"

				if(it.empleado.id==246 ){
					claveSucInd="0269"
					tipoCtaInd="01"
				}
				
				def numCtaInd=it.empleado.salario.clabe.padLeft(20,"0")
				def referencia="0000000001".padRight(40)
				def beneficiario=it.empleado.nombre.replace("Ñ","N").padRight(55)
				def blanco1=StringUtils.leftPad("",40)
				def blanco2=StringUtils.leftPad("",24)
				def ultimo="0000000000"

				registroIndividual=tipoRegInd+tipoOpInd+claveMoneda+importeAbono+tipoCtaInd+claveSucInd+numCtaInd+referencia+beneficiario+blanco1+blanco2+ultimo
				append(registroIndividual+"\n")
				numAbonos=numAbonos+1
			}

			// Creacion de registro totales

			def registroTotales
			def tipoRegTotal="4"
			def abonos=numAbonos.toString().padLeft(6,"0")
			def numCargos="000001"

			registroTotales=tipoRegTotal+claveMoneda+abonos+importeCargo+numCargos+importeCargo
			append(registroTotales+"\n")


			println absolutePath
		}
		
	  
		String name="NominaBanamex_"+"$nomina.ejercicio"+"_$nomina.tipo"+"_$nomina.periodicidad"+"_$nomina.folio"+".txt"
		response.setContentType("application/octet-stream")
		response.setHeader("Content-disposition", "attachment; filename=\"$name\"")
		response.outputStream << temp.newInputStream()	
    	
    }
	
/** Layouts Imss */	
	def modificacionesImss(){
		[reportCommand:new EjercicioBimestreCommand()]
	}
	
	def generarModificacionesImss(EjercicioBimestreCommand command){
		File temp = File.createTempFile("temp",".txt")
		
		temp.with {

		  Empresa emp=Empresa.first()
		def registroPatronal=emp.registroPatronal
		def numeroDeMovs=0
		SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy")
		
		def ejercicio=command.ejercicio
		def bimestre=command.bimestre
		def fecha_aplic
		
				switch(bimestre)
				{
			  
				case 1:
						fecha_aplic="0103"+ejercicio
				 break;
				case 2:
						fecha_aplic="0105"+ejercicio
				 break;
				case 3:
						fecha_aplic="0107"+ejercicio
				 break;
				case 4:
						fecha_aplic="0109"+ejercicio
				 break;
				case 5:
						fecha_aplic="0111"+ejercicio
				 break;
				case 6:
						fecha_aplic="0101"+(ejercicio+1)
				 break;
				   
				  }
		
		
		
		def calculosSdi=CalculoSdi.findAllByEjercicioAndBimestre(command.ejercicio,command.bimestre).each{calculo ->
		 
		  
		  if(calculo.sdiInf!=0.0  && calculo.tipo== 'CALCULO_SDI' ){
			  
		  
			numeroDeMovs=numeroDeMovs+1
			
		  def numeroSeguridadSocial= SeguridadSocial.findByEmpleado(calculo.empleado).numero.replace('-','')
		  def apellidoPaterno=calculo.empleado.apellidoPaterno ? calculo.empleado.apellidoPaterno.replace('\u00d1','N').padRight(27) : calculo.empleado.apellidoMaterno.replace('\u00d1','N').padRight(27)
		  def apellidoMaterno=calculo.empleado.apellidoPaterno ? calculo.empleado.apellidoMaterno.replace('\u00d1','N').padRight(27) : StringUtils.leftPad("",27)
		  def nombres= calculo.empleado.nombres ? calculo.empleado.nombres.replace('\u00d1','N').padRight(27) : StringUtils.leftPad("",27)
		  def salarioBase=calculo.sdiInf.toString().replace('.','').padLeft(6,"0")
		  def filler= StringUtils.leftPad("",6)
		  def tipoTrabajador="1"
		  def tipoSalario="2"
			if(calculo.empleado.id==273 || calculo.empleado.id==274)
			tipoSalario=1
		  def tipoJornada=0
			if(calculo.empleado.perfil.jornada=="MEDIA" || calculo.empleado.perfil.jornada=="REDUCIDA")
			 tipoJornada=6
		  def fechaMov=fecha_aplic
		  
		  
		  def unidadMedica=   StringUtils.leftPad("",3)   //   calculo.empleado.seguridadSocial.unidadMedica? calculo.empleado.seguridadSocial.unidadMedica.padLeft(3,"0") :"000"
		  def filler2=StringUtils.leftPad("",2)
		  def tipoMov="07"
		  def guia="11400"
		  def claveTrab=StringUtils.leftPad("",10)
		  
		  def filler3=StringUtils.leftPad("",1)
		  def curp=StringUtils.leftPad("",18)  //calculo.empleado.curp?:calculo.empleado.rfc.padLeft(18," ")
		  def identificador="9"
		  
			  def registro= registroPatronal+numeroSeguridadSocial+apellidoPaterno+apellidoMaterno+nombres+salarioBase+filler+tipoTrabajador+tipoSalario+tipoJornada+fechaMov+unidadMedica+filler2+tipoMov+guia+claveTrab+filler3+curp+identificador
			  append(registro+"\r\n","UTF8")
			  
		   
		  }		 
		}	
	  }	
			String name="ModificacionesIMSS_"+new Date().format("dd_MM_yyyy")+".txt"
		response.setContentType("application/octet-stream")
		response.setHeader("Content-disposition", "attachment; filename=\"$name\"")
		response.outputStream << temp.newInputStream()
	}
	
	
	def altasImss(){
		[reportCommand:new PeriodoCommand()]
}

def generarAltasImss(PeriodoCommand command){
	
	def temp = File.createTempFile('temp', '.txt')
	
	temp.with {

    Empresa emp=Empresa.first()
	def registroPatronal=emp.registroPatronal
	def numeroDeMovs=0
	Date fechaIni=command.fechaInicial
	Date fechaFin=command.fechaFinal
	def modificaciones=ModificacionSalarial.findAll("from ModificacionSalarial m where m.tipo='ALTA' and m.fecha between ? and ?",[fechaIni,fechaFin]).each{calculo ->
	SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy")
		  
		numeroDeMovs=numeroDeMovs+1
		
	  def numeroSeguridadSocial= SeguridadSocial.findByEmpleado(calculo.empleado).numero.replace('-','')
	  def apellidoPaterno=calculo.empleado.apellidoPaterno ? calculo.empleado.apellidoPaterno.replace('\u00d1','N').padRight(27) : calculo.empleado.apellidoMaterno.replace('\u00d1','N').padRight(27)
	  def apellidoMaterno=calculo.empleado.apellidoPaterno ? calculo.empleado.apellidoMaterno.replace('\u00d1','N').padRight(27) : StringUtils.leftPad("",27)
	   def nombres= calculo.empleado.nombres ? calculo.empleado.nombres.replace('\u00d1','N').padRight(27) : StringUtils.leftPad("",27)
	  def salarioBase= calculo.sdiNuevo.toString().replace('.','').padLeft(6,"0")
	  def filler= StringUtils.leftPad("",6)
	  def tipoTrabajador="1"
	  def tipoSalario="0"
	  
	  def tipoJornada="0"
		if(calculo.empleado.perfil.jornada=="MEDIA" || calculo.empleado.perfil.jornada=="REDUCIDA"){
		 tipoSalario=2
		 tipoJornada=6
		}
	  def fechaMov=df.format(calculo.fecha)
	  def unidadMedica= StringUtils.leftPad("",3) 
	  def filler2=StringUtils.leftPad("",2)
	  def tipoMov="08"
	  def guia="11400"
	  def claveTrab= StringUtils.leftPad("",10)  
		 def filler3=StringUtils.leftPad("",1)
	  def curp= StringUtils.leftPad("",18) 
	  def identificador="9"
	
	   def registro=registroPatronal+numeroSeguridadSocial+apellidoPaterno+apellidoMaterno+nombres+salarioBase+filler+tipoTrabajador+tipoSalario+tipoJornada+fechaMov+unidadMedica+filler2+tipoMov+guia+claveTrab+filler3+curp+identificador
	   append(registro+"\r\n","UTF8")
	  
	  
	}	
	
  }
	String name="AltasIMSS_"+new Date().format("dd_MM_yyyy")+".txt"
	response.setContentType("application/octet-stream")
	response.setHeader("Content-disposition", "attachment; filename=\"$name\"")
	response.outputStream << temp.newInputStream()
}

def modificacionIndividualImss(){
	[reportCommand:new PeriodoCommand()]
}

def generarModificacionIndividualImss(PeriodoCommand command){
	
	def temp = File.createTempFile('temp', '.txt')
	SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy")
	temp.with {
	 Date fechaIni=command.fechaInicial
	 Date fechaFin=command.fechaFinal
	 Empresa emp=Empresa.first()
	 def registroPatronal=emp.registroPatronal
	 def numeroDeMovs=0
	 
	 
	 def calculosSdi=ModificacionSalarial.findAll("from ModificacionSalarial m where m.fecha between ? and  ? and tipo=\'AUMENTO\'",[fechaIni,fechaFin]).each{calculo ->
	   if(calculo.calculoSdi.sdiInf!=0.0){
	
		   numeroDeMovs=numeroDeMovs+1
		   
	   def numeroSeguridadSocial= SeguridadSocial.findByEmpleado(calculo.calculoSdi.empleado).numero.replace('-','')
	   def apellidoPaterno=calculo.calculoSdi.empleado.apellidoPaterno ? calculo.calculoSdi.empleado.apellidoPaterno.replace('\u00d1','N').padRight(27) : calculo.calculoSdi.empleado.apellidoMaterno.replace('\u00d1','N').padRight(27)
	   //def apellidoPaterno=calculo.calculoSdi.empleado.apellidoPaterno.replace(\u00d1,'N').padRight(27) 
	   
	   def apellidoMaterno=calculo.calculoSdi.empleado.apellidoPaterno ? calculo.calculoSdi.empleado.apellidoMaterno.replace('\u00d1','N').padRight(27) : StringUtils.leftPad("",27)
	   def nombres= calculo.calculoSdi.empleado.nombres ? calculo.calculoSdi.empleado.nombres.replace('\u00d1','N').padRight(27) : StringUtils.leftPad("",27)
	   def salarioBase=calculo.calculoSdi.sdiInf.toString().replace('.','').padLeft(6,"0")
	   def filler= StringUtils.leftPad("",6)
	   def tipoTrabajador="1"
	   def tipoSalario="2"
		 if(calculo.calculoSdi.empleado.id==273 || calculo.calculoSdi.empleado.id==274)
		 tipoSalario=1
	   def tipoJornada=0
		 if(calculo.calculoSdi.empleado.perfil.jornada=="MEDIA" || calculo.calculoSdi.empleado.perfil.jornada=="REDUCIDA")
		  tipoJornada=6
	   def fechaMov=df.format(calculo.fecha) 
	   def unidadMedica=   StringUtils.leftPad("",3)   //   calculo.empleado.seguridadSocial.unidadMedica? calculo.empleado.seguridadSocial.unidadMedica.padLeft(3,"0") :"000"
	   def filler2=StringUtils.leftPad("",2)
	   def tipoMov="07"
	   def guia="11400"
	   def claveTrab=StringUtils.leftPad("",10)
	   def filler3=StringUtils.leftPad("",1)
	   def curp=StringUtils.leftPad("",18)  //calculo.empleado.curp?:calculo.empleado.rfc.padLeft(18," ")
	   def identificador="9"

		def registro= registroPatronal+numeroSeguridadSocial+apellidoPaterno+apellidoMaterno+nombres+salarioBase+filler+tipoTrabajador+tipoSalario+tipoJornada+fechaMov+unidadMedica+filler2+tipoMov+guia+claveTrab+filler3+curp+identificador
		append(registro+"\r\n")
	   }
	
  }
	String name="ModificacionSalarialIMSS_"+new Date().format("dd_MM_yyyy")+".txt"
	response.setContentType("application/octet-stream")
	response.setHeader("Content-disposition", "attachment; filename=\"$name\"")
	response.outputStream << temp.newInputStream()
}
}
def bajasImss(){
	[reportCommand:new PeriodoCommand()]
}

def generarBajasImss(PeriodoCommand command){
	
	def temp = File.createTempFile('temp', '.txt')
	
	temp.with {
		Empresa emp=Empresa.first()
		def registroPatronal=emp.registroPatronal
		def numeroDeMovs=0
		Date fechaIni=command.fechaInicial
		Date fechaFin=command.fechaFinal
		def bajas=BajaDeEmpleado.findAll("from BajaDeEmpleado e where   e.fecha between ? and ?",[fechaIni,fechaFin]).each{calculo ->
		SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy")
			  
			numeroDeMovs=numeroDeMovs+1
			
		  def numeroSeguridadSocial= SeguridadSocial.findByEmpleado(calculo.empleado).numero.replace('-','')
		  def apellidoPaterno=calculo.empleado.apellidoPaterno ? calculo.empleado.apellidoPaterno.replace('\u00d1','N').padRight(27) : calculo.empleado.apellidoMaterno.replace('\u00d1','N').padRight(27) 
		  def apellidoMaterno=calculo.empleado.apellidoMaterno ? calculo.empleado.apellidoMaterno.replace('\u00d1','N').padRight(27) : StringUtils.leftPad("",27)
		  def nombres= calculo.empleado.nombres ? calculo.empleado.nombres.replace('\u00d1','N').padRight(27) : StringUtils.leftPad("",27)
		  def salarioBase= "000000"   //calculo.empleado.salario.salarioDiarioIntegrado.toString().replace('.','').padLeft(6,"0") //calculo.sdiNuevo.toString().replace('.','').padLeft(6,"0")
		  def filler= "000000"
		  def tipoTrabajador="0"
		  def fillerBlanco= "00" //StringUtils.leftPad("0",2)
		  def fechaMov=df.format(calculo.fecha)
		  def unidadMedica= StringUtils.leftPad("",3)  //calculo.empleado.seguridadSocial.unidadMedica? calculo.empleado.seguridadSocial.unidadMedica.padLeft(3,"0") :"000"
		  def filler2=StringUtils.leftPad("",2)
		  def tipoMov="02"
		  def guia="11400"
		  def claveTrab= StringUtils.leftPad("",10)  //StringUtils.leftPad("",10)
		  def causaBaja=calculo.motivo.clave
	  
		  def curp= StringUtils.leftPad("",18) //calculo.empleado.curp?:calculo.empleado.rfc.padLeft(18," ")
		  def identificador="9"
			
		   println numeroSeguridadSocial+apellidoPaterno+apellidoMaterno+nombres+salarioBase+filler+tipoTrabajador+fechaMov+unidadMedica+filler2+tipoMov+guia+claveTrab+causaBaja+curp+identificador
		   def registro=registroPatronal+numeroSeguridadSocial+apellidoPaterno+apellidoMaterno+nombres+salarioBase+filler+tipoTrabajador+fillerBlanco+fechaMov+unidadMedica+filler2+tipoMov+guia+claveTrab+causaBaja+curp+identificador
		   append(registro+"\r\n","UTF8")	  
		}
		
  }
	String name="BajasIMSS_"+new Date().format("dd_MM_yyyy")+".txt"
	response.setContentType("application/octet-stream")
	response.setHeader("Content-disposition", "attachment; filename=\"$name\"")
	response.outputStream << temp.newInputStream()
}	
	

/** Layouts del SUA*/

def trabajadoresSua(){
	[reportCommand:new PeriodoCommand()]
}

def generarTrabajadoresSua(PeriodoCommand command){
	
	def temp = File.createTempFile('temp', '.txt')
	
	temp.with {
		Empresa emp=Empresa.first()
		def registroPatronal=emp.registroPatronal
		def fechaInicial=command.fechaInicial
		def fechaFinal=command.fechaFinal
		def empleados = Empleado.findAll("from Empleado e where  alta between ? and ? order by e.apellidoPaterno asc",[fechaInicial,fechaFinal]).each{empleado->
		SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy")
		def formato = new DecimalFormat("###")
		def formatoDec = new DecimalFormat(".####")
		  
		  
		  def numSeguridadSocial=SeguridadSocial.findByEmpleado(empleado).numero.replace('-','')
			 def rfc=empleado.rfc.padLeft(13)
		  def curp=empleado.curp
		  def nombre=((empleado.apellidoPaterno?(empleado.apellidoPaterno+"\$"):(empleado.apellidoMaterno+"\$"))+(empleado.apellidoPaterno?(empleado.apellidoMaterno+"\$"):"\$")+empleado.nombres).padRight(50)
			def tipoTrabajador="1"
		  def jornada="0"
		  if(empleado.perfil.jornada=="MEDIA" || empleado.perfil.jornada=="REDUCIDA"){
			jornada="6"
			 }
		  def fechaAlta=df.format(empleado.alta)
		  //def sdi=empleado.salario.salarioDiarioIntegrado.toString().replace('.','').padLeft(7,"0")
		  def sdi=ModificacionSalarial.findByEmpleadoAndTipo(empleado,'ALTA').sdiNuevo.toString().replace('.','').padLeft(7,"0")
		  def ubicacion=StringUtils.leftPad("",17)
		  
		  def infonavitNumero=StringUtils.leftPad("",10)
		  def inicioDesc="00000000"
		  def tipoDesc="0"
		  def valorDesc="00000000"
		  def infonavit=Infonavit.findByEmpleadoAndActivo(empleado,true)
		  if(infonavit)
		  {
	   
				infonavitNumero=infonavit.numeroDeCredito.padLeft(10)
			  inicioDesc=infonavit.alta<new Date("01/07/1997")?"30061997":df.format(infonavit.alta)
			  tipoDesc=infonavit.tipo=="PORCENTAJE"?"1":infonavit.tipo=="CUOTA_FIJA"?"2":infonavit.tipo=="VSM"?"3":"0"
			int importeInd=Math.floor(infonavit.cuotaFija)
			def importeDesc =formato.format(importeInd) //.padLeft(16,"0");
			def decimalDesc=formatoDec.format(infonavit.cuotaFija-importeInd).replace('.','') //.padRight(2,"0")
			   valorDesc=importeDesc.padLeft(4,"0")+decimalDesc.padRight(4,"0")
				 
		  }
		
		  append(registroPatronal+numSeguridadSocial+rfc+curp+nombre+tipoTrabajador+jornada+fechaAlta+sdi+ubicacion+infonavitNumero+inicioDesc+tipoDesc+valorDesc+"\r\n")
	  
		}
	  
	}
	
	String name="TrabajadoresSUA_"+new Date().format("dd_MM_yyyy")+".txt"
	response.setContentType("application/octet-stream")
	response.setHeader("Content-disposition", "attachment; filename=\"$name\"")
	response.outputStream << temp.newInputStream()
	
}

def reporteDeTrabajadores(){
	[reportCommand:new PeriodoCommand()]
}

def reportePorPeriodo(PeriodoCommand command){
	if(command==null){
		render 'No esta bien generado el gsp para el reporte falta el bean PorEmpleadoCommand'
	}
	command.validate()
	if(command.hasErrors()){
		log.info 'Errores de validacion al ejecurar reporte: '+ params
		render view:WordUtils.uncapitalize(params.reportName),model:[reportCommand:command]
		return [reportCommand:command]
	}
	def repParams=[:]
	repParams['FECHA_INI']=command.fechaInicial
	repParams['FECHA_FIN']=command.fechaFinal 
	repParams.reportName=params.reportName?:'FaltaNombre Del Reporte'
	ByteArrayOutputStream  pdfStream=runReport(repParams)
	render(file: pdfStream.toByteArray(), contentType: 'application/pdf'
		,fileName:repParams.reportName)
}

def bajasSua(){
	[reportCommand:new PeriodoCommand()]
}

def generarBajasSua(PeriodoCommand command){
	
	def temp = File.createTempFile('temp', '.txt')
	
	temp.with {
		Empresa emp=Empresa.first()
		def registroPatronal=emp.registroPatronal
		def fechaIni=command.fechaInicial
		def fechaFin=command.fechaFinal
		def formato = new DecimalFormat("###")
		def formatoDec = new DecimalFormat(".####")
		  SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy")
		
	   
		 def bajas=BajaDeEmpleado.findAll("from BajaDeEmpleado e where   e.fecha between ? and ?",[fechaIni,fechaFin]).each{calculo ->
	   
		  
		  def numSeguridadSocial=SeguridadSocial.findByEmpleado(calculo.empleado).numero.replace('-','')
		  def tipoMov="02"
		  def fechaMov=df.format(calculo.fecha)
		  def folioInc= StringUtils.leftPad("",8)
		  def diasInc= StringUtils.leftPad("",2)
		  def sdiOAp= StringUtils.leftPad("0000000",7)
		  append(registroPatronal+numSeguridadSocial+tipoMov+fechaMov+folioInc+diasInc+sdiOAp+"\r\n")
	  
		}
	 
	}
	
	String name="BajasSUA_"+new Date().format("dd_MM_yyyy")+".txt"
	response.setContentType("application/octet-stream")
	response.setHeader("Content-disposition", "attachment; filename=\"$name\"")
	response.outputStream << temp.newInputStream()
	
}

def reporteDeBajas(){
	[reportCommand:new PeriodoCommand()]
}

def modificacionBimestralSua(){
	[reportCommand:new EjercicioBimestreCommand()]
}

def generarModificacionBimestralSua(EjercicioBimestreCommand command){
	
	def temp = File.createTempFile('temp', '.txt')
	
	temp.with {
		Empresa emp=Empresa.first()
		def registroPatronal=emp.registroPatronal
		def ejercicio=command.ejercicio
		def bimestre=command.bimestre
		def formato = new DecimalFormat("###")
		def formatoDec = new DecimalFormat(".####")
		  SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy")

		  def fecha_aplic
		  
				  switch(bimestre)
				  {
				
				  case 1:
						  fecha_aplic="0103"+ejercicio
				   break;
				  case 2:
						  fecha_aplic="0105"+ejercicio
				   break;
				  case 3:
						  fecha_aplic="0107"+ejercicio
				   break;
				  case 4:
						  fecha_aplic="0109"+ejercicio
				   break;
				  case 5:
						  fecha_aplic="0111"+ejercicio
				   break;
				  case 6:
						  fecha_aplic="0101"+(ejercicio+1)
				   break;
					 
					}
	   
		 def calculosSdi=CalculoSdi.findAllByEjercicioAndBimestre(ejercicio,bimestre).sort{it.empleado.apellidoPaterno}.each{calculo ->
	   
			 
			 if(calculo.sdiInf!=0.0  && calculo.tipo== 'CALCULO_SDI' ){
				 def numSeguridadSocial=SeguridadSocial.findByEmpleado(calculo.empleado).numero.replace('-','')
				 def tipoMov="07"
				 def fechaMov= fecha_aplic  //df.format(calculo.fechaFin+1)
				 def folioInc= StringUtils.leftPad("",8)
				 def diasInc= StringUtils.leftPad("",2)
				 def sdiOAp=calculo.sdiInf.toString().replace('.','').padLeft(7,"0")
				 append(registroPatronal+numSeguridadSocial+tipoMov+fechaMov+folioInc+diasInc+sdiOAp+"\r\n")
			 }
		  
		 
	  
		}
	  
	}
	
	String name="ModifBimestral_"+new Date().format("dd_MM_yyyy")+".txt"
	response.setContentType("application/octet-stream")
	response.setHeader("Content-disposition", "attachment; filename=\"$name\"")
	response.outputStream << temp.newInputStream()
	
}

def reporteDeModificacionBimestral(){
	[reportCommand:new EjercicioBimestreCommand()]
}

def reportePorBimestre(EjercicioBimestreCommand command){
	if(command==null){
		render 'No esta bien generado el gsp para el reporte falta el bean PorEmpleadoCommand'
	}
	command.validate()
	if(command.hasErrors()){
		log.info 'Errores de validacion al ejecurar reporte: '+ params
		render view:WordUtils.uncapitalize(params.reportName),model:[reportCommand:command]
		return [reportCommand:command]
	}
	
	def repParams=[:]
	repParams['BIMESTRE']=command.bimestre
	repParams['EJERCICIO']=command.ejercicio
	repParams.reportName=params.reportName?:'FaltaNombre Del Reporte'
	ByteArrayOutputStream  pdfStream=runReport(repParams)
	render(file: pdfStream.toByteArray(), contentType: 'application/pdf'
		,fileName:'TrabajadoresSua'+repParams.reportName)
}

def modificacionIndividualSua(){
	[reportCommand:new PeriodoCommand()]
}

def generarModificacionIndividualSua(PeriodoCommand command){
	
	def temp = File.createTempFile('temp', '.txt')
	
	temp.with {
		Empresa emp=Empresa.first()
		def registroPatronal=emp.registroPatronal
		def fechaIni=command.fechaInicial
		def fechaFin=command.fechaFinal
		def formato = new DecimalFormat("###")
		def formatoDec = new DecimalFormat(".####")
		  SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy")
		
	   def calculosSdi=ModificacionSalarial.findAll("from ModificacionSalarial m where m.fecha between ? and  ? and tipo=\'AUMENTO\'",[fechaIni,fechaFin]).each{calculo ->
		  
		  def numSeguridadSocial=SeguridadSocial.findByEmpleado(calculo.empleado).numero.replace('-','')
		  def tipoMov="07"
		  def fechaMov=df.format(calculo.fecha)
		  def folioInc= StringUtils.leftPad("",8)
		  def diasInc= StringUtils.leftPad("",2)
		  def sdiOAp=calculo.sdiNuevo.toString().replace('.','').padLeft(7,"0")
		  append(registroPatronal+numSeguridadSocial+tipoMov+fechaMov+folioInc+diasInc+sdiOAp+"\r\n")
	  
		}
	 
	}
	
	String name="ModificacionIndividualIMSS_"+new Date().format("dd_MM_yyyy")+".txt"
	response.setContentType("application/octet-stream")
	response.setHeader("Content-disposition", "attachment; filename=\"$name\"")
	response.outputStream << temp.newInputStream()
	
}

def reporteDeModificacionIndividual(){
	[reportCommand:new PeriodoCommand()]
}

def ausentismoSua(){
	[reportCommand:new PeriodoCommand()]
}

def generarAusentismoSua(PeriodoCommand command){
	
	def temp = File.createTempFile('temp', '.txt')
	
	temp.with {
		
		Empresa emp=Empresa.first()
		def registroPatronal=emp.registroPatronal
		def fechaIni=command.fechaInicial
		def fechaFin=command.fechaFinal
        //def fechaIni=new Date("2015/09/01")
        //def fechaFin=new Date("2015/09/30")
		def formato = new DecimalFormat("###")
		def formatoDec = new DecimalFormat(".####")
		SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy")
		Periodo periodo=new Periodo(fechaIni,fechaFin)
	  
		def numSeguridadSocial=""
		def tipoMov=""
		def fechaMov=""
		def folioInc="        "
		def diasInc=""
		def sdiOAp="0000000"
	   
		  def ausentismos=AsistenciaImssDet.findAll("from AsistenciaImssDet i where  i.fecha between ? and ? and i.tipo='falta' and excluir=false "
												,[fechaIni,fechaFin]).sort{it.asistenciaImss.empleado}.each{calculo ->
            BajaDeEmpleado baja =BajaDeEmpleado.find("from BajaDeEmpleado b where b.empleado=? and b.fecha>=?",[calculo.asistenciaImss.empleado,calculo.asistenciaImss.empleado.alta])
			def fechaBaja=baja? baja.fecha :new Date()
            
            if(calculo.asistenciaImss.empleado.controlDeAsistencia==true  && calculo.fecha>=calculo.asistenciaImss.empleado.alta && calculo.fecha<= fechaBaja ){
              numSeguridadSocial=SeguridadSocial.findByEmpleado(calculo.asistenciaImss.empleado).numero.replace('-','')
              tipoMov="11"
			  fechaMov=df.format(calculo.fecha)
			  folioInc="        "
			  diasInc="01"
              append(registroPatronal+numSeguridadSocial+tipoMov+fechaMov+folioInc+diasInc+sdiOAp+"\r\n")
            }else if(calculo.asistenciaImss.empleado.controlDeAsistencia==true && calculo.asistenciaImss.asistencia.diasTrabajados!=0.0 && calculo.asistenciaImss.asistencia.faltasManuales>0 && calculo.fecha<= fechaBaja ){
              
               numSeguridadSocial=SeguridadSocial.findByEmpleado(calculo.asistenciaImss.empleado).numero.replace('-','')
			   tipoMov="11"
			   fechaMov=df.format(calculo.fecha)
			   folioInc="        "
			   diasInc="01"
			  append(registroPatronal+numSeguridadSocial+tipoMov+fechaMov+folioInc+diasInc+sdiOAp+"\r\n")
              
            }
            
            
          }
		
	}
	
	String name="AusentismoSUA_"+new Date().format("dd_MM_yyyy")+".txt"
	response.setContentType("application/octet-stream")
	response.setHeader("Content-disposition", "attachment; filename=\"$name\"")
	response.outputStream << temp.newInputStream()
	
}


def reporteDeAusentismo(){
	[reportCommand:new PeriodoCommand()]
}

def incapacidadesSua(){
	[reportCommand:new PeriodoCommand()]
}

def generarIncapacidadesSua(PeriodoCommand command){
	
	def temp = File.createTempFile('temp', '.txt')
	
	temp.with {
		
		Empresa emp=Empresa.first()
	  def registroPatronal=emp.registroPatronal
	  def fechaIni=command.fechaInicial
	  def fechaFin=command.fechaFinal
	  def formato = new DecimalFormat("###")
	  def formatoDec = new DecimalFormat(".####")
	  SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy")
	  Periodo periodo=new Periodo(fechaIni,fechaFin)
	 
		def incapacidades=Incapacidad.findAll("from Incapacidad i where  i.fechaInicial between ? and ? "
											  ,[fechaIni,fechaFin]).each{calculo ->
	 
		
		def numSeguridadSocial=SeguridadSocial.findByEmpleado(calculo.empleado).numero.replace('-','')
		def tipoMov="12"
		def fechaMov=df.format(calculo.fechaInicial)
		def folioInc= calculo.referenciaImms.padLeft(8)
		def diasInc= (calculo.fechaFinal-calculo.fechaInicial+1).toString().padLeft(2,"0")
		  
		  
		  
		
		def sdiOAp="0000000" //calculo.sdiNuevo.toString().replace('.','').padLeft(7,"0")
		
	   
		println calculo.empleado.id+"-"+calculo.empleado.status+"--"+registroPatronal+numSeguridadSocial+tipoMov+fechaMov+folioInc+diasInc+sdiOAp+"fin"
		append(registroPatronal+numSeguridadSocial+tipoMov+fechaMov+folioInc+diasInc+sdiOAp+"\r\n")
	
	  }
	
	}
	
	String name="Incapacidades"+new Date().format("dd_MM_yyyy")+".txt"
	response.setContentType("application/octet-stream")
	response.setHeader("Content-disposition", "attachment; filename=\"$name\"")
	response.outputStream << temp.newInputStream()
	
}

	def exportadorDim
	
	def dim(){
		def ejercicio=session.ejercicio
		exportadorDim.generarLayuout(ejercicio)
	}

def reporteDeIncapacidades(){
	[reportCommand:new PeriodoCommand()]
}

def infonavitSua(){
	[reportCommand:new PeriodoCommand()]
}

def generarInfonavitSua(PeriodoCommand command){
	
	def temp = File.createTempFile('temp', '.txt')
	
	temp.with {
		  
		Empresa emp=Empresa.first()
	  def registroPatronal=emp.registroPatronal
	    def fechaInicial=command.fechaInicial
	  def fechaFinal=command.fechaFinal
	  def empleados = Infonavit.findAll("from Infonavit i order by i.empleado.apellidoPaterno asc").each{infonavit->
	  SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy")
	  def formato = new DecimalFormat("###")
	  def formatoDec = new DecimalFormat(".####")
		
		
		def numSeguridadSocial=SeguridadSocial.findByEmpleado(infonavit.empleado).numero.replace('-','')
	  
		def infonavitNumero="0000000000"
		def tipoMov="00"
		def fechaMov="00000000"
		def tipoDesc="0"
		def valorDesc="00000000"
		def aplicaTabla="N"
	
		if (infonavit.alta> fechaInicial && infonavit.alta<fechaFinal){
			 fechaMov=df.format(infonavit.alta)
				tipoMov="15"
		}
		if (infonavit.suspension> fechaInicial && infonavit.suspension<fechaFinal){
			   fechaMov=df.format(infonavit.suspension)
				tipoMov="16"
		}
		if (infonavit.reinicio> fechaInicial && infonavit.reinicio<fechaFinal){
			   fechaMov=df.format(infonavit.reinicio)
				tipoMov="17"
		}
		if (infonavit.modificacionTipo> fechaInicial && infonavit.modificacionTipo<fechaFinal){
			   fechaMov=df.format(infonavit.modificacionTipo)
				tipoMov="18"
		}
		if (infonavit.modificacionValor> fechaInicial && infonavit.modificacionValor<fechaFinal){
			   fechaMov=df.format(infonavit.modificacionValor)
				tipoMov="19"
		}
		if (infonavit.modificacionNumero> fechaInicial && infonavit.modificacionNumero<fechaFinal){
			   fechaMov=df.format(infonavit.modificacionNumero)
				tipoMov="20"
		}
		
		
		infonavitNumero=infonavit.numeroDeCredito.padLeft(10)
		tipoDesc=infonavit.tipo=="PORCENTAJE"?"1":infonavit.tipo=="CUOTA_FIJA"?"2":infonavit.tipo=="VSM"?"3":"0"
		int importeInd=Math.floor(infonavit.cuotaFija)
		def importeDesc =formato.format(importeInd) //.padLeft(16,"0");
		def decimalDesc=formatoDec.format(infonavit.cuotaFija-importeInd).replace('.','') //.padRight(2,"0")
		 valorDesc=importeDesc.padLeft(4,"0")+decimalDesc.padRight(4,"0")
	
    	if(fechaMov!="00000000"){
		
		  def registro=registroPatronal+numSeguridadSocial+infonavitNumero+tipoMov+fechaMov+tipoDesc+valorDesc+aplicaTabla+"\r\n" //+aplicaTabla
	
		append registro
		  
		}
		
	  }
	
	}
	
	String name="Infonavit"+new Date().format("dd_MM_yyyy")+".txt"
	response.setContentType("application/octet-stream")
	response.setHeader("Content-disposition", "attachment; filename=\"$name\"")
	response.outputStream << temp.newInputStream()
}

def reporteDeInfonavit(){
	[reportCommand:new PeriodoCommand()]
}


def rfc(){
	[reportCommand:new PeriodoCommand()]
}

def generarRfc(PeriodoCommand command){
	
def temp = File.createTempFile('temp', '.txt')
	
	def consecutivo="01"
	  Empresa emp=Empresa.first()
	
	temp.with {
	  
	def fechaIni=command.fechaInicial  //new Date('2015/01/13')
	def fechaFin=command.fechaFinal    //new Date('2015/01/31')
	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy")

	
	def rfc=emp.rfc
	def p="|"
	def empleados = Empleado.findAll("from Empleado e where e.alta between ? and ?",[fechaIni,fechaFin]).each{ empleado ->
  
		  def curp=empleado.curp
		  def apellidoPaterno=empleado.apellidoPaterno?empleado.apellidoPaterno:empleado.apellidoMaterno
		  def apellidoMaterno=empleado.apellidoPaterno?empleado.apellidoMaterno:""
		  def nombre=empleado.nombres
		  def ingreso=df.format(empleado.alta)
		  def tipoSalario="2"
		  
  
 
	  def registro=curp+p+apellidoPaterno+p+apellidoMaterno+p+nombre+p+ingreso+p+tipoSalario+p+rfc+"\r\n"
	 	append registro
	 //println registro
		  
	 }

}
	String name=emp.rfc+"_"+new Date().format("ddMMyyyy")+"_"+consecutivo+".txt"
	response.setContentType("application/octet-stream")
	response.setHeader("Content-disposition", "attachment; filename=\"$name\"")
	response.outputStream << temp.newInputStream()
  

}


def reporteDeRFC(){
	[reportCommand:new PeriodoCommand()]
}




private runReport(Map repParams){
	log.info 'Ejecutando reporte  '+repParams
	def nombre=WordUtils.capitalize(repParams.reportName)
	def reportDef=new JasperReportDef(
		name:nombre
		,fileFormat:JasperExportFormat.PDF_FORMAT
		,parameters:repParams
		)
	ByteArrayOutputStream  pdfStream=jasperService.generateReport(reportDef)
	return pdfStream
	
}




}




@Validateable
class NominaCommand{
	Nomina nomina

	static constraints={
		nomina nullable:false
	}
}
