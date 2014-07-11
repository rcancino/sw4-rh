package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import grails.transaction.NotTransactional

import com.luxsoft.sw4.rh.Checado
import com.luxsoft.sw4.Periodo

@Transactional
class ChecadoService {

	def grailsApplication

    @NotTransactional
    def importarLecturas(Periodo periodo){
		log.info 'Importando lectoras para el periodo: '+periodo
    	for(date in periodo.fechaInicial..periodo.fechaFinal){
    		importarLecturas(date)
    	}
    	
    }

    @NotTransactional
    def importarLecturas(Date date){
		log.info 'Importando lectoras para : '+date

		def sdate=date.format('yyyyMMdd')
    		
		def rawdata=grailsApplication.config.sw4.rh.asistencia.rawdata
		File file =new File(rawdata+"/"+sdate+".chk")
		log.info 'Rawdata: '+file.path+' Exists: '+file.exists()
    	
		if(file.exists()){
			//FileUtils.copyFileToDirectory(file,new File("c:/basura/rawdata"))
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
				def hora=Date.parse('HHmmss',fields[0])
				println "registrando evento empleado: ${fields[2]} hora:$hora  Fecha:$fecha"
				def r=Checado.findOrSaveWhere(lector:lector,fecha:fecha,hora:hora,numeroDeEmpleado:fields[2])
				//def r=new Checado(lector:lector,fecha:fecha,hora:hora,numeroDeEmpleado:fields[2])
				//r.save(failOnError:true)
			  }
			 
			  
			}
		}
    	
    }
}
