package com.luxsoft.sw4.rh


import grails.plugin.springsecurity.annotation.Secured;
import grails.transaction.Transactional
import grails.validation.Validateable

@Secured(['ROLE_ADMIN','RH_USER'])
@Transactional(readOnly = true)
class ExportadorController {

    def index() { }


    def nominaBanamex(){
    	
    }

    def generarNominaBanamex(Nomina nomina){
    	def temp = File.createTempFile('temp', '.txt') 
		temp.write('Prueba de Nomina banamex: '+nomina.toString())
		
		  
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
