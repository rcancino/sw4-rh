package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Empresa
import com.luxsoft.sw4.Periodo
import com.luxsoft.sw4.rh.acu.AcumuladoPorConcepto

import grails.transaction.NotTransactional
import grails.transaction.Transactional
import groovy.time.TimeCategory
import grails.events.Listener



class NominaService {
	
	def cfdiService
	
	def procesadorDeNomina
	
	def prestamoService
	
	def otraDeduccionService
	
	
	@Transactional
	def eliminarNomina(Long id){
		def nomina=Nomina.get(id)
		//nominaInstance.attach()
		if(nomina.tipo=='AGUINALDO'){
			//def res=Aguinaldo.executeUpdate("update Aguinaldo a set a.nominaPorEmpleado=null where a.nominaPorEmpleado.nomina=?",[nomina])
			def aguinaldos=Aguinaldo.findAll("from Aguinaldo a where a.nominaPorEmpleado.nomina=?",[nomina])
			aguinaldos.each {
				it.nominaPorEmpleado=null
				it.save flush:true
			}
			
		}
		nomina.delete()
	}
	
	/*@Transactional
	def generarAguinaldo(Long calendarioDetId,String formaDePago){
		log.info 'Generando nomina de aguinaldo'
		def calendarioDet=CalendarioDet.get(calendarioDetId)
		def periodicidad=calendarioDet.calendario.tipo=='SEMANA'?'SEMANAL':'QUINCENAL'
		Empresa empresa=Empresa.first()
		def nomina=Nomina.find{calendarioDet==calendarioDet && tipo==tipo && formaDePago==formaDePago}
		if(nomina){
			throw new NominaException(message:'Nomina ya generada calendario: '+calendarioDet)
		}
		nomina=new Nomina(tipo:'AGUINALDO',
			periodicidad:periodicidad,
			folio:calendarioDet.folio,
			status:"PENDIENTE",
			periodo:periodo,
			calendarioDet:calendarioDet,
			ejercicio:calendarioDet.calendario.ejercicio)
		nomina.pago=calendarioDet.fechaDePago
		nomina.diaDePago=calendarioDet.fechaDePago.format('EEEE')
		nomina.formaDePago=formaDePago
		nomina.empresa=empresa
		nomina.total=0.0
		generarPartidas nomina
		nomina.save(failOnError:true)
		return nomina
	}*/

