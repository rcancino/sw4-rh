package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Periodo
import grails.plugin.springsecurity.annotation.Secured

@Secured(["hasRole('RH_USER')"])
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
}
