package com.luxsoft.sw4.rh


import com.luxsoft.sw4.Empresa
import grails.plugin.springsecurity.annotation.Secured;
import grails.transaction.Transactional
import grails.validation.Validateable
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import org.apache.commons.lang.StringUtils

@Secured(['ROLE_ADMIN','RH_USER'])
@Transactional(readOnly = true)
class ExportadorController {

    def index() { }


    def nominaBanamex(){
    	
    }

    def generarNominaBanamex(Nomina nomina){
    	 //def temp = File.createTempFile('temp', '.txt') 
		//temp.write('Prueba de Nomina banamex: '+nomina.toString())
		File.createTempFile("temp",".tmp").with {
      // Include the line below if you want the file to be automatically deleted when the 
     // JVM exits
    // deleteOnExit()
Empresa emp=Empresa.first()
Nomina n=Nomina.get(296)

SimpleDateFormat df = new SimpleDateFormat("ddMMyy")

  // Creacion del registro de contol
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
	append(registroControl+"\n")
  
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
  	append(registroGlobal+"\n")
	
  	def numAbonos=0
	n.partidas.each{
      
      // Creacion de registro abono individual
       if(it.empleado.id==280 || it.empleado.id==260 || it.empleado.id==246 || it.empleado.id==245)

      
     def registroIndividual
     def tipoRegInd="3"
     def tipoOpInd="0"
     int importeInd=Math.floor(it.total)
     def importeAbonos =formato.format(importeInd).padLeft(16,"0");
     def decimalAbono=formatoDec.format(it.total-importeInd).replace('.','').padRight(2,"0")
     def importeAbono= importeAbonos+""+decimalAbono
     def tipoCtaInd="03"
     def claveSucInd="0270"
     def numCtaInd=it.empleado.salario.clabe.padLeft(20,"0")
     def referencia="0000000001".padRight(40)
     def beneficiario=it.empleado.nombre.replace("Ñ","N").padRight(55)
     def blanco1=StringUtils.leftPad("",40)
     def blanco2=StringUtils.leftPad("",24)
     def ultimo="0000000000"
     
      def registroIndividual=tipoRegInd+tipoOpInd+claveMoneda+importeAbono+tipoCtaInd+claveSucInd+numCtaInd+referencia+beneficiario+blanco1+blanco2+ultimo
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
}

@Validateable
class NominaCommand{
	Nomina nomina

	static constraints={
		nomina nullable:false
	}
}
