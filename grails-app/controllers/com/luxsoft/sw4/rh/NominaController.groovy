package com.luxsoft.sw4.rh


import com.luxsoft.sw4.Empresa;
import com.luxsoft.sw4.Periodo

import grails.transaction.Transactional
import grails.validation.Validateable
import groovy.transform.ToString
import grails.plugin.springsecurity.annotation.Secured

import org.grails.databinding.BindingFormat

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
        params.max = Math.min(max ?: 60, 100)
		params.periodicidad=params.periodicidad?:'QUINCENAL'
		params.sort=params.sort?:'lastUpdated'
		params.order='desc'
		def tipo=params.periodicidad=='SEMANAL'?'SEMANA':'QUINCENA'
		def periodos=CalendarioDet
			.findAll('from CalendarioDet d where d.calendario.tipo=? ',[tipo])
		
			def aguinaldos=CalendarioDet.findAll("from CalendarioDet d where d.calendario.comentario='AGUINALDO' and d.calendario.ejercicio=?",[2014])
			if(aguinaldos){
				periodos.addAll(aguinaldos)
			}
			//println 'Aguinaldo: '+aguinaldo
		//
		
		def query=Nomina.where{periodicidad==params.periodicidad }
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
		redirect action:'actualizarPartidas',params:[id:nominaInstance.id]
		
	}
	
	def actualizarPartidas(Long id) {
		def nomina=nominaService.actualizarPartidas(Nomina.get(id))
		redirect action:'depurar',params:[id:id]
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
		
		redirect action:'show',params:[id:nominaInstance.id]
	}
	
	/*def importar(ImportacionCmd cmd) {
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
					importarNominaService.importarQuincena(file,cmd.folio,periodo,cmd.fechaDePago,cmd.diaDePago)
					params.periodicidad='QUINCENAL'
					redirect action:'index',params:params
				}else if(cmd.tipo=='SEMANAL') {
					def file=new ByteArrayInputStream(cmd.archivo)
					importarNominaService.importarSemana(file,cmd.folio,periodo,cmd.fechaDePago,cmd.diaDePago)
					params.periodicidad='SEMANAL'
					redirect action:'index',params:params
				}
	
				
			}
			
		}
		
	}*/

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
		nomina.partidas.each{
			ajusteIsr.ajusteMensual(it)
		}
		nominaService.actualizarPartidas(Nomina.get(id))
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
    
}

@ToString(includeNames=true,includePackage=false)
class ImportacionCmd{
	
	byte[] archivo
	
	Integer folio
	
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

