package com.luxsoft.sw4.rh


import com.luxsoft.sw4.Empresa;
import com.luxsoft.sw4.Periodo

import grails.transaction.Transactional
import grails.validation.Validateable
import groovy.transform.ToString
import grails.plugin.springsecurity.annotation.Secured

import org.grails.databinding.BindingFormat

import com.luxsoft.sw4.rh.acu.IsptMensual

//@Transactional(readOnly = true)
@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
class NominaController {

	def nominaService
    //static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
	def importarNominaService
	
	def ajusteIsr

	def nominaPorEmpleadoService
	
	def calculoAnualService
    
    def index(Integer max) {
        params.max = 1000  //Math.min(max ?: 60, 100)
		params.periodicidad=params.periodicidad?:'QUINCENAL'
		params.sort=params.sort?:'lastUpdated'
		params.order='desc'
		def tipo=params.periodicidad=='SEMANAL'?'SEMANA':'QUINCENA'
		def ejercicio=session.ejercicio
		def periodos=CalendarioDet
			.findAll('from CalendarioDet d where d.calendario.tipo=? and d.calendario.ejercicio=?',[tipo,ejercicio])
		
		/*
			def aguinaldos=CalendarioDet
			.findAll("from CalendarioDet d where d.calendario.comentario='AGUINALDO' and d.calendario.ejercicio=?"
				,[ejercicio])
			if(aguinaldos){
				periodos.addAll(aguinaldos)
			}
			//println 'Aguinaldo: '+aguinaldo
		*/
		//
		
		def query=Nomina.where{periodicidad==params.periodicidad && ejercicio==session.ejercicio}
		[nominaInstanceList:query.list(params)
			,nominaInstanceListTotal:query.count(params)
			,periodicidad:params.periodicidad,periodos:periodos
			,calendarioActual:tipo=='SEMANAL'?session.calendarioSemana:session.calendarioQuincena]
    }

    def show(Long id) {
		def nominaInstance=Nomina.get(id)
		Map partidasMap=nominaInstance.partidas.groupBy([{it.ubicacion.clave}])
		[nominaInstance:nominaInstance,partidasMap:partidasMap]
		        
    }	
	

	def generar(Long calendarioDet){
		def tipo=params.tipo
		def periodicidad=params.periodicidad
		def formaDePago=params.formaDePago
		def nominaInstance=nominaService.generar(calendarioDet,tipo,formaDePago,periodicidad)
		nominaInstance.partidas.each{
			nominaService.actualizarCalculoAnual(nominaInstance.ejercicio,it.empleado)
			nominaService.actualizarVacaciones(nominaInstance.ejercicio,it.empleado)
		}
		redirect action:'actualizarPartidas',params:[id:nominaInstance.id]
		
	}
	
	def actualizarPartidas(Long id) {
		//def nomina=nominaService.actualizarPartidas(Nomina.get(id))

		def nomina =Nomina.get(id)

		if(nomina.tipo == 'AGUINALDO' ){
			nominaService.actualizarAguinaldo(nomina)
			flash.message="Nomina de Aguinaldo actualizada"
			redirect action:'show',params:[id:id]
			return
		}

		if(nomina.tipo=='PTU'){
			nominaService.actualizarPtu(nomina)
			flash.message="Nomina de PTU actualizada"
			redirect action:'show',params:[id:id]
			return
		}
		nomina.partidas.each{
			def ajuste=IsptMensual.find("from IsptMensual i where i.nominaPorEmpleado.id=?",[it.id])
			if(ajuste){
				flash.message="Nomina con ajuste mensual ISPT (NO SE PUEDE RECALCULAR)"
				redirect action:'show',params:[id:id]
				return
			}else{
				def ne=nominaPorEmpleadoService.actualizarNominaPorEmpleado(it.id)
				nominaPorEmpleadoService.depurarNominaPorEmpleado(ne.id)
			}
		}
		nominaService.depurar(id)
		flash.message="Actualizacion exitosa"
		redirect action:'show',params:[id:id]
	}

	def depurar(Long id){
		nominaService.depurar(id)
		redirect action:'show',params:[id:id]	
	}

    def delete(Nomina nominaInstance){
    	if(nominaInstance==null){
    		notFound()
    		return
    	}
    	nominaService.eliminarNomina(nominaInstance.id)
    	flash.message="Nomina ${nominaInstance.id} eliminada"
    	redirect action:'index',params:[periodicidad:nominaInstance.periodicidad]
    }
	
	

    private List findEmpleadosDisponibles(Long nominaId,String periodicidad){
        def res=Empleado.findAll(
            "from Empleado e where e.status ='ALTA' and e.salario.periodicidad=? and e.id not in(select ne.empleado.id from NominaPorEmpleado ne where ne.nomina.id=?) "
            ,[periodicidad,nominaId])
    }
	
	def timbrar(Nomina nominaInstance) {
		if(nominaInstance==null){
			notFound()
			return
		}
		nominaInstance.partidas.each{
			nominaService.timbrar(it.id)
		}
		nominaInstance.status='CERRADA'
		nominaInstance=nominaInstance.save flush:true
		nominaService.actualizarSaldos(nominaInstance)
		redirect action:'show',params:[id:nominaInstance.id]
	}
	
	def actualizarSaldos(Nomina nominaInstance){
		nominaService.actualizarSaldos(nominaInstance)
		flash.message="Saldos actualizados"
		redirect action:'show',params:[id:nominaInstance.id]
	}
	
