package com.luxsoft.sw4.rh

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional


@Secured(['ROLE_ADMIN','RH_USER'])
@Transactional(readOnly = true)
class DiaFestivoController {

    def index(Integer max) {
		params.max = Math.min(max ?: 15, 100)
		[diaFestivoList:DiaFestivo.list(params), diaFestivoTotalCount: DiaFestivo.count()]
	}
	
	def create() {
		[diaFestivoInstance:new DiaFestivo(fecha:new Date(),ejercicio:new Date().year)]
	}
	
	@Transactional
	def save(DiaFestivo diaFestivoInstance) {
		if(diaFestivoInstance==null) {
			notFound()
			return
		}
		if(diaFestivoInstance.hasErrors()) {
			render view:'create',model:[diaFestivoInstance:diaFestivoInstance]
		}
		diaFestivoInstance.save flush:true
		flash.message="Fecha registrada : "+diaFestivoInstance.id
		
		redirect action:'index'
	}
	
	def edit(DiaFestivo diaFestivoInstance) {
		if(diaFestivoInstance==null) {
			notFound()
			return
		}
		[diaFestivoInstance:diaFestivoInstance]
	}
	
	@Transactional
	def delete(DiaFestivo diaFestivoInstance) {
		if(diaFestivoInstance==null) {
			notFound()
			return
		}
		diaFestivoInstance.delete flush:true
		flash.message="Dia festivo  $diaFestivoInstance.id eliminado"
		redirect action:'index'
	}
	
	@Transactional
	def update(DiaFestivo diaFestivoInstance) {
		if(diaFestivoInstance==null) {
			notFound()
			return
		}
		if(diaFestivoInstance.hasErrors()) {
			render view:'edit',model:[diaFestivoInstance:diaFestivoInstance]
		}
		diaFestivoInstance.save flush:true
		flash.mesage="Dia festivo modificado: "+diaFestivoInstance.id
		redirect action:'index'
	}
	
	protected void notFound() {
		request.withFormat {
			form multipartForm {
				flash.message = message(code: 'default.not.found.message'
					, args: [message(code: 'vacacinesInstance.label', default: 'Dia Festivo'), params.id])
				redirect action: "index", method: "GET"
			}
			'*'{ render status: NOT_FOUND }
		}
	}
}
