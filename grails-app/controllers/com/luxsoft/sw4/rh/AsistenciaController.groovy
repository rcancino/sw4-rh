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

    def index(Integer max) {
		params.max = Math.min(max ?: 20, 100)
		
		def p=params.periodo?:session.periodo
		println 'Periodo operativo: '+p
		
		def list=Asistencia.findAll(
			"from Asistencia a where date(a.periodo.fechaInicial)>=? and date(a.periodo.fechaFinal)<=?"
			,[p.fechaInicial,p.fechaFinal]
			,params)  
		println 'Regs: '+list.size()
		//def query=Asistencia.where{periodo?.fechaInicial>=p.fechaInicial && periodo?.fechaFinal<=p.fechaFinal} 
		def query=new DetachedCriteria(Asistencia).build{
			ge 'periodo.fechaInicial',p.fechaInicial
		}
		query=query.build{
			le 'periodo.fechaFinal',p.fechaFinal
		}
		
		[asistenciaInstanceList:query.list(params),asistenciaTotalCount:query.count(params),periodo:p]
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
	
	def actualizarAsistencia(String tipo){
		def periodo=new Periodo('22/04/2014','22/04/2014')
		asistenciaService.registrarAsistencias(periodo,tipo)
		redirect action:'index'
	}
}
