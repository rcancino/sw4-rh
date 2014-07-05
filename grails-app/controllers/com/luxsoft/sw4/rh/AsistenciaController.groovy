package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Periodo
import grails.gorm.DetachedCriteria
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN'])
class AsistenciaController {
	
	def asistenciaService
	def checadoService
	
	def cambiarPeriodo(Periodo periodo) {
		session.periodo=periodo?:Periodo.getCurrentMonth()
		redirect action:'index'
	}
	
	def asistenciaSemanal(){
		def tipo='SEMANA'
		def periodos=CalendarioDet.findAll("from CalendarioDet d where d.calendario.tipo=?",[tipo])
		render  view:'index',model:[periodos:periodos,tipo:tipo]
	}
	
	def asistenciaQuincenal(Long calendarioDetId){
		def tipo='QUINCENA'
		def periodos=CalendarioDet.findAll("from CalendarioDet d where d.calendario.tipo='QUINCENA'")
		render  view:'index',model:[periodos:periodos,tipo:tipo]
	}

	def cargarAsistencia(Long calendarioDetId){
		def list=Asistencia.findAll("from Asistencia a where a.calendarioDet.id=? order by a.empleado.perfil.ubicacion.clave asc",[calendarioDetId])
		render template:'asistenciaGridPanel',model:[asistenciaInstanceList:list]
	}

    def index() {
    	redirect action:'asistenciaQuincenal'
	}
	
	def show(Asistencia asistencia){
		[asistenciaInstance:asistencia,asistenciaDetList:asistencia.partidas.sort(){it.fecha}]
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

	def actualizarAsistencias(Long calendarioDetId){
		def calendarioDet=CalendarioDet.get(calendarioDetId)
		if(calendarioDet){
			println 'Actualizando lista de asistencias para el periodo: '+calendarioDet.asistencia
			asistenciaService.actualizarAsistencia(calendarioDet)
			def list=Asistencia.findAll("from Asistencia a where a.calendarioDet.id=?",[calendarioDetId])
			render template:'asistenciaGridPanel',model:[asistenciaInstanceList:list]
		}else{
			
			render {
    			div(class:"", id: "myDiv", "Debe seleccionar un periodo...")
			}
		}
		
	}
	
	
	def actualizar(Long id) {
		
		def asistencia=Asistencia.get(id)
		def d=params.double('diasTrabajados')
		if(d>0.0){
			log.debug "Actualizando dias trabajados "+d
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
	
}
