package com.luxsoft.sw4.rh


import com.luxsoft.sw4.Empresa
import grails.plugin.springsecurity.annotation.Secured;
import grails.transaction.Transactional
import grails.validation.Validateable
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import org.apache.commons.lang.StringUtils
import java.io.BufferedWriter
import com.luxsoft.sw4.*

@Secured(['ROLE_ADMIN','RH_USER'])
@Transactional(readOnly = true)
class ExportadorController {

    def index() { }


    def nominaBanamex(){
    	
    }

    def generarNominaBanamex(Nomina nomina){
    	def temp = File.createTempFile('temp', '.txt') 
		
		temp.with{
			Empresa emp=Empresa.first()
			Nomina n=nomina

			SimpleDateFormat df = new SimpleDateFormat("ddMMyy")

			// Creacion del registro de control
			def registroControl

			def tipoRegistro=1
			def numIdentCte="000005139464"
			def fechaPago= df.format(n.getPago())
			def secuenciaArch="0001"
			def nombreEmpresa=emp.getNombre().padRight(36)
			def descripcion=StringUtils.rightPad("Pago de nomina",20)
			def naturaleza ="05"
			def instrucciones=StringUtils.leftPad("",40)
			def version="B"
			def volumen="0"
			def caracteristicas="0"

			registroControl= tipoRegistro+numIdentCte+fechaPago+secuenciaArch+nombreEmpresa+descripcion+naturaleza+instrucciones+version+volumen+caracteristicas
			append(registroControl+"\r\n")

			// Creacion del registro global

			def registroGlobal

			def tipoReg="2"
			def tipoOp="1"
			def claveMoneda="001"
			def formato = new DecimalFormat("###")
			int importe	= Math.floor(n.total)


			def importeCargos =formato.format(importe).padLeft(16,"0")
			def formatoDec = new DecimalFormat(".##")
			def decimalCargo=formatoDec.format(n.total-importe).replace('.','').padRight(2,"0")

			def importeCargo=importeCargos+decimalCargo
			def tipoCta="01"
			def claveSuc="0270"
			def numCta="00000000000001858193"
			def blanco=StringUtils.leftPad("",20)

			registroGlobal=tipoReg+tipoOp+claveMoneda+importeCargo+tipoCta+claveSuc+numCta+blanco
			append(registroGlobal+"\r\n")

			def numAbonos=0
			n.partidas.each{

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
				
				def numCtaInd=it.empleado.salario.clabe.padLeft(20,"0")
				def referencia="0000000001".padRight(40)
				def beneficiario=it.empleado.nombre.replace("Ñ","N").padRight(55)
				def blanco1=StringUtils.leftPad("",40)
				def blanco2=StringUtils.leftPad("",24)
				def ultimo="0000000000"

				registroIndividual=tipoRegInd+tipoOpInd+claveMoneda+importeAbono+tipoCtaInd+claveSucInd+numCtaInd+referencia+beneficiario+blanco1+blanco2+ultimo
				append(registroIndividual+"\r\n")
				numAbonos=numAbonos+1
			}

			// Creacion de registro totales

			def registroTotales
			def tipoRegTotal="4"
			def abonos=numAbonos.toString().padLeft(6,"0")
			def numCargos="000001"

			registroTotales=tipoRegTotal+claveMoneda+abonos+importeCargo+numCargos+importeCargo
			append(registroTotales+"\r\n")


			println absolutePath
		}
		
	  
		String name="NominaBanamex_"+"$nomina.ejercicio"+"_$nomina.tipo"+"_$nomina.periodicidad"+"_$nomina.folio"+".txt"
		response.setContentType("application/octet-stream")
		response.setHeader("Content-disposition", "attachment; filename=\"$name\"")
		response.outputStream << temp.newInputStream()	
    	
    }
	
	
	def modificacionesImss(){
		[reportCommand:new EjercicioBimestreCommand()]
	}
	
