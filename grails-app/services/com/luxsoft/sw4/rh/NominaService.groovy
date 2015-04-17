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
	
	/**
	* Nota: Por el momento este metodo no se debe llamar si la nomina ya ha sido generada
	* 		es decir solo  funciona bien para genera los registros de nomina por empleado
	* 		la primer vez
	**/
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
			log.info 'Deduccion de prestamo personal detectado D004 '+ne.empleado+ ' Importe: '+neDet.total+ " NominaEmpleado: "+ne.id
			
			def prestamo=buscarPrestamo(ne)
			
			if(prestamo){
			//if(prestamo && (ne.cfdi==null)){
				
				
				def found=prestamo.abonos.find{a->
					//println 'Evaluando:'+a
					a?.nominaPorEmpleadoDet?.id==neDet?.id
				}
				
				if(!found){
					log.info "Generando abono de $neDet.importeExcento para prestamo: "+prestamo
					
					def abono=new PrestamoAbono(fecha:neDet.parent.nomina.pago
						,importe:neDet.importeExcento
						,nominaPorEmpleadoDet:neDet)
					prestamo.addToAbonos(abono)
					
					//prestamo.save failOnError:true
					
				}
				
				prestamo.actualizarSaldo()
			}
			
		}
	}
	
	def cancelarPrestamo(NominaPorEmpleado ne){
		def neDet=ne.conceptos.find{it.concepto.clave=='D004'}
		
		if(neDet){
			log.info 'Cancelando deduccion de prestamo aplicada '+ne.empleado+ ' Importe: '+neDet.total+ " NominaEmpleado: "+ne.id
			
			def prestamo=buscarPrestamo(ne)
			
			if(prestamo){
			
				def abono=prestamo.abonos.find{a->
					a?.nominaPorEmpleadoDet?.id==neDet?.id
				}
				if(abono){
					log.info "Eliminando el  abono de $neDet.importeExcento para prestamo: "+prestamo
					prestamo.removeFromAbonos(abono)
					
					//prestamo.save failOnError:true
					
				}
				
				prestamo.actualizarSaldo()
			}
			
		}
	}
	
	
	
	def actualizarOtrasDeducciones(NominaPorEmpleado ne){
		def neDet=ne.conceptos.find{it.concepto.clave=='D005'}
		if(neDet){
			log.info 'Otra Deduccion  detectado D005 '+ne.empleado+ ' Importe: '+neDet.total+ " NominaEmpleado: "+ne.id
			def deduccion=buscarOtraDeduccion(ne)
			//if(deduccion && (ne.cfdi==null) ){
			if(deduccion  ){
				def found=deduccion.abonos.find{a->
					a.nominaPorEmpleadoDet.id==neDet.id
				}
				if(!found){
					log.info "Generando abono de $neDet.importeExcento para prestamo: "+deduccion
					def abono=new OtraDeduccionAbono(
						fecha:neDet.parent.nomina.pago
						,importe:neDet.importeExcento
						,nominaPorEmpleadoDet:neDet)
					deduccion.addToAbonos(abono)
					deduccion.actualizarSaldo()
					//deduccion.save failOnError:true
					//log.info "Abono generado abono de $abono.importe para "+deduccion.toString()
				}
				deduccion.actualizarSaldo()
				
			}
		}
	}
	
	def cancelarOtrasDeducciones(NominaPorEmpleado ne){
		def neDet=ne.conceptos.find{it.concepto.clave=='D005'}
		if(neDet){
			log.info 'Cancelando Otra Deduccion  detectado D005 '+ne.empleado+ ' Importe: '+neDet.total+ " NominaEmpleado: "+ne.id
			def deduccion=buscarOtraDeduccion(ne)
			
			if(deduccion){
				println 'Eliminando otra deduccion '+deduccion
				def abono=deduccion.abonos.find{a->
					a.nominaPorEmpleadoDet.id==neDet.id
				}
				if(abono){
					log.info "Elinando otra deduccion  de $neDet.importeExcento para : "+deduccion
					deduccion.removeFromAbonos(abono)
				}
				deduccion.actualizarSaldo()
				deduccion.save flush:true
			}else{
				println 'No encontro el registro de Deduccion para: Nomina'+ne.id
			}
		}
	}
	
	
	
	private Prestamo buscarPrestamo(NominaPorEmpleado ne) {
		def prestamos=Prestamo.findAll("from Prestamo p where p.saldo>=0 and p.empleado=? order by p.id  asc"
			,[ne.empleado],[max:1])
		return prestamos?prestamos[0]:null
	}
	private OtraDeduccion buscarOtraDeduccion(NominaPorEmpleado ne) {
		def prestamos=OtraDeduccion.findAll("from OtraDeduccion o where o.saldo>=0 and o.empleado=? order by o.id asc"
			,[ne.empleado],[max:1])
		return prestamos?prestamos[0]:null
	}
	
	def actualizarSaldos(Nomina nomina){
		nomina.partidas.each{ne->
			if(ne.cfdi){
				actualizarOtrasDeducciones(ne)
				actualizarPrestamo(ne)
				actualizarCalculoAnual(ne)
				actualizarVacaciones(ne)
			}
			
		}
	}
	
	def cancelarSaldos(Nomina nomina){
		nomina.partidas.each{ne->
			if(!ne.cfdi){
				println 'Cancelando saldos: '+ne
				cancelarOtrasDeducciones(ne)
				cancelarPrestamo(ne)
				cancelarCalculoAnual(ne)
				cancelarVacaciones(ne)
			}
			
		}
	}
	
	def actualizarVacaciones(NominaPorEmpleado ne){
		def neDet=ne.conceptos.find{it.concepto.clave=='P024'}
		if(neDet){
			log.info 'Actualizando vacaciones detectada P024 para  '+ne.empleado+ ' Importe: '+neDet.total+ " NominaEmpleado: "+ne.id
			def control=ControlDeVacaciones.find("from ControlDeVacaciones v where v.ejercicio=? and v.empleado=?"
				,[ne.nomina.ejercicio.toLong(),ne.empleado])
			def data=NominaPorEmpleadoDet.executeQuery(
				"select sum(d.importeExcento),sum(d.importeGravado) from NominaPorEmpleadoDet d where d.parent.empleado=? and d.parent.nomina.ejercicio=? and d.concepto.clave=? and d.parent.cfdi!=null",
				[ne.empleado,ne.nomina.ejercicio,neDet.concepto.clave])
			control.acumuladoExcento=data.get(0)[0]?:0.0
			control.acumuladoGravado=data.get(0)[1]?:0.0
		}
	}
	
	def cancelarVacaciones(NominaPorEmpleado ne){
		def neDet=ne.conceptos.find{it.concepto.clave=='P024'}
		if(neDet){
			log.info 'Cancelando vacaciones detectada P024 para  '+ne.empleado+ ' Importe: '+neDet.total+ " NominaEmpleado: "+ne.id
			def control=ControlDeVacaciones.find("from ControlDeVacaciones v where v.ejercicio=? and v.empleado=?"
				,[ne.nomina.ejercicio.toLong(),ne.empleado])
			def data=NominaPorEmpleadoDet.executeQuery(
				"select sum(d.importeExcento),sum(d.importeGravado) from NominaPorEmpleadoDet d where d.parent.empleado=? and d.parent.nomina.ejercicio=? and d.concepto.clave=? and d.parent.cfdi!=null and d.id!=?",
				[ne.empleado,ne.nomina.ejercicio,neDet.concepto.clave,neDet.id])
			control.acumuladoExcento=data.get(0)[0]?:0.0
			control.acumuladoGravado=data.get(0)[1]?:0.0
		}
	}
	
	
	def actualizarCalculoAnual(NominaPorEmpleado ne){
		def neDet=ne.conceptos.find{it.concepto.clave=='P033'}
		if(neDet){
			log.info 'Percepcion por calculo anual P003 detectada para  '+ne.empleado+ ' Importe: '+neDet.total+ " NominaEmpleado: "+ne.id
			def calculo=CalculoAnual.find (
				"from CalculoAnual c where c.ejercicio=? and c.empleado=?  "
				,[ne.nomina.ejercicio-1,ne.empleado])
			
			if(calculo){
				def aplicado=NominaPorEmpleadoDet.executeQuery(
					"select sum(d.importeExcento) from NominaPorEmpleadoDet d where  d.parent.empleado=? and d.concepto.clave=? and d.id!=?",
					[ne.empleado,"P033",neDet.id])
				calculo.aplicado=aplicado.get(0)?:0.0
				
			}
		}
	}
	
	def cancelarCalculoAnual(NominaPorEmpleado ne){
		def neDet=ne.conceptos.find{it.concepto.clave=='P033'}
		if(neDet){
			log.info 'Percepcion por calculo anual P003 detectada para  '+ne.empleado+ ' Importe: '+neDet.total+ " NominaEmpleado: "+ne.id
			def calculo=CalculoAnual.find (
				"from CalculoAnual c where c.ejercicio=? and c.empleado=?  "
				,[ne.nomina.ejercicio-1,ne.empleado])
			
			if(calculo){
				def aplicado=NominaPorEmpleadoDet.executeQuery(
					"select sum(d.importeExcento) from NominaPorEmpleadoDet d where  d.parent.empleado=? and d.concepto.clave=?",
					[ne.empleado,"P033"])
				calculo.aplicado=aplicado.get(0)?:0.0
				
			}
		}
	}
	
}

class NominaException extends RuntimeException{
	String message
	Nomina nomina
}
