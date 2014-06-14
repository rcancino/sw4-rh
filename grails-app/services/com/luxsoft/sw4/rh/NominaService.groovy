package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Empresa;
import com.luxsoft.sw4.Periodo;

import grails.transaction.Transactional
import groovy.time.TimeCategory
import grails.events.Listener


class NominaService {
	
	def cfdiService
	
	@Transactional
	def eliminarNomina(Long id){
		def nomina=Nomina.get(id)
		//nominaInstance.attach()
		nomina.delete()
	}

	
	def generar(Long calendarioDetId,String tipo,String formaDePago){
		println 'Generando calendario: '+calendarioDetId
		def calendarioDet=CalendarioDet.get(calendarioDetId)
		//assert(calendarioDet,'ERROR caldnario det nulo')
		def periodicidad=calendarioDet.calendario.tipo=='SEMANA'?'SEMANAL':'QUINCENAL'
		def periodo=calendarioDet.periodo()
		Empresa empresa=Empresa.first()
		def nomina=Nomina.find{calendarioDet==calendarioDet}
		if(nomina){
			throw new NominaException(message:'Nomina ya generada calendario: '+calendarioDet)
		}
		nomina=new Nomina(tipo:tipo,
			periodicidad:periodicidad,
			folio:calendarioDet.folio,
			status:"PENDIENTE",
			periodo:periodo,
			calendarioDet:calendarioDet)
		nomina.pago=calendarioDet.fechaDePago
		nomina.diaDePago=calendarioDet.fechaDePago.format('ddd')
		nomina.formaDePago=formaDePago
		nomina.empresa=empresa
		nomina.total=0.0
		nomina.save(failOnError:true)
		return nomina
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
	
	
	def timbrar(Long id){
		NominaPorEmpleado ne=NominaPorEmpleado.get(id)
		cfdiService.cfdiTimbrador.timbradoDePrueba=false
		if(ne.cfdi==null && ne.getPercepciones()>0){
			log.info 'Timbrando Ne id:'+ne.id
			try{
				cfdiService.generarComprobante(ne.id)
			}catch(Exception ex){
				ex.printStackTrace()
				log.error ex
			}
			return ne
		}
		
	}
	
	def timbrar2(Long nominaId) {
		def nomina =Nomina.get(nominaId)
		cfdiService.cfdiTimbrador.timbradoDePrueba=false
		if(nomina.status=='CERRADA') {
			throw new NominaException(message:"Nomina cerrada no se puede timbrar",nomina:nomina)
		}
		
		for(NominaPorEmpleado ne:nomina.partidas){
			
			if(ne.cfdi==null){
				log.info 'Timbrando Ne id:'+ne.id
				def res=cfdiService.generarComprobante(ne.id)
			}
			
		/*	try{
			  if(ne.cfdi==null){
				log.info 'Timbrando Ne id:'+ne.id
				def res=cfdiService.generarComprobante(ne.id)
			  }  
			}catch(Exception ex){
				
			log.error 'Error timbrando '+ExceptionUtils.getRootCauseMessage(ex)
				
			}*/
		}
	  nomina.status='CERRADA'
	}
}

class NominaException extends RuntimeException{
	String message
	Nomina nomina
}
