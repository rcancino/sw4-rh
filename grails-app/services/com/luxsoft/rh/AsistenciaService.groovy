package com.luxsoft.rh

import java.sql.Time;

import grails.transaction.Transactional

import com.luxsoft.sw4.Periodo
import com.luxsoft.sw4.rh.Asistencia;
import com.luxsoft.sw4.rh.Checado
import com.luxsoft.sw4.rh.Empleado

@Transactional
class AsistenciaService {

	def grailsApplication

    def importarLecturas(Periodo periodo){
    	for(date in periodo.fechaInicial..periodo.fechaFinal){
    		def sdate=date.format('yyyyMMdd')
    		
			def rowdata=grailsApplication.config.sw4.rh.asistencia.rowdata
			File file =new File(rowdata+"/"+sdate+".chk")
    		//File file=grailsApplication.mainContext.getResource("/WEB-INF/data/"+sdate+'.chk').file
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
    				//def r=Checado.findOrSaveWhere(lector:lector,fecha:fecha,hora:hora,numeroDeEmpleado:fields[2])
    				def r=new Checado(lector:lector,fecha:fecha,hora:hora,numeroDeEmpleado:fields[2])
    				r.save(failOnError:true)
    			  }
    			 
    			  
    			}
    		}
    	}
    	
    }

    def registrarAsistencias(Periodo periodo) {

		//numero magico
		def tolerancia1=(60*1000*10)
		
		for(date in periodo.fechaInicial..periodo.fechaFinal){
			def lecturas=Checado.findAll(sort:"numeroDeEmpleado"){fecha==date}
			def map=lecturas.groupBy{it.numeroDeEmpleado}
			map.each{ key,val->
				
				def empleado=Empleado.find("from Empleado e where e.perfil.numeroDeTrabajador=?",[key])
				if(!empleado) {
					log.info 'Empleado no registrado: '+key
					
				}
				println 'Procesando: '+key+' '+empleado
				val.sort(){ c->
					c.hora
				}
				def last=null
				def valid =[]
				val.each{ reg->
					if(!last)last=reg.hora
					def dif=reg.hora.time-last.time
					if(dif>tolerancia1 || dif==0){
						//println "$reg.lector $reg.hora"
						last=reg.hora
						valid.add(reg)
					}
				}
				def asistencia=Asistencia.findWhere(empleado:empleado,fecha:date)
				if(asistencia==null)
					asistencia=new Asistencia(empleado:empleado,fecha:date)
				for(def i=0;i<valid.size;i++) {
					def checado=valid[i]
					def time=new Time(checado.hora.time)
					switch(i) {
						case 0:
							
							asistencia.entrada1=time
							break
						case 1:
							asistencia.salida1=time
							break
						case 2:
							asistencia.entrada2=time
							break
						case 3:
							asistencia.salida2=time
							break
						default:
							break
					}
				}
				if(asistencia.empleado)	
					asistencia.save(failOnError:true)
			}
		}
    }
}
