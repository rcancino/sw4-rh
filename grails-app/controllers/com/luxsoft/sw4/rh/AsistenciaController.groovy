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
	
	def asistenciaQuincenal(){
		session.periodicidad='QUINCENAL'
		redirect action:'index'
	}

    def index(Integer max) {
		params.max = Math.min(max ?: 20, 100)
		def tipo=session.periodicidad?:'QUINCENAL'
		def p=params.periodo?:session.periodo
		
		def query=new DetachedCriteria(Asistencia).build{
			ge 'periodo.fechaInicial',p.fechaInicial
		}
		query=query.build{
			le 'periodo.fechaFinal',p.fechaFinal
		}
		query=query.build{
			eq 'tipo',tipo
		}
		
		[asistenciaInstanceList:query.list(params),asistenciaTotalCount:query.count(params),periodo:p,tipo:tipo]
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
	
	def actualizarAsistencia(){
		def periodo=session.periodo
		def tipo=session.periodicidad
				
		asistenciaService.registrarAsistencias(periodo,tipo)
		redirect action:'index'
	}
	
	def actualizar(Long id) {
		def asistencia=asistenciaService.actualizarAsistencia(id)
		render view:'show',model:[asistenciaInstance:asistencia,asistenciaDetList:asistencia.partidas.sort(){it.fecha}]
	}
	/*
	def imprimirAsistencia(Asistencia asistencia){
		$P{SFECHA_INI}
		new java.text.SimpleDateFormat('yyyy/MM/dd').parse('23/')
	}*/
}