	@Transactional
	def generar(Long calendarioDetId,String tipo,String formaDePago,String periodicidad){
		log.info 'Generando nomina: '+tipo+ " Cal:"+calendarioDetId
		def calendarioDet=CalendarioDet.get(calendarioDetId)
		
		//def periodicidad=calendarioDet.calendario.tipo=='SEMANA'?'SEMANAL':'QUINCENAL'
		def periodo=calendarioDet.periodo()
		Empresa empresa=Empresa.first()
		def nomina=Nomina.find{calendarioDet==calendarioDet && tipo==tipo && formaDePago==formaDePago}
		if(nomina){
			throw new NominaException(message:'Nomina ya generada calendario: '+calendarioDet)
		}
		nomina=new Nomina(tipo:tipo,
			periodicidad:periodicidad,
			folio:calendarioDet.folio,
			status:"PENDIENTE",
			periodo:periodo,
			calendarioDet:calendarioDet,
			ejercicio:calendarioDet.calendario.ejercicio)
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
		
		if(nomina.tipo=='AGUINALDO'){
			nomina=generarAguinaldo(nomina)
			return nomina
		}
		def tipo=nomina.periodicidad
		//def asistencias=Asistencia.findAllByCalendarioDet(nomina.calendarioDet)
		def asistencias=Asistencia
			.findAll("from Asistencia a where a.calendarioDet=? and a.empleado.salario.formaDePago=?"
				,[nomina.calendarioDet,nomina.formaDePago])
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
	
	@Transactional
	def actualizarPartidas(Nomina nomina) {
		assert nomina,"Nomina nula no es valido"
		validarParaModificacion(nomina)
		if(nomina.tipo=='AGUINALDO'){
			actualizarAguinaldo(nomina)
			return nomina
		}
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
				if(ne.nomina.tipo=='AGUINALDO'){
					actualizarPrestamo(ne)
					actualizarOtrasDeducciones(ne)
				}
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
	
	def actualizarSaldos(Nomina nomina){
		
		//Actualiza los saldos de prima vacacional
		/*
		long ejercicio=nomina.calendarioDet.calendario.ejercicio
		def rows=NominaPorEmpleadoDet.findAll("from NominaPorEmpleadoDet d where d.parent.nomina=? and d.concepto.clave=?",[nomina,'P024'])
		rows.each{
			def control=ControlDeVacaciones.find("from ControlDeVacaciones v where v.ejercicio=? and v.empleado=?",[ejercicio,it.parent.empleado])
			if(it.importeExcento>0){
				control.acumuladoExcento=control.acumuladoExcento+it.importeExcento
				control.
				log.info "Acumulado actualizado de prima vacacional para $it.parent.empleado Excento:$it.importeExcento Acu: ${control?.acumuladoExcento}"
			  	
			}
		}
		*/
	}
	
	@Transactional
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
				log.debug 'Re asignando ubicacion...'+ne
				ne.ubicacion=empleado.perfil.ubicacion
			}

		}
		porBorrar.each{ ne->
			ne.conceptos.each{
				//Tratando de localizar prestamo
				def abono=PrestamoAbono.findByNominaPorEmpleadoDet(it)
				if(abono){
					log.debug 'Eliminando abono a prestamo...'+abono
					abono.delete()
				}
			}
			nomina.removeFromPartidas(ne)
			log.debug 'Depuranda por baja nomina de : '+ne.empleado +' NominaPorEmpleado: '+ne.id+ ' Ubicacion: '+ne.ubicacion


		}
		// Ordendar la nomina
		ordenar(nomina)
		nomina.save failOnError:true
		return nomina
	}
	
	
	@Transactional
	def generarEmpleado(Nomina nomina,Empleado empleado) {
		def ne=new NominaPorEmpleado(
			empleado:empleado,
			ubicacion:empleado.perfil.ubicacion,
			antiguedadEnSemanas:0,
			nomina:nomina,
			vacaciones:0,
			fraccionDescanso:0
			)
		ne.antiguedadEnSemanas=ne.getAntiguedad()
		ne.salarioDiarioBase=empleado.salario.salarioDiario
		ne.salarioDiarioIntegrado=empleado.salario.salarioDiarioIntegrado
		def asistencia=Asistencia.findByCalendarioDetAndEmpleado(nomina.calendarioDet,empleado)
		ne.asistencia=asistencia
		nomina.addToPartidas(ne)
		nomina.save failOnError:true
		ordenar(nomina)
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
	
	def generarAguinaldo(Nomina nomina){
		def aguinaldos=Aguinaldo
			.findAll (
				"from Aguinaldo a where a.ejercicio=? and a.empleado.salario.periodicidad=? and a.empleado.salario.formaDePago=?"
			,[nomina.ejercicio,nomina.periodicidad,nomina.formaDePago])
		
		aguinaldos.each{
			def empleado=it.empleado
			def ne=new NominaPorEmpleado(
			empleado:empleado,
			ubicacion:empleado.perfil.ubicacion,
			antiguedadEnSemanas:0,
			nomina:nomina,
			vacaciones:0,
			fraccionDescanso:0
			
			)
			ne.antiguedadEnSemanas=ne.getAntiguedad()
			ne.salarioDiarioBase=empleado.salario.salarioDiario
			ne.salarioDiarioIntegrado=empleado.salario.salarioDiarioIntegrado
			//def asistencia=Asistencia.findByCalendarioDetAndEmpleado(nomina.calendarioDet,empleado)
			//ne.asistencia=asistencia
			nomina.addToPartidas(ne)
			
			it.nominaPorEmpleado=ne
		}
		nomina.save failOnError:true
		ordenar(nomina)
		return nomina
	}
	 
	def actualizarAguinaldo(Nomina nomina)	{	
		nomina.partidas.each{ ne ->
			ne.conceptos.clear()
			def aguinaldo=Aguinaldo.findByNominaPorEmpleado(ne)
			if(aguinaldo){
				log.info 'Actualizando aguinaldo: '+aguinaldo
				//Percepcion 1
				def p1=new NominaPorEmpleadoDet(concepto:ConceptoDeNomina.findByClave('P002')
					,importeGravado:aguinaldo.aguinaldoGravado
					,importeExcento:aguinaldo.aguinaldoExcento
					,comentario:'PENDIENTE')
				ne.addToConceptos(p1)
				
				def p2=new NominaPorEmpleadoDet(concepto:ConceptoDeNomina.findByClave('P011')
					,importeGravado:aguinaldo.bono
					,importeExcento:0.0
					,comentario:'PENDIENTE')
				ne.addToConceptos(p2)
				
				
				def d1=new NominaPorEmpleadoDet(concepto:ConceptoDeNomina.findByClave('D002')
					,importeGravado:0.0
					,importeExcento:aguinaldo.isrPorRetener
					,comentario:'PENDIENTE')
				ne.addToConceptos(d1)
				
				if(aguinaldo.pensionA){
					def d2=new NominaPorEmpleadoDet(concepto:ConceptoDeNomina.findByClave('D007')
						,importeGravado:0.0
						,importeExcento:aguinaldo.pensionA
						,comentario:'PENDIENTE')
					ne.addToConceptos(d2)
				}
				if(aguinaldo.otrasDed){
					def d2=new NominaPorEmpleadoDet(concepto:ConceptoDeNomina.findByClave('D005')
						,importeGravado:0.0
						,importeExcento:aguinaldo.otrasDed
						,comentario:'PENDIENTE')
					ne.addToConceptos(d2)
				}
				if(aguinaldo.prestamo){
					def d2=new NominaPorEmpleadoDet(concepto:ConceptoDeNomina.findByClave('D004')
						,importeGravado:0.0
						,importeExcento:aguinaldo.prestamo
						,comentario:'PENDIENTE')
					ne.addToConceptos(d2)
				}
				
			}
			
		}
	}
	
	def actualizarPrestamo(NominaPorEmpleado ne){
		def neDet=ne.conceptos.find{it.concepto.clave=='D004'}
		if(neDet){
			def prestamo=buscarPrestamo(ne)
			if(prestamo){
				log.info 'Generando abono nuevo '
				def abono=new PrestamoAbono(fecha:neDet.parent.nomina.pago
						,importe:neDet.importeExcento
						,nominaPorEmpleadoDet:neDet)
				prestamo.addToAbonos(abono)
				//prestamo.save()
			}
		}
	}
	
	def actualizarOtrasDeducciones(NominaPorEmpleado ne){
		def neDet=ne.conceptos.find{it.concepto.clave=='D005'}
		if(neDet){
			def deduccion=buscarOtraDeduccion(ne)
			if(deduccion){
				log.info 'Generando abono nuevo '
				def abono=new OtraDeduccionAbono(
							fecha:neDet.parent.nomina.pago
							,importe:neDet.importeExcento
							,nominaPorEmpleadoDet:neDet)
					deduccion.addToAbonos(abono)
				//prestamo.save()
			}
		}
	}
	
	private Prestamo buscarPrestamo(NominaPorEmpleado ne) {
		def prestamos=Prestamo.findAll("from Prestamo p where p.saldo>0 and p.empleado=? order by p.saldo desc"
			,[ne.empleado],[max:1])
		return prestamos?prestamos[0]:null
	}
	private OtraDeduccion buscarOtraDeduccion(NominaPorEmpleado ne) {
		def prestamos=OtraDeduccion.findAll("from OtraDeduccion o where o.saldo>0.0 and o.empleado=? order by o.saldo desc"
			,[ne.empleado],[max:1])
		return prestamos?prestamos[0]:null
	}
}

class NominaException extends RuntimeException{
	String message
	Nomina nomina
}
