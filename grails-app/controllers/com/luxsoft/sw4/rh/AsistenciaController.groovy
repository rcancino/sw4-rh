package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Periodo
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN'])
class AsistenciaController {
	
	def asistenciaService

    def index(Integer max) {
		params.max = Math.min(max ?: 20, 100)
		params.periodo=session.periodo?:new Periodo('01/04/2014','15/04/2014')
		def query=Asistencia.where{fecha>=params.periodo.fechaInicial &&
			fecha<=params.periodo.fechaFinal
		}
		[asistenciaInstanceList:query.list(),asistenciaTotalCount:query.count()]
	}



	def lectora(Integer max){
		params.max = Math.min(max ?: 50, 100)
		def query=Checado.where{fecha>=session.periodo.fechaInicial &&
			fecha<=session.periodo.fechaFinal
		}
		[checadoInstanceList:query.list(params),checadoTotalCount:query.count(params)]

	}
	def importarLecturas(){
		asistenciaService.importarLecturas(session.periodo)
		
	}
	def actualizarAsistencia(){
		def periodo=new Periodo('22/04/2014','22/04/2014')
		asistenciaService.registrarAsistencias(periodo)
	}
}
