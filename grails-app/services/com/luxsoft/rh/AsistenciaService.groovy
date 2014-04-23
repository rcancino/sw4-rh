package com.luxsoft.rh

import grails.transaction.Transactional
import com.luxsoft.sw4.Periodo
import com.luxsoft.sw4.rh.Checado

@Transactional
class AsistenciaService {

	def grailsApplication

    def importarLecturas(Periodo periodo){
    	for(date in periodo.fechaInicial..periodo.fechaFinal){
    		def sdate=date.format('yyyyMMdd')
    		
    		File file=grailsApplication.mainContext.getResource("/WEB-INF/data/"+sdate+'.chk').file
    		if(file.exists()){
    			log.info 'Importando lecturas para: '+sdate
    			log.info 'Importando desde: '+file.name
    			int lector
    			def fecha
    			file.eachLine{line,row ->
    			  def fields=line.split(',')
    			  
    			  if(fields.length==2){
    				lector=fields[0].toInteger()
    				println 'Lector: '+lector
    			  }else if(fields.length==4){
    				fecha=Date.parse('yyyyMMdd',fields[2])
    				  println 'Fecha: '+fecha
    			  }else{
    				//
    				def hora=Date.parse('hhmmss',fields[0])
    				println "registrando evento empleado: ${fields[2]} hora:$hora  Fecha:$fecha"
    				def r=Checado.findOrSaveWhere(lector:lector,fecha:fecha,hora:hora,numeroDeEmpleado:fields[2])
    				//def r=new Checado(lector:lector,fecha:fecha,hora:hora,numeroDeEmpleado:fields[2])
    				//r.save(failOnError:true)
    			  }
    			 
    			  
    			}
    		}
    	}
    	
    }

    def registrarAsistencias(Periodo periodo) {
		
    }
}