	def generarModificacionesImss(EjercicioBimestreCommand command){
		File temp = File.createTempFile("temp",".txt")
		
		temp.with {

		  Empresa emp=Empresa.first()
		def registroPatronal=emp.registroPatronal
		def numeroDeMovs=0
		
		def calculosSdi=CalculoSdi.findAllByEjercicioAndBimestre(command.ejercicio,command.bimestre).each{calculo ->
		 
		  
		  if(calculo.sdiInf!=0.0){
		  
			numeroDeMovs=numeroDeMovs+1
			
		  def numeroSeguridadSocial= SeguridadSocial.findByEmpleado(calculo.empleado).numero.replace('-','')
		  def apellidoPaterno=calculo.empleado.apellidoPaterno ? calculo.empleado.apellidoPaterno.padRight(27) : calculo.empleado.apellidoMaterno.padRight(27)
		  def apellidoMaterno=calculo.empleado.apellidoPaterno ? calculo.empleado.apellidoMaterno.padRight(27) : StringUtils.leftPad("",27)
		  def nombres= calculo.empleado.nombres ? calculo.empleado.nombres.padRight(27) : StringUtils.leftPad("",27)
		  def salarioBase=calculo.sdiInf.toString().replace('.','').padLeft(6,"0")
		  def filler= StringUtils.leftPad("",6)
		  def tipoTrabajador="1"
		  def tipoSalario="2"
			if(calculo.empleado.id==273 || calculo.empleado.id==274)
			tipoSalario=1
		  def tipoJornada=0
			if(calculo.empleado.perfil.jornada=="MEDIA" || calculo.empleado.perfil.jornada=="REDUCIDA")
			 tipoJornada=6
		  def fechaMov="01012015"
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
	  def apellidoPaterno=calculo.empleado.apellidoPaterno ? calculo.empleado.apellidoPaterno.padRight(27) : StringUtils.leftPad("",27)
	  def apellidoMaterno=calculo.empleado.apellidoMaterno ? calculo.empleado.apellidoMaterno.padRight(27) : StringUtils.leftPad("",27)
	  def nombres= calculo.empleado.nombres ? calculo.empleado.nombres.padRight(27) : StringUtils.leftPad("",27)
	  def salarioBase= calculo.sdiNuevo.toString().replace('.','').padLeft(6,"0")
	  def filler= StringUtils.leftPad("",6)
	  def tipoTrabajador="1"
	  def tipoSalario="0"
	  
	  def tipoJornada="0"
		if(calculo.empleado.perfil.jornada=="MEDIA" || calculo.empleado.perfil.jornada=="REDUCIDA")
		 tipoJornada=6
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
	  def apellidoPaterno=calculo.empleado.apellidoPaterno ? calculo.empleado.apellidoPaterno.padRight(27) : StringUtils.leftPad("",27)
	  def apellidoMaterno=calculo.empleado.apellidoMaterno ? calculo.empleado.apellidoMaterno.padRight(27) : StringUtils.leftPad("",27)
	  def nombres= calculo.empleado.nombres ? calculo.empleado.nombres.padRight(27) : StringUtils.leftPad("",27)
	  def salarioBase= calculo.empleado.salario.salarioDiarioIntegrado.toString().replace('.','').padLeft(6,"0") //calculo.sdiNuevo.toString().replace('.','').padLeft(6,"0")
	  def filler= StringUtils.leftPad("",6)
	  def tipoTrabajador="1"
	  def fillerBlanco= StringUtils.leftPad("",2)
	  def fechaMov=df.format(calculo.fecha)
	  def unidadMedica= StringUtils.leftPad("",3) 
	  def filler2=StringUtils.leftPad("",2)
	  def tipoMov="02"
	  def guia="11400"
	  def claveTrab= StringUtils.leftPad("",10) 
	  def filler3=StringUtils.leftPad("",1)
	  def curp= StringUtils.leftPad("",18) 
	  def identificador="9"
	 
	   def registro=registroPatronal+numeroSeguridadSocial+apellidoPaterno+apellidoMaterno+nombres+salarioBase+filler+tipoTrabajador+fechaMov+unidadMedica+filler2+tipoMov+guia+claveTrab+filler3+curp+identificador
	   append(registro+"\r\n","UTF8")
	  
	  
	}
  }
	String name="BajasIMSS_"+new Date().format("dd_MM_yyyy")+".txt"
	response.setContentType("application/octet-stream")
	response.setHeader("Content-disposition", "attachment; filename=\"$name\"")
	response.outputStream << temp.newInputStream()
}	
	
}



@Validateable
class NominaCommand{
	Nomina nomina

	static constraints={
		nomina nullable:false
	}
}
