package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Periodo
import grails.gorm.DetachedCriteria
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN'])
class AsistenciaController {
	
	def asistenciaService
	
	def cambiarPeriodo(Periodo periodo) {
		session.periodo=periodo?:Periodo.getCurrentMonth()
		redirect action:'index'
	}
	
	def asistenciaSemanal(){
		session.periodicidad='SEMANAL'
		redirect action:'index'
	}
	
	def asistenciaQuincenal(Long calendarioDetId){
		def tipo='QUINCENA'
		def periodos=CalendarioDet.findAll("from CalendarioDet d where d.calendario.tipo='QUINCENA'")
		render  view:'index',model:[periodos:periodos,tipo:tipo]
	}

	def cargarAsistencia(Long calendarioDetId){
		def list=Asistencia.findAll("from Asistencia a where a.calendarioDet.id=? order by a.empleado.apellidoMaterno desc",[calendarioDetId])
		render template:'asistenciaGridPanel',model:[asistenciaInstanceList:list]
	}

    def index() {
    	redirect action:'asistenciaQuincenal'
	}
	
	def show(Asistencia asistencia){
		[asistenciaInstance:asistencia,asistenciaDetList:asistencia.partidas.sort(){it.fecha}]
	}



	def lectora(Integer max){
		params.max = Math.min(max ?: 50, 100)
		def periodo=session.periodo
		def query=Checado.where{fecha>=periodo.fechaInicial &&
			fecha<=periodo.fechaFinal
		}
		[checadoInstanceList:query.list(params),checadoTotalCount:query.count(params),periodo:periodo]

	}
	
	def importarLecturas(Periodo periodo){
		
		asistenciaService.importarLecturas(periodo)
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
		if(asistencia){
			asistencia=asistenciaService.actualizarAsistencia(asistencia)
			render view:'show',model:[asistenciaInstance:asistencia,asistenciaDetList:asistencia.partidas.sort(){it.fecha}]
		}
		
	}
	
}
