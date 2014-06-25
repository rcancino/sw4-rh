package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Empresa;
import com.luxsoft.sw4.Periodo;

import grails.transaction.Transactional
import groovy.time.TimeCategory
import grails.events.Listener



class NominaService {
	
	def cfdiService
	
	def procesadorDeNomina
	
	@Transactional
	def eliminarNomina(Long id){
		def nomina=Nomina.get(id)
		//nominaInstance.attach()
		nomina.delete()
	}

	@Transactional
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
		nomina.diaDePago=calendarioDet.fechaDePago.format('EEEE')
		nomina.formaDePago=formaDePago
		nomina.empresa=empresa
		nomina.total=0.0
		generarPartidas nomina
		nomina.save(failOnError:true)
		return nomina
	}
	
	@Transactional
	def generarPartidas(Nomina nomina) {
		
		def tipo=nomina.periodicidad
		def empleados=Empleado.findAll(sort:"apellidoPaterno"){salario.periodicidad==tipo }
		//println "Generando nomina por empleado $tipo para ${empleados.size()} empleados"
		for(def empleado:empleados) {
			if(empleado.baja && empleado.baja.fecha<nomina.calendarioDet.asistencia.fechaInicial) {
				continue
			}
			NominaPorEmpleado ne=nomina.partidas.find{
				it.empleado.id==empleado.id
			}
			if(!ne){
				println 'Agregando empleado: '+empleado
				ne=new NominaPorEmpleado(
					empleado:empleado,
					ubicacion:empleado.perfil.ubicacion,
					antiguedadEnSemanas:0,
					nomina:nomina,
					vacaciones:0,
					fraccionDescanso:0
					)
				ne.antiguedadEnSemanas=ne.getAntiguedad()
				def res=nomina.addToPartidas(ne)
				//nomina.save failOnError:true
			}
			//Actualizar asistencia
			def asistencia=Asistencia.find{calendarioDet==nomina.calendarioDet && empleado==ne.empleado}
			ne.asistencia=asistencia
		}
		return nomina
	}
	
	
	def actualizarPartidas(Nomina nomina) {
		assert nomina,"Nomina nula no es valido"
		validarParaModificacion(nomina)
		generarPartidas(nomina)
		nomina=procesadorDeNomina.procesar(nomina)
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
		timbrar(ne)
	}
	
	def timbrar(NominaPorEmpleado ne) {
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
	
	def timbrarNomina(Long nominaId) {
		def nomina =Nomina.get(nominaId)
		validarParaModificacion(nomina)
		for(NominaPorEmpleado ne:nomina.partidas){
			timbrar(ne)
		}
	  nomina.status='CERRADA'
	}
}

class NominaException extends RuntimeException{
	String message
	Nomina nomina
}
