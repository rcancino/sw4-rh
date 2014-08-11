package com.luxsoft.sw4.rh

import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef

import com.luxsoft.sw4.Periodo
import grails.gorm.DetachedCriteria
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN'])
class AsistenciaController {
	
	def asistenciaService
	def checadoService
	

	def cargarAsistencia(Long calendarioDetId){
		def list=Asistencia.findAll("from Asistencia a where a.calendarioDet.id=? order by a.empleado.perfil.ubicacion.clave asc",[calendarioDetId])
		render template:'asistenciaGridPanel',model:[asistenciaInstanceList:list]
	}

    def index() {
    	def tipo=params.tipo?:'SEMANA'
    	def calendarioDet
    	if(tipo=='SEMANA'){
    		calendarioDet=session.calendarioSemana
    		
    	}else if(tipo=='QUINCENA'){
    		calendarioDet=session.calendarioQuincena
    	}
    	
    	def partidasMap=[]
    	if(calendarioDet){
    		def list=Asistencia.findAll{calendarioDet==calendarioDet}
    		partidasMap=list.groupBy([{it.empleado.perfil.ubicacion.clave}])
    	}
    	def periodos=CalendarioDet.findAll{calendario.ejercicio==2014 && calendario.tipo==tipo}
    	
    	[calendarioDet:calendarioDet,partidasMap:partidasMap,tipo:tipo,periodos:periodos]
    	//redirect action:'asistenciaQuincenal'
	}

	def cambiarCalendario(Long calendarioDetId){
		def cal=CalendarioDet.get(calendarioDetId)
		def tipo='SEMANA'
		if(cal){
			if(cal.calendario.tipo=='SEMANA'){
				session.calendarioSemana=cal
			}else if(cal.calendario.tipo=='QUINCENA'){
				session.calendarioQuincena=cal
				tipo='QUINCENA'
			}
		}
		redirect action:'index',parans:[tipo:tipo]
	}
	
	def show(Asistencia asistencia){
		def tipo=asistencia.calendarioDet.calendario.tipo
		log.info 'Cargando calendarios para: '+tipo
		def periodos=CalendarioDet.findAll{calendario.ejercicio==2014 && calendario.tipo==tipo}
		[asistenciaInstance:asistencia,asistenciaDetList:asistencia.partidas.sort(){it.fecha},periodos:periodos]
	}

	def delete(Asistencia asistencia){
		def res=asistenciaService.delete(asistencia)
		log.info res
		flash.message=res
		def tipo=asistencia.calendarioDet.calendario.tipo=='SEMANA'?'SEMANAL':'QUINCENAL'
		redirect view:'index', model:[tipo:tipo]
	}

	def lectora(Integer max,Periodo periodo){
		params.max = Math.min(max ?: 50, 100)
		if(!periodo)
			periodo=new Periodo()
		def query=Checado.where{fecha>=periodo.fechaInicial &&
			fecha<=periodo.fechaFinal
		}
		[checadoInstanceList:query.list(params),checadoTotalCount:query.count(params),periodo:periodo]

	}
	
	def importarLecturas(Periodo periodo){
		
		checadoService.importarLecturas(periodo)
		session.periodo=periodo
		redirect action:'lectora',params:params
	}

	def actualizarAsistencias(Long id){
		def calendarioDet=CalendarioDet.get(id)
		
		if(calendarioDet){
			log.info 'Actualizando lista de asistencias para el periodo: '+calendarioDet.asistencia
			//asistenciaService.actualizarAsistencia(calendarioDet)
			asistenciaService.depurar(calendarioDet)

		}
		redirect action:'index',model:[tipo:calendarioDet.calendario.tipo]
		
	}
	
	
	def actualizar(Long id) {
		
		def asistencia=Asistencia.get(id)
		def d=params.double('diasTrabajados')
		bindData(asistencia, params, [include: ['minutosPorDescontar']])
		if(d>0.0){
			log.info "Actualizando dias trabajados "+d
			asistencia.diasTrabajados=d
			asistencia=asistencia.save flush:true
			render view:'show',model:[asistenciaInstance:asistencia,asistenciaDetList:asistencia.partidas.sort(){it.fecha}]
		}
		
		if(asistencia){
			asistencia=asistenciaService.actualizarAsistencia(asistencia)
			render view:'show',model:[asistenciaInstance:asistencia,asistenciaDetList:asistencia.partidas.sort(){it.fecha}]
		}
	}
	
	def next(Long id,Long calendarioDetId){
		def found=Asistencia.findAll(
			"from Asistencia a where a.calendarioDet.id=? and a.id>? order by a.empleado.perfil.ubicacion.clave asc",
			[calendarioDetId,id]
			,[max:1])
		//def found=Asistencia.findAll("from Asistencia a where id>? order by a.empleado.perfil.ubicacion.clave asc",[id],[max:1])
		//def found=Asistencia.findAll(sort:'empleado',order:'asc',max:1){id>id}
		def next=found?found.get(0):Asistencia.get(id)
		redirect action:'show',params:[id:next.id]
		//respond next,[view:'show']
	}
	
	def previous(Long id){
		
		def found=Asistencia.findAll{id<id}
		def next=found?found.get(0):Asistencia.get(id)
		//redirect action:'show',params:[id:next.id]
		respond next,[view:'show']
	}
	
	def nomina(Long id){
		def asistencia=Asistencia.get(id)
		def ne=NominaPorEmpleado.findByAsistencia(asistencia)
		if(ne){
			redirect controller:'nominaPorEmpleado',action:'edit',params:[id:ne.id]
		}else{
			redirect action:'show',params:[id:id]
		}
		
	}
	
	def jasperService
	
	def reporteMensual(){
		println 'Ejecutando reporte mensual '+params
		//println "Repote mensual: CalIni:${params.CAL_ID_INI} CalFin:${params.CAL_ID_FIN} empleado:${params.ID}"
		def reportDef = new JasperReportDef(
			name:'RetardoMensualPorEmpleado',
			fileFormat:JasperExportFormat.PDF_FORMAT,
			parameters:params
			)
		//jasperService
	}
	
}
