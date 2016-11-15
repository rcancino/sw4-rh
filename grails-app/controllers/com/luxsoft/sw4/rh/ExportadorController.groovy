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
import java.math.*
import com.luxsoft.sw4.rh.tablas.TarifaIsr



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
				if(it.empleado.id==280 || it.empleado.id==260 ||  it.empleado.id==245 || it.empleado.id==271)
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

				if(it.empleado.id==271 )
					claveSucInd="7002"
				
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
			  
		   if(calculo.sdiInf!= calculo.sdiAnterior){
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
		//return [reportCommand:command]
		return
	}
	def repParams=[:]
	repParams['FECHA_INI']=command.fechaInicial
	repParams['FECHA_FIN']=command.fechaFinal 
	repParams.reportName=params.reportName?:'FaltaNombre Del Reporte'
	ByteArrayOutputStream  pdfStream=runReport(repParams)
	render(file: pdfStream.toByteArray(), contentType: 'application/pdf'
		,fileName:repParams.reportName+".pdf")
}

def incapacidades(PeriodoCommand command){
		if(request.method=='GET'){
			return [reportCommand:new PeriodoCommand()]
		}
		command.validate()
		if(command.hasErrors()){
			log.info 'Errores de validacion al ejecurar reporte'
			render view:'incapacidades',model:[reportCommand:command]
			return
		}
		def repParams=[:]
		repParams['FECHA_INICIAL']=command.fechaInicial
		repParams['FECHA_FINAL']=command.fechaFinal
		repParams.reportName=params.reportName?:'FaltaNombre Del Reporte'
		ByteArrayOutputStream  pdfStream=runReport(repParams)
		render(file: pdfStream.toByteArray(), contentType: 'application/pdf'
			,fileName:repParams.reportName+".pdf")
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

			 if(calculo.sdiInf!= calculo.sdiAnterior){
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

	
def dim(){
		[reportCommand:new EjercicioCommand()]
}

def generarDim(EjercicioCommand command){

	println "Generando Layout DIM Sueldos y Salarios"

		def temp = File.createTempFile('temp', '.txt')
	
temp.with {
	def p="|"
 	Periodo periodo=new Periodo()
	def ejercicio=2015
    SimpleDateFormat df= new SimpleDateFormat("dd/MM/yyyy")
    def formato = new DecimalFormat("#####")
  
	def registros=0
	def empleados=CalculoAnual.findAll("from CalculoAnual c where c.ejercicio=?  ",[ejercicio]).each{calculo ->
  
  	def yearAlta=periodo.obtenerYear(calculo.empleado.alta)
    def yearBaja
  	def bajaFecha  
  
    def mesIni="01"
    def mesFin="12"
  
    def baja =BajaDeEmpleado.findAllByEmpleado(calculo.empleado)
   
    if (baja){
      bajaFecha=new Date().parse('yyyy-MM-dd', baja.fecha.toString().replace('[','').replace(' 00:00:00.0]',''))  
      yearBaja=periodo.obtenerYear(bajaFecha)
      }else{
       yearBaja=ejercicio+1
    }
  
 
    
    if(yearAlta==ejercicio){
     mesIni=(periodo.obtenerMes(calculo.empleado.alta)+1).toString().padLeft(2,"0") 
    } 
    
  if(yearBaja==ejercicio){
      	mesFin=(periodo.obtenerMes(bajaFecha)+1).toString().padLeft(2,"0") 
      }
 
    def rfc=calculo.empleado.rfc.padLeft(13," ") 
    def curp=calculo.empleado.curp.padLeft(18," ")
    
    def apellidoPaterno=calculo.empleado.apellidoPaterno  ? calculo.empleado.apellidoPaterno.replace('Ñ','N') : calculo.empleado.apellidoMaterno.replace('Ñ','N') 
    def apellidoMaterno=calculo.empleado.apellidoPaterno ? calculo.empleado.apellidoMaterno.replace('Ñ','N') : "" 
    def nombres= calculo.empleado.nombres.replace('Ñ','N')?:""  
    def areaGeo="01"
    def calculoAnual="1"
        
          
    def tarifaUtilizada="1"
    def tarifaUtiAct="2"
    def proporcionDeSubsApl=""
    def sindicalizado= PerfilDeEmpleado.findByEmpleado(calculo.empleado).tipo =="SINDICALIZADO"?"1":"2" 
    def asimiladosASal="0"
    def claveEntidad="09"
    def montoAportaciones=""
    def aplicoMonto="0"
    def montoAportacionesDed=""
    def montoAportacionesDedPat=""
      
        //SELECCION DE TEMAS
        
    def pagosPorSeparacion="2"
    def asimilados="2"
    def pagosPatron="1"
    
        //SUELDOS
        
    def sueldosGra=0
    def sueldosExc=0
    def gratificacionAnualGra=0
    def gratificacionAnualExc=0
    def viaticosGra=0
    def viaticosExc=0
    def tiempExtraGra=0
    def tiempoExtraExc=0
    def primaVacGra=0
    def primaVacExc=0
    def primaDomGra=0
    def primaDomExc=0
    def ptuGra=0
    def ptuExc=0
    def gastosMedGra=0
    def gastosMedExc=0
    def fondoDeAhorroGra=0
    def fondoDeAhorroExc=0
    def cajaDeAhorroGra=0
    def cajaDeAhorroExc=0
    def valesDespensaGra=0
    def valesDespensaExc=0
    def gastosFuneralesGra=0
    def gastosFuneralesExc=0
    def contribucionesTrabGra=0
    def contribucionesTrabExc=0
    def premioPuntGra=0
    def premioPuntExc=0
    def primaSeguroVidaGra=0
    def primaSeguroVidaExc=0
    def gastosMedMayGra=0
    def gastosMedMayExc=0
    def valesRestGra=0
    def valesRestExc=0
    def valesGasolinaGra=0
    def valesGasolinaExc=0
    def valesRopaGra=0
    def valesRopaExc=0
    def ayudaRentaGra=0
    def ayudaRentaExc=0
    def ayudaArtEscGra=0
    def ayudaArtEscExc=0
    def ayudaAnteojosGra=0
    def ayudaAnteojosExc=0
    def ayudaTransporteGra=0
    def ayudaTransporteExc=0
    def cuotasSindicalesGra=0
    def cuotasSindicalesExc=0
    def subsIncapGra=0
    def subsIncapExc=0
    def becasGra=0
    def becasExc=0
    def pagosOtrosGra=0
    def pagosOtrosExc=0
    def OtrosIngreGra=0
    def otrosIngreExc=0
    def sumaIngreSalGra=0
    def sumaIngreSalExc=0
    def impuestoRet=0
    def impuestoRetOtros=0
    def saf=0
    def safAnt=0
    def sumaCreditoTrab=0
    def credSalEntregado=0
    def totalIngresosPrestPrevSocial=0
    def ingresosExcPrestPrevSocial=0
    def sumaSueldos=0
    def impuestoLocalIngresos=0
    def subsEmpleoTrab=0
    
    ///IMPUESTO SOBRE LA RENTA RESUMEN
    
    def aportacionesVoDed=0
    def ISRTarifAnual=0
    def subsidioAcreditable=0
    def subsidioNoAcreditable=0
    def impuestoSobreIngreAcu=0
    def impuestoSobreIngreNoAcu=0
    def impuestoLocalASueldos=0
    def subsidioEmpleoEjercicio=0
    
  
    
   def empleadoIndemnizacion=NominaPorEmpleadoDet.executeQuery("select d.parent.empleado from NominaPorEmpleadoDet d where d.parent.empleado=? and d.parent.nomina.ejercicio=? and finiquito is true"
                                                             ,[calculo.empleado,ejercicio])
      
      	if(!empleadoIndemnizacion ){
      	
        		
      
    
		    calculoAnual=calculo.calculoAnual?"1":"2"
     
     		sueldosGra=(calculo.sueldo+calculo.vacaciones+calculo.vacacionesPagadas+calculo.comisiones+calculo.permisoPorPaternidad-calculo.retardos).setScale(0, RoundingMode.HALF_EVEN)
     		sueldosExc=0
     		gratificacionAnualGra=calculo.aguinaldoGravable.setScale(0, RoundingMode.HALF_EVEN)
     		gratificacionAnualExc=calculo.aguinaldoExento.setScale(0, RoundingMode.HALF_EVEN)
     		viaticosGra=0
     		viaticosExc=0
     		tiempExtraGra=(calculo.tiempoExtraDobleGravado+calculo.tiempoExtraTripleGravado).setScale(0, RoundingMode.HALF_EVEN)
     		tiempoExtraExc=calculo.tiempoExtraDobleExento.setScale(0, RoundingMode.HALF_EVEN)
     		primaVacGra=calculo.primaVacacionalGravada.setScale(0, RoundingMode.HALF_EVEN)
     		primaVacExc=calculo.primaVacacionalExenta.setScale(0, RoundingMode.HALF_EVEN)
     		primaDomGra=calculo.primaDominicalGravada.setScale(0, RoundingMode.HALF_EVEN)
     		primaDomExc=calculo.primaDominicalExenta.setScale(0, RoundingMode.HALF_EVEN)
     		ptuGra=calculo.ptuGravada.setScale(0, RoundingMode.HALF_EVEN)
     		ptuExc=calculo.ptuExenta.setScale(0, RoundingMode.HALF_EVEN)
     		gastosMedGra=0
     		gastosMedExc=0
     		fondoDeAhorroGra=0
     		fondoDeAhorroExc=0
     		cajaDeAhorroGra=0
     		cajaDeAhorroExc=0
     		valesDespensaGra=0
     		valesDespensaExc=0
     		gastosFuneralesGra=0
     		gastosFuneralesExc=0
     		contribucionesTrabGra=0
     		contribucionesTrabExc=0
     		premioPuntGra=0
     		premioPuntExc=0
     		primaSeguroVidaGra=0
     		primaSeguroVidaExc=0
     		gastosMedMayGra=0
     		gastosMedMayExc=0
     		valesRestGra=0
     		valesRestExc=0
     		valesGasolinaGra=0
     		valesGasolinaExc=0
     		valesRopaGra=0
     		valesRopaExc=0
     		ayudaRentaGra=0
     		ayudaRentaExc=0
     		ayudaArtEscGra=0
     		ayudaArtEscExc=0
     		ayudaAnteojosGra=0
     		ayudaAnteojosExc=0
     		ayudaTransporteGra=0
     		ayudaTransporteExc=0
     		cuotasSindicalesGra=0
     		cuotasSindicalesExc=0
     		subsIncapGra=0
     		subsIncapExc=0
     		becasGra=0
     		becasExc=0
     		pagosOtrosGra=0
     		pagosOtrosExc=0
     		OtrosIngreGra=(calculo.incentivo+calculo.bonoDeProductividad+calculo.bonoPorDesempeno+calculo.compensacion+calculo.bono+calculo.bonoAntiguedad).setScale(0, RoundingMode.HALF_EVEN)
    		otrosIngreExc=0
     		sumaIngreSalGra=calculo.totalGravado.setScale(0, RoundingMode.HALF_EVEN)
     		sumaIngreSalExc=calculo.totalExento.setScale(0, RoundingMode.HALF_EVEN)
     		impuestoRet=calculo.ISR.setScale(0, RoundingMode.HALF_EVEN)
     		impuestoRetOtros=0
     		//saf=calculo.resultado.setScale(0, RoundingMode.HALF_EVEN)>0 ? calculo.resultado.setScale(0, RoundingMode.HALF_EVEN) :0
     		saf=calculo.calculoAnual? calculo.resultado.setScale(0, RoundingMode.HALF_EVEN) :0
  
			def calculoAnt=CalculoAnual.findByEjercicioAndEmpleado(calculo.ejercicio-1,calculo.empleado)  
  
     		safAnt= 0 //calculoAnt? (calculoAnt.calculoAnual ? (calculoAnt.resultado-calculo.aplicado).setScale(0, RoundingMode.HALF_EVEN) : 0)  :0
  
     		sumaCreditoTrab=0
     		credSalEntregado=0
  
     		totalIngresosPrestPrevSocial=0
     		ingresosExcPrestPrevSocial=0
  
     		sumaSueldos=calculo.total.setScale(0, RoundingMode.HALF_EVEN)
     		impuestoLocalIngresos=0
    		subsEmpleoTrab=calculo.subsEmpPagado.setScale(0, RoundingMode.HALF_EVEN)
    
   			 ///IMPUESTO SOBRE LA RENTA RESUMEN
    
     		aportacionesVoDed=0
    		ISRTarifAnual=calculo.calculoAnual?calculo.impuestoDelEjercicio.setScale(0, RoundingMode.HALF_EVEN):0
  			subsidioAcreditable=0
     		subsidioNoAcreditable=0
     		impuestoSobreIngreAcu=0
     		impuestoSobreIngreNoAcu=0
     		impuestoLocalASueldos=0
     		subsidioEmpleoEjercicio=calculo.subsEmpAplicado.setScale(0, RoundingMode.HALF_EVEN)
      
       
    		def registro=mesIni+p+mesFin+p +rfc+p+curp+p+apellidoPaterno+p+apellidoMaterno+p+nombres+p+areaGeo+p+calculoAnual+p+tarifaUtilizada+p+tarifaUtiAct+p+proporcionDeSubsApl+p+sindicalizado+p+asimiladosASal+p+claveEntidad+
      		p+p+p+p+p+p+p+p+p+p+p+
       		montoAportaciones+p+aplicoMonto+p+montoAportacionesDed+p+montoAportacionesDedPat+p+pagosPorSeparacion+p+asimilados+p+pagosPatron+
        	p+sueldosGra+p+sueldosExc+p+gratificacionAnualGra+p+gratificacionAnualExc+p+viaticosGra+p+viaticosExc+p+tiempExtraGra+p+tiempoExtraExc+p+
        	primaVacGra+p+primaVacExc+p+primaDomGra+p+primaDomExc+p+ptuGra+p+ptuExc+p+gastosMedGra+p+gastosMedExc+p+fondoDeAhorroGra+p+
        	fondoDeAhorroExc+p+cajaDeAhorroGra+p+cajaDeAhorroExc+p+valesDespensaGra+p+valesDespensaExc+p+gastosFuneralesGra+p+gastosFuneralesExc+p+
        	contribucionesTrabGra+p+contribucionesTrabExc+p+premioPuntGra+p+premioPuntExc+p+primaSeguroVidaGra+p+primaSeguroVidaExc+p+gastosMedMayGra+p+
        	gastosMedMayExc+p+valesRestGra+p+valesRestExc+p+valesGasolinaGra+p+valesGasolinaExc+p+valesRopaGra+p+valesRopaExc+p+ayudaRentaGra+p+
        	ayudaRentaExc+p+ayudaArtEscGra+p+ayudaArtEscExc+p+ayudaAnteojosGra+p+ayudaAnteojosExc+p+ayudaTransporteGra+p+ayudaTransporteExc+p+
        	cuotasSindicalesGra+p+cuotasSindicalesExc+p+subsIncapGra+p+subsIncapExc+p+becasGra+p+becasExc+p+pagosOtrosGra+p+ pagosOtrosExc+p+
        	OtrosIngreGra+p+otrosIngreExc+p+sumaIngreSalGra+p+sumaIngreSalExc+p+impuestoRet+p+impuestoRetOtros+p+saf+p+safAnt+p+sumaCreditoTrab+p+
       	 	credSalEntregado+p+totalIngresosPrestPrevSocial+p+ingresosExcPrestPrevSocial+p+sumaSueldos+p+impuestoLocalIngresos+p+subsEmpleoTrab+p+
        	aportacionesVoDed+p+ISRTarifAnual+p+subsidioAcreditable+p+subsidioNoAcreditable+p+impuestoSobreIngreAcu+p+impuestoSobreIngreNoAcu+p+
       	 	impuestoLocalASueldos+p+subsidioEmpleoEjercicio+p
    
   			append(registro+"\r\n","UTF8")
    
    
  			}
        
  		}
	 }

	String name="DIM Sueldos"+new Date().format("dd_MM_yyyy")+".txt"
	response.setContentType("application/octet-stream")
	response.setHeader("Content-disposition", "attachment; filename=\"$name\"")
	response.outputStream << temp.newInputStream()
		
}

def dimPagosPorSeparacion(){
		[reportCommand:new EjercicioCommand()]
}


def generarDimPagosPorSeparacion(){
		println "Generando Layout DIM Sueldos y Salarios"

		def temp = File.createTempFile('temp', '.txt')
	
	temp.with {
		def p="|"
 Periodo periodo=new Periodo()
	def ejercicio=2015
    SimpleDateFormat df= new SimpleDateFormat("dd/MM/yyyy")
    def formato = new DecimalFormat("#####")
  
def registros=0
def empleados=CalculoAnual.findAll("from CalculoAnual c where c.ejercicio=?  ",[ejercicio]).each{calculo ->
  
 
  
  
  
  def yearAlta=periodo.obtenerYear(calculo.empleado.alta)
    def yearBaja
  	def bajaFecha  
  
    def mesIni="01"
    def mesFin="12"
  
  	def rfc=calculo.empleado.rfc.padLeft(13," ") 
    def curp=calculo.empleado.curp.padLeft(18," ")
    
    def apellidoPaterno=calculo.empleado.apellidoPaterno  ? calculo.empleado.apellidoPaterno.replace('Ñ','N') : calculo.empleado.apellidoMaterno.replace('Ñ','N') 
    def apellidoMaterno=calculo.empleado.apellidoPaterno ? calculo.empleado.apellidoMaterno.replace('Ñ','N') : "" 
    def nombres= calculo.empleado.nombres.replace('Ñ','N')?:"" 
    def areaGeo="01"
    def calculoAnual="1"
        
          
    def tarifaUtilizada="1"
    def tarifaUtiAct="2"
    def proporcionDeSubsApl=""
    def sindicalizado= PerfilDeEmpleado.findByEmpleado(calculo.empleado).tipo =="SINDICALIZADO"?"1":"2" 
    def asimiladosASal="0"
    def claveEntidad="09"
    def montoAportaciones=""
    def aplicoMonto="0"
    def montoAportacionesDed=""
    def montoAportacionesDedPat=""
      
        //SELECCION DE TEMAS
        
    def pagosPorSeparacion="1"
    def asimilados="2"
    def pagosPatron="1"
  
  
   //SUELDOS
        
    def sueldosGra=0
    def sueldosExc=0
    def gratificacionAnualGra=0
    def gratificacionAnualExc=0
    def viaticosGra=0
    def viaticosExc=0
    def tiempExtraGra=0
    def tiempoExtraExc=0
    def primaVacGra=0
    def primaVacExc=0
    def primaDomGra=0
    def primaDomExc=0
    def ptuGra=0
    def ptuExc=0
    def gastosMedGra=0
    def gastosMedExc=0
    def fondoDeAhorroGra=0
    def fondoDeAhorroExc=0
    def cajaDeAhorroGra=0
    def cajaDeAhorroExc=0
    def valesDespensaGra=0
    def valesDespensaExc=0
    def gastosFuneralesGra=0
    def gastosFuneralesExc=0
    def contribucionesTrabGra=0
    def contribucionesTrabExc=0
    def premioPuntGra=0
    def premioPuntExc=0
    def primaSeguroVidaGra=0
    def primaSeguroVidaExc=0
    def gastosMedMayGra=0
    def gastosMedMayExc=0
    def valesRestGra=0
    def valesRestExc=0
    def valesGasolinaGra=0
    def valesGasolinaExc=0
    def valesRopaGra=0
    def valesRopaExc=0
    def ayudaRentaGra=0
    def ayudaRentaExc=0
    def ayudaArtEscGra=0
    def ayudaArtEscExc=0
    def ayudaAnteojosGra=0
    def ayudaAnteojosExc=0
    def ayudaTransporteGra=0
    def ayudaTransporteExc=0
    def cuotasSindicalesGra=0
    def cuotasSindicalesExc=0
    def subsIncapGra=0
    def subsIncapExc=0
    def becasGra=0
    def becasExc=0
    def pagosOtrosGra=0
    def pagosOtrosExc=0
    def otrosIngreGra=0
    def otrosIngreExc=0
    def sumaIngreSalGra=0
    def sumaIngreSalExc=0
    def impuestoRet=0
    def impuestoRetOtros=0
    def saf=0
    def safAnt=0
    def sumaCreditoTrab=0
    def credSalEntregado=0
    def totalIngresosPrestPrevSocial=0
    def ingresosExcPrestPrevSocial=0
    def sumaSueldos=0
    def impuestoLocalIngresos=0
    def subsEmpleoTrab=0
  
  //INDEMNIZACIONES
      		//NO
     def ingresosTotPAgParc=0
     def montoDiarioJubi=0
     def noPagoUnicoJubi=0
     def totalPagoUnaExhib=0
     def numeroDias=0
     def ingresosExeNo=0
     def ingresosGravNo=0
     def ingresosAcum=0
     def ingresosNoAcum=0
     def inpuestoRetenido=0
      
      		//SI
                                                                      
     def totalPagOtrosSep=0
     def aosDeServicio=0
     def ingresosExe=0
     def ingresosGrav=0
     def ingresosAcuUltimoSueldoMen=0
     def impuestoCorrespUltSueldoMen=0
     def ingresosNoAcumulables=0
     def impuestoRetenido=0
  
  
  
  
   	
 	 
    def baja =BajaDeEmpleado.findAllByEmpleado(calculo.empleado)
  if(baja){
  	bajaFecha=new Date().parse('yyyy-MM-dd', baja.fecha.toString().replace('[','').replace(' 00:00:00.0]','')) 
   	
    
    
      def nominaIndemnizacion=NominaPorEmpleadoDet.executeQuery("from NominaPorEmpleadoDet d where d.parent.empleado=? and d.parent.nomina.ejercicio=? and d.parent.finiquito is true"
                                                                ,[calculo.empleado,ejercicio]).each{ nominaDet ->
       
        
        if(nominaDet.concepto.id==40 || nominaDet.concepto.id==38 ){
      		
            totalPagOtrosSep= (totalPagOtrosSep+nominaDet.importeGravado+nominaDet.importeExcento).setScale(0, RoundingMode.HALF_EVEN)
          	ingresosExe=(ingresosExe+nominaDet.importeExcento).setScale(0, RoundingMode.HALF_EVEN)
          	ingresosGrav=(ingresosGrav+nominaDet.importeGravado).setScale(0, RoundingMode.HALF_EVEN)
          	
          
        }
        
        
      }
      
      if(nominaIndemnizacion ){
      	
        println "Generando Layout para "+ calculo.empleado
      
       	mesFin=(periodo.obtenerMes(bajaFecha)+1).toString().padLeft(2,"0") 
      
      //INDEMNIZACIONES
      		//NO
      ingresosTotPAgParc=0
      montoDiarioJubi=0
      noPagoUnicoJubi=0
      totalPagoUnaExhib=0
      numeroDias=0
      ingresosExeNo=0
      ingresosGravNo=0
      ingresosAcum=0
      ingresosNoAcum=0
      inpuestoRetenido=0
      
      		//SI
        
        
                                                                                        
      
        
      aosDeServicio=((bajaFecha-calculo.empleado.alta)/365).setScale(0, RoundingMode.HALF_EVEN)
      
      
       ingresosAcuUltimoSueldoMen= (calculo.salario*30).setScale(0, RoundingMode.HALF_EVEN)
        
        
        
       def tarifa=TarifaIsr.findAll("from TarifaIsr t where  t.limiteInferior < ? and t.limiteSuperior >= ? and t.ejercicio=?  ",[ingresosAcuUltimoSueldoMen,ingresosAcuUltimoSueldoMen,ejercicio])
        
        if(tarifa){
      		impuestoCorrespUltSueldoMen = ingresosAcuUltimoSueldoMen-tarifa.limiteInferior
          	impuestoCorrespUltSueldoMen*= tarifa.porcentaje
          	impuestoCorrespUltSueldoMen/=100
          	impuestoCorrespUltSueldoMen+=tarifa.cuotaFija
            impuestoCorrespUltSueldoMen=impuestoCorrespUltSueldoMen.setScale(0,RoundingMode.HALF_EVEN)
        }
        
        
     
          
        
      
        
      ingresosNoAcumulables=(ingresosGrav-ingresosAcuUltimoSueldoMen)>0? (ingresosGrav-ingresosAcuUltimoSueldoMen).setScale(0, RoundingMode.HALF_EVEN) :0 
        
      
        
        def tarifaIng=TarifaIsr.findAll("from TarifaIsr t where  t.limiteInferior < ? and t.limiteSuperior >= ? and t.ejercicio=?  ",[(BigDecimal)ingresosGrav,(BigDecimal)ingresosGrav,ejercicio])
        
        if(tarifaIng){
      		impuestoRetenido = ingresosGrav-tarifa.limiteInferior
          	impuestoRetenido*= tarifa.porcentaje
          	impuestoRetenido/=100
          	impuestoRetenido+=tarifa.cuotaFija
            impuestoRetenido=impuestoRetenido.setScale(0,RoundingMode.HALF_EVEN)
        }
      
    
    
      //SUELDOS
      
      calculoAnual=calculo.calculoAnual?"1":"2"
     
     sueldosGra=(calculo.sueldo+calculo.vacaciones+calculo.vacacionesPagadas+calculo.comisiones+calculo.permisoPorPaternidad-calculo.retardos).setScale(0, RoundingMode.HALF_EVEN)
     sueldosExc=0
     gratificacionAnualGra=calculo.aguinaldoGravable.setScale(0, RoundingMode.HALF_EVEN)
     gratificacionAnualExc=calculo.aguinaldoExento.setScale(0, RoundingMode.HALF_EVEN)
     viaticosGra=0
     viaticosExc=0
     tiempExtraGra=(calculo.tiempoExtraDobleGravado+calculo.tiempoExtraTripleGravado).setScale(0, RoundingMode.HALF_EVEN)
     tiempoExtraExc=calculo.tiempoExtraDobleExento.setScale(0, RoundingMode.HALF_EVEN)
     primaVacGra=calculo.primaVacacionalGravada.setScale(0, RoundingMode.HALF_EVEN)
     primaVacExc=calculo.primaVacacionalExenta.setScale(0, RoundingMode.HALF_EVEN)
     primaDomGra=calculo.primaDominicalGravada.setScale(0, RoundingMode.HALF_EVEN)
     primaDomExc=calculo.primaDominicalExenta.setScale(0, RoundingMode.HALF_EVEN)
     ptuGra=calculo.ptuGravada.setScale(0, RoundingMode.HALF_EVEN)
     ptuExc=calculo.ptuExenta.setScale(0, RoundingMode.HALF_EVEN)
     gastosMedGra=0
     gastosMedExc=0
     fondoDeAhorroGra=0
     fondoDeAhorroExc=0
     cajaDeAhorroGra=0
     cajaDeAhorroExc=0
     valesDespensaGra=0
     valesDespensaExc=0
     gastosFuneralesGra=0
     gastosFuneralesExc=0
     contribucionesTrabGra=0
     contribucionesTrabExc=0
     premioPuntGra=0
     premioPuntExc=0
     primaSeguroVidaGra=0
     primaSeguroVidaExc=0
     gastosMedMayGra=0
     gastosMedMayExc=0
     valesRestGra=0
     valesRestExc=0
     valesGasolinaGra=0
     valesGasolinaExc=0
     valesRopaGra=0
     valesRopaExc=0
     ayudaRentaGra=0
     ayudaRentaExc=0
     ayudaArtEscGra=0
     ayudaArtEscExc=0
     ayudaAnteojosGra=0
     ayudaAnteojosExc=0
     ayudaTransporteGra=0
     ayudaTransporteExc=0
     cuotasSindicalesGra=0
     cuotasSindicalesExc=0
     subsIncapGra=0
     subsIncapExc=0
     becasGra=0
     becasExc=0
     pagosOtrosGra=0
     pagosOtrosExc=0
     otrosIngreGra=(calculo.incentivo+calculo.bonoDeProductividad+calculo.bonoPorDesempeno+calculo.compensacion+calculo.bono+calculo.bonoAntiguedad).setScale(0, RoundingMode.HALF_EVEN)
     otrosIngreExc=0
     sumaIngreSalGra=calculo.totalGravado.setScale(0, RoundingMode.HALF_EVEN)
     sumaIngreSalExc=calculo.totalExento.setScale(0, RoundingMode.HALF_EVEN)
     /*Se resta ISR de bajas de ISR de Sueldo de bajas*/
     impuestoRet=(calculo.ISR.setScale(0, RoundingMode.HALF_EVEN))-impuestoRetenido
     impuestoRetOtros=0
     //saf=calculo.resultado.setScale(0, RoundingMode.HALF_EVEN)>0 ? calculo.resultado.setScale(0, RoundingMode.HALF_EVEN) :0
     saf=0
  
	def calculoAnt=CalculoAnual.findByEjercicioAndEmpleado(calculo.ejercicio-1,calculo.empleado)  
  
     safAnt= 0 //calculoAnt? (calculoAnt.calculoAnual ? (calculoAnt.resultado-calculo.aplicado).setScale(0, RoundingMode.HALF_EVEN) : 0)  :0
  
     sumaCreditoTrab=0
     credSalEntregado=0
  
     totalIngresosPrestPrevSocial=0
     ingresosExcPrestPrevSocial=0
  
     sumaSueldos=calculo.total.setScale(0, RoundingMode.HALF_EVEN)
     impuestoLocalIngresos=0
     subsEmpleoTrab=calculo.subsEmpPagado.setScale(0, RoundingMode.HALF_EVEN)
      
      
      
      
      
      
     ///IMPUESTO SOBRE LA RENTA RESUMEN
    
     def aportacionesVoDed=0
     def ISRTarifAnual=calculo.calculoAnual?calculo.impuestoDelEjercicio.setScale(0, RoundingMode.HALF_EVEN):0
  	 def subsidioAcreditable=0
     def subsidioNoAcreditable=0
     def impuestoSobreIngreAcu=0
     def impuestoSobreIngreNoAcu=0
     def impuestoLocalASueldos=0
     def subsidioEmpleoEjercicio=calculo.subsEmpAplicado.setScale(0, RoundingMode.HALF_EVEN)
       
       
     def registro=mesIni+p+mesFin+p +rfc+p+curp+p+apellidoPaterno+p+apellidoMaterno+p+nombres+p+areaGeo+p+calculoAnual+p+tarifaUtilizada+p+tarifaUtiAct+p+proporcionDeSubsApl+p+sindicalizado+p+asimiladosASal+p+claveEntidad+
       p+p+p+p+p+p+p+p+p+p+p+
       montoAportaciones+p+aplicoMonto+p+montoAportacionesDed+p+montoAportacionesDedPat+p+pagosPorSeparacion+p+asimilados+p+pagosPatron+p+
       //INDEMNIZACIONES
       ingresosTotPAgParc+p+montoDiarioJubi+p+noPagoUnicoJubi+p+totalPagoUnaExhib+p+numeroDias+p+ingresosExeNo+p+ingresosGravNo+p+ingresosAcum+p+
       ingresosNoAcum+p+inpuestoRetenido+p+totalPagOtrosSep+p+aosDeServicio+p+ingresosExe+p+ingresosGrav+p+ingresosAcuUltimoSueldoMen+p+impuestoCorrespUltSueldoMen+p+ingresosNoAcumulables+p+
       impuestoRetenido+p+
       //SUELDOS
       sueldosGra+p+sueldosExc+p+gratificacionAnualGra+p+gratificacionAnualExc+p+viaticosGra+p+viaticosExc+p+tiempExtraGra+p+tiempoExtraExc+p+
        primaVacGra+p+primaVacExc+p+primaDomGra+p+primaDomExc+p+ptuGra+p+ptuExc+p+gastosMedGra+p+gastosMedExc+p+fondoDeAhorroGra+p+
        fondoDeAhorroExc+p+cajaDeAhorroGra+p+cajaDeAhorroExc+p+valesDespensaGra+p+valesDespensaExc+p+gastosFuneralesGra+p+gastosFuneralesExc+p+
        contribucionesTrabGra+p+contribucionesTrabExc+p+premioPuntGra+p+premioPuntExc+p+primaSeguroVidaGra+p+primaSeguroVidaExc+p+gastosMedMayGra+p+
        gastosMedMayExc+p+valesRestGra+p+valesRestExc+p+valesGasolinaGra+p+valesGasolinaExc+p+valesRopaGra+p+valesRopaExc+p+ayudaRentaGra+p+
        ayudaRentaExc+p+ayudaArtEscGra+p+ayudaArtEscExc+p+ayudaAnteojosGra+p+ayudaAnteojosExc+p+ayudaTransporteGra+p+ayudaTransporteExc+p+
        cuotasSindicalesGra+p+cuotasSindicalesExc+p+subsIncapGra+p+subsIncapExc+p+becasGra+p+becasExc+p+pagosOtrosGra+p+ pagosOtrosExc+p+
        otrosIngreGra+p+otrosIngreExc+p+sumaIngreSalGra+p+sumaIngreSalExc+p+impuestoRet+p+impuestoRetOtros+p+saf+p+safAnt+p+sumaCreditoTrab+p+
        credSalEntregado+p+totalIngresosPrestPrevSocial+p+ingresosExcPrestPrevSocial+p+sumaSueldos+p+impuestoLocalIngresos+p+subsEmpleoTrab+p+
       //IMPUESTO
       aportacionesVoDed+p+ISRTarifAnual+p+subsidioAcreditable+p+subsidioNoAcreditable+p+impuestoSobreIngreAcu+p+impuestoSobreIngreNoAcu+p+
       impuestoLocalASueldos+p+subsidioEmpleoEjercicio+p
       
    

      
    registros=registros+1
    
    
   append(registro+"\r\n","UTF8")
     
     }
    
   
  }
   
  	
 
  }
	}
	String name="DIM Pagos Por Separacion"+new Date().format("dd_MM_yyyy")+".txt"
	response.setContentType("application/octet-stream")
	response.setHeader("Content-disposition", "attachment; filename=\"$name\"")
	response.outputStream << temp.newInputStream()
}


def reporteDeIncapacidades(){
	[reportCommand:new PeriodoCommand()]
}


def reporteDiasIncapacidad(){
	[reportCommand:new PeriodoCommand()]
}

def diasIncapacidad(PeriodoCommand command){
		if(request.method=='GET'){
			return [reportCommand:new PeriodoCommand()]
		}
		command.validate()
		if(command.hasErrors()){
			log.info 'Errores de validacion al ejecurar reporte'
			render view:'incapacidades',model:[reportCommand:command]
			return
		}
		def repParams=[:]
		repParams['FECHA_INICIAL']=command.fechaInicial
		repParams['FECHA_FINAL']=command.fechaFinal
		repParams.reportName=params.reportName?:'FaltaNombre Del Reporte'
		ByteArrayOutputStream  pdfStream=runReport(repParams)
		render(file: pdfStream.toByteArray(), contentType: 'application/pdf'
			,fileName:repParams.reportName+".pdf")
	}


def incapacidadesSuaDet(){
	[reportCommand:new PeriodoCommand()]
}

def generarIncapacidadesSuaDet(PeriodoCommand command){
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
    def tipoMov="4"
    def fechaI=df.format(calculo.fechaInicial)
    def fechaF=df.format(calculo.fechaFinal)
    def folioInc= calculo.referenciaImms.padLeft(8)
    def diasInc= (calculo.fechaFinal-calculo.fechaInicial+1).toString().padLeft(3,"0")
    def porcentajeInc=calculo.porcentaje?calculo.porcentaje.toString().padLeft(3,"0"):'000'
    def ramaInc=calculo.tipo.clave
    def tipoRiesgo=calculo.tipoRiesgo=='ACCIDENTE DE TRABAJO'?1:calculo.tipoRiesgo=='ACCIDENTE TRAYECTO'?2:calculo.tipoRiesgo=='ENFERMEDAD PROFESIONAL'?3:'0'
    def secuela=calculo.secuela=='NINGUNA' ? 1 : calculo.secuela=='INCAPACIDAD TEMPORAL' ? 2 :calculo.secuela=='VALUACION INICIAL PROVISIONAL' ? 3 : calculo.secuela=='VALUACION INICIAL DEFINITIVA' ? 4 :calculo.secuela== 'DEFUNCION' ? 5 : calculo.secuela== 'RECAIDA' ? 6 : calculo.secuela=='VALUACION POST ALTA' ? 7 : '0'
    def control= calculo.control=='UNICA' ? 1 :calculo.control=='INICIAL'? 2 : calculo.control=='SUBSECUENTE' ? 3 : calculo.control=='ALTA O ST-2' ? 4 : calculo.control== 'PRENATAL' ? 5 : calculo.control== 'ENLACE' ? 6 : calculo.control=='POSTNATAL' ? 7 : '0' 
   
   // println calculo.empleado.id+"-"+calculo.empleado.status+"--"+registroPatronal+numSeguridadSocial+tipoMov+fechaI+folioInc+diasInc+sdiOAp+"fin"
    println registroPatronal+"-"+numSeguridadSocial+"-"+tipoMov+"-"+fechaI+"-"+folioInc+"-"+diasInc+"-"+porcentajeInc+"-"+ramaInc+"-"+tipoRiesgo+"-"+secuela+"-"+control+"-"+fechaF
	append(registroPatronal+numSeguridadSocial+tipoMov+fechaI+folioInc+diasInc+porcentajeInc+ramaInc+tipoRiesgo+secuela+control+fechaF+"\r\n")

  }

  	}

  	String name="Incapacidades"+new Date().format("dd_MM_yyyy")+".txt"
	response.setContentType("application/octet-stream")
	response.setHeader("Content-disposition", "attachment; filename=\"$name\"")
	response.outputStream << temp.newInputStream()
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









}




@Validateable
class NominaCommand{
	Nomina nomina

	static constraints={
		nomina nullable:false
	}
}