	def importar(ImportacionCmd cmd) {
		if(request.method=='GET') {
			render view:'importar',model:[importacionCmd:new ImportacionCmd()]
		}else {
			//Parsing parameters
			log.info 'Generando importacion para: '+cmd
			if(cmd.hasErrors()) {
				flash.message="Errores en parametros de importacion"
				render view:'importar',model:[importacionCmd:cmd]
			}else {
				
				//def file=request.getFile('archivo')
				//render 'Procesando importcion con archivo: '+file.getContentType()
				
				def periodo=new Periodo(cmd.fechaInicial,cmd.fechaFinal)
				if(cmd.tipo=='QUINCENAL') {
					def file=new ByteArrayInputStream(cmd.archivo)
					importarNominaService.importarQuincena(file,cmd.folio,periodo,cmd.fechaDePago,cmd.diaDePago,cmd.ejercicio)
					params.periodicidad='QUINCENAL'
					redirect action:'index',params:params
				}else if(cmd.tipo=='SEMANAL') {
					def file=new ByteArrayInputStream(cmd.archivo)
					importarNominaService.importarSemana(file,cmd.folio,periodo,cmd.fechaDePago,cmd.diaDePago,cmd.ejercicio)
					params.periodicidad='SEMANAL'
					redirect action:'index',params:params
				}
	
				
			}
			
		}
		
	}

    protected void notFound() {
		request.withFormat {
			form multipartForm {
				flash.message = message(code: 'default.not.found.message'
					, args: [message(code: 'nomina.label', default: 'Nómina'), params.id])
				redirect action: "index", method: "GET"
			}
			'*'{ render status: NOT_FOUND }
		}
	}
	
	//@Transactional
	def ajusteMensualIsr(Long id){
		
		Nomina nomina=Nomina.get(id)
		nomina.partidas.each{ne->
			
			println "Calculando ajuste mensual para :"+ ne.empleado.nombre
			def found=IsptMensual.findByNominaPorEmpleado(ne)
			if(!found){
				println "Ajustando nomina"
				ajusteIsr.ajusteMensual(ne)
				nominaPorEmpleadoService.actualizarNominaPorEmpleado(ne.id)
			}else{
			  println "Nomina ya ajustada"
			}

		}
		redirect action:'show',params:[id:nomina.id]
	}
	
	@Transactional
	def eliminarAjusteMensualIsr(Long id){
		
		Nomina nomina=Nomina.get(id)
		nomina.partidas.each{ne ->
			if(ne.cfdi==null){
				def found=IsptMensual.findByNominaPorEmpleado(ne)
				if(found){
					log.info 'Eliminando ajuste mensual '+ne
					found.delete flush:true
					nominaPorEmpleadoService.actualizarNominaPorEmpleado(ne.id)
					
				}
			}
		}
		//nominaService.actualizarPartidas(Nomina.get(id))
		flash.message="Ajuste mensual ISTP eliminado"
		redirect action:'show',params:[id:nomina.id]
	}

	def aplicarCalculoAnual(Long id){
		
		Nomina nomina=Nomina.get(id)
		nomina.partidas.each{
			calculoAnualService.aplicar(it)
		}
		//nominaService.actualizarPartidas(Nomina.get(id))
		redirect action:'show',params:[id:nomina.id]
	}
	
	@Transactional
	def aplicarCalculoAnualConSaldo(Long id){
		Nomina nomina=Nomina.get(id)
		def ejercicio=nomina.ejercicio-1
		nomina.partidas.each{ne->
			
			def calculo=CalculoAnual.findByEjercicioAndEmpleado(ejercicio,ne.empleado)
			if(calculo && calculo.saldo && calculo.calculoAnual){
				calculoAnualService.aplicarCalculoAnualConSaldo(ne,calculo)
				
			}
		}
		flash.message="Aplicacion de saldos para el calculo anual "+ejercicio+ " generado"
		redirect action:'show',params:[id:nomina.id]
	}


	// def getNominasAsJSON() {
	// 	def term=params.term.trim()+'%'
	// 	def query=Nomina.where{
	// 		calendario.tipo=~term || folio.toString()=~term 
	// 	}
	// 	def list=query.list(max:100, sort:"folio",order:'desc')
	// 	//println query.count()
	// 	println list.size()
	// 	list=list.collect{ calDet->
	// 		def nombre="$calDet.calendario.tipo $calDet.folio  $calDet.calendario.ejercicio"
	// 		[id:calDet.id
	// 			,label:nombre
	// 			,value:nombre
	// 		]
	// 	}
	// 	def res=list as JSON
		
	// 	render res
	// }
    
}

@ToString(includeNames=true,includePackage=false)
class ImportacionCmd{
	
	byte[] archivo
	
	Integer folio
	
	Integer ejercicio
	
	String tipo
	
	@BindingFormat('dd/MM/yyyy')
	Date fechaInicial
	
	@BindingFormat('dd/MM/yyyy')
	Date fechaFinal
	
	@BindingFormat('dd/MM/yyyy')
	Date fechaDePago
	
	String diaDePago
	
	static constraints = {
		tipo inList:['SEMANAL','QUINCENAL']
		diaDePago inList:['LUNES','MARTES','MIERCOLES','JUEVES','VIERNES']
		archivo nullable:false
		folio minSize:1
		ejercicio inList:[2015,2016,2017,2018,2019,2020]
	} 
}

@Validateable
class ReporteDeNominaCommand{
	
	String tipo
	Integer ejercicio
	Integer folio
	String periodicidad

	static constraints = {
		ejercicio inList:2014..2018
		tipo inList:['GENERAL','ESPECIAL','AGUINALDO','PTU']
		folio range:1..200
		periodicidad inList:['SEMANAL','QUINCENAL']
		
	}
	
}

