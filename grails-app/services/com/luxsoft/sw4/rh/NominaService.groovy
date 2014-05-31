package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Empresa;
import com.luxsoft.sw4.Periodo;

import grails.transaction.Transactional
import groovy.time.TimeCategory
import grails.events.Listener

@Transactional
class NominaService {
	
	def cfdiService

	def eliminarNomina(Long id){
		def nomina=Nomina.get(id)
		//nominaInstance.attach()
		nomina.delete()
	}

    def generarNominas(String tipo,String periodicidad) {
		
		def year=Calendar.getInstance().get(Calendar.YEAR)
		
		
		switch(periodicidad){
			
			case 'QUINCENAL':
				def periodo=Periodo.getPeriodoAnual(year)
				Date fecha=periodo.fechaInicial
				Integer folio=1
				(0..11).each{ 
					def mes=Periodo.getPeriodoEnUnMes(it,year)
					def q1=new Periodo(fechaInicial:mes.fechaInicial,fechaFinal:mes.fechaInicial+14)
					log.info "Generando nomina quincenale periodo: "+q1
					generarNomina(tipo,periodicidad,q1,folio++)
					def q2=new Periodo(fechaInicial:q1.fechaFinal+1,fechaFinal:mes.fechaFinal)
					log.info "Generando nomina quincenale periodo: "+q2
					generarNomina(tipo,periodicidad,q2,folio++)
					
				}
				break
			case 'SEMANAL':
				def periodo=Periodo.getPeriodoAnual(year)
				log.info 'Generando nominas semanales periodo: '+periodo
				Date fecha=periodo.fechaInicial
				
				def folio=1
				
				while(fecha<periodo.fechaFinal){
					def fechaFinal=fecha+7
					if(folio==1)
						fechaFinal=Date.parse('dd/MM/yyyy','05/01/2014')
					def per=new Periodo(fechaInicial:fecha+1,fechaFinal:fechaFinal)
					
					
					
					if(folio==52){
						per.fechaFinal=periodo.fechaFinal
						log.info "ULITMA SEMANA Semana ${folio} "+per
						generarNomina(tipo,periodicidad,per,folio++)
						break
					}
					log.info "Semana ${folio} "+per
					generarNomina(tipo,periodicidad,per,folio++)
					
					fecha=fechaFinal
					//folio++
				}
				break
			default:
				break
		}
    }
	
	private Nomina generarNomina(String tipo,String periodicidad,Periodo periodo,Integer folio){
		
		Empresa empresa=Empresa.first()
		assert empresa,'No existe la empresa del sistema'
		def nomina=Nomina.findWhere(empresa:empresa,tipo:tipo,periodicidad:periodicidad,folio:folio)
		if(!nomina){
			//println 'Nomina ya existente.....+'+nomina
			nomina=new Nomina(tipo:tipo,periodicidad:periodicidad,folio:folio)
			nomina.status="PENDIENTE"
			nomina.periodo=periodo
			nomina.pago=periodo.fechaFinal
			nomina.diaDePago='JUEVES'
			nomina.formaDePago='TRANSFERENCIA'
			nomina.empresa=empresa
			nomina.total=0.0
			nomina.save(failOnError:true)
			
			return nomina
		}
	}

	@Listener(namespace='gorm')
	def beforeDelete(Nomina nomina){
		validarParaModificacion(nomina)
	}

	void validarParaModificacion(Nomina nomina){
		log.info 'Validando la eliminacion de la nomina: '+nomina
		if(nomina.status=='CERRADA')
			throw new NominaException(message:"La nomina ${nomina.id} ya esta cerrada no se puede eliminar",nomina:nomina)
		nomina.partidas.each{
			if(it.cfdi){
				throw new NominaException(message:"La nomina ${nomina.id} ya tiene partidas timbradas",nomina:nomina)
			}
		}
	}
	
	def timbrar(Long nominaId) {
		def nomina =Nomina.get(nominaId)
		cfdiService.cfdiTimbrador.timbradoDePrueba=false
		if(nomina.status=='CERRADA') {
			throw new NominaException(message:"Nomina cerrada no se puede timbrar",nomina:nomina)
		}
		
		for(NominaPorEmpleado ne:nomina.partidas){
			try{
			  if(ne.cfdi==null){
				log.info 'Timbrando Ne id:'+ne.id
				def res=cfdiService.generarComprobante(ne.id)
			  }  
			}catch(Exception ex){
				log.error 'Error timbrando '+ExceptionUtils.getRootCauseMessage(ex)
				
			}
		}
	  nomina.status='CERRADA'
	}
}

class NominaException extends RuntimeException{
	String message
	Nomina nomina
}
