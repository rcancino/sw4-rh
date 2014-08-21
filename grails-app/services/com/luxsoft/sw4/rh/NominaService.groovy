package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Empresa
import com.luxsoft.sw4.Periodo
import com.luxsoft.sw4.rh.acu.AcumuladoPorConcepto

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
		def asistencias=Asistencia.findAllByCalendarioDet(nomina.calendarioDet)
		
		int orden=1
		
		for(def asistencia:asistencias) {
			
			def empleado=asistencia.empleado
			
			NominaPorEmpleado ne=nomina.partidas.find{
				it.empleado.id==empleado.id
			}
			if(!ne){
				
				log.info 'Agregando empleado: '+empleado
					ne=new NominaPorEmpleado(
						empleado:empleado,
						ubicacion:empleado.perfil.ubicacion,
						antiguedadEnSemanas:0,
						nomina:nomina,
						vacaciones:0,
						fraccionDescanso:0,
						orden:orden++
						)
					ne.antiguedadEnSemanas=ne.getAntiguedad()
					ne.asistencia=asistencia
					nomina.addToPartidas(ne)
				
			}
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
	
	/*
	def actualizarAcumulados(int ejercicio,ConceptoDeNomina concepto,Empleado empleado){
		def acu=AumuladoPorConcepto.find{ejercicio==ejercicio && concepto==concepto && empleado==empleado}
	}
	*/
	
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
	
	def actualizarSaldo(Nomina nomina){
		//Actualiza los saldos de prima vacacional
		def rows=NominaPorEmpleadoDet.exequteQuery("from NominaPorEmpleadoDet d where d.parent.nomina=? and d.concepto.clave=?"
			,[nomina,'P024'])
		log.debug 'Registros de prima vacacional por actualizar: '+rows.size()
	}

	def depurar(Long id){
		Nomina nomina=Nomina.get(id)
		def calendarioDet=nomina.calendarioDet
		def asistencia=calendarioDet.asistencia
		def porBorrar=[]
		nomina.partidas.each{ ne->
			def empleado=ne.empleado
			if(empleado.baja && empleado.baja.fecha>=asistencia.fechaInicial){
				porBorrar.add(ne)
				
			}
			//Localisando modiificaciones de ubicacion
			if(empleado.perfil.ubicacion!=ne.ubicacion){
				log.info 'Re asignando ubicacion...'+ne
				ne.ubicacion=empleado.perfil.ubicacion
			}

		}
		porBorrar.each{ ne->
			ne.conceptos.each{
				//Tratando de localizar prestamo
				def abono=PrestamoAbono.findByNominaPorEmpleadoDet(it)
				if(abono){
					log.info 'Eliminando abono a prestamo...'+abono
					abono.delete()
				}
			}
			nomina.removeFromPartidas(ne)
			log.info 'Depuranda por baja nomina de : '+ne.empleado +' NominaPorEmpleado: '+ne.id+ ' Ubicacion: '+ne.ubicacion


		}
		// Ordendar la nomina
		ordenar(nomina)
		nomina.save failOnError:true
		return nomina
	}

	def ordenar(Nomina nomina){
		def list=nomina.partidas.sort{a,b ->
			a.ubicacion.clave<=>b.ubicacion.clave?:a.empleado.nombre<=>b.empleado.nombre
		}
		for(int i=0;i<list.size();i++){
			def ne=list[i]
			ne.orden=i+1
			//ne.save()
		}
	}
}

class NominaException extends RuntimeException{
	String message
	Nomina nomina
}
