package com.luxsoft.sw4.rh

import static org.springframework.http.HttpStatus.*

import com.luxsoft.sw4.Autorizacion;

import grails.plugin.springsecurity.annotation.Secured;
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN','RH_USER'])
@Transactional(readOnly = true)
class IncentivoController {
	
	def incentivoService

	def index() {

		def calendarioDet
    	def calendarioDet2
		def tipo=params.tipo?:'QUINCENA'

		if(tipo=='SEMANA'){
			calendarioDet=session.calendarioSemana
			calendarioDet2=session.calendarioSemana2

		}else{
			calendarioDet=session.calendarioQuincena
			calendarioDet2=calendarioDet
		}
		calendarioDet.attach()
		calendarioDet2.attach()
    	
    	
    	def partidasMap=[]
    	if(calendarioDet){
    		def list=Incentivo.findAll("from Incentivo i where i.calendarioIni=? and i.calendarioFin=?"
    			,[calendarioDet,calendarioDet2])

    		partidasMap=list.groupBy([{it.empleado.perfil.ubicacion.clave}])
    	}
    	def ejercicio=calendarioDet?calendarioDet.calendario.ejercicio:session.ejercicio
    	def periodos=CalendarioDet.findAll{calendario.ejercicio==ejercicio && calendario.tipo==tipo}
    	
    	[calendarioDet:calendarioDet,calendarioDet2:calendarioDet2
    	,partidasMap:partidasMap,tipo:tipo
    	,periodos:periodos
    	,ejercicio:ejercicio]
	}

	def actualizarPeriodo(){
		Long calendarioDetId=params.long('calendarioDetId')
		CalendarioDet ini=CalendarioDet.get(calendarioDetId)
		String tipo=params.tipo

		if(tipo=='SEMANA'){
			Long calendarioDetId2=params.long('calendarioDetId2')
			CalendarioDet fin=CalendarioDet.get(calendarioDetId2)
			if(ini && fin){
				session.calendarioSemana=ini
				session.calendarioSemana2=fin
			}

		}else{
			if(ini){
				session.calendarioQuincena=ini
			}
		}
		redirect action:'index' ,params:[tipo:tipo]
	}
	
	@Transactional
	def actualizarIncentivos(){
		def calendarioDet
    	def calendarioDet2
		def tipo=params.tipo
		assert tipo,'Se debe definir el tipo de actualiacion SEMANAl o QUINCENL'
		if(tipo=='SEMANA'){
			calendarioDet=session.calendarioSemana
			calendarioDet2=session.calendarioSemana2

		}else {
			calendarioDet=session.calendarioQuincena
			calendarioDet2=calendarioDet
		}
		calendarioDet.attach()
		calendarioDet2.attach()
		
		incentivoService.generarIncentivos(calendarioDet,calendarioDet2)
		redirect action:'index',params:[tipo:tipo]
	}

	def create() {
		[incentivoInstance:new Incentivo(fecha:new Date())]
	}

	
	@Transactional
	def edit(Incentivo incentivoInstance) {
		if(incentivoInstance==null) {
			notFound()
			return
		}
		[incentivoInstance:incentivoInstance]
	}

	@Transactional
	def update(Incentivo incentivoInstance) {
		if(incentivoInstance==null) {
			notFound()
			return
		}
		if(incentivoInstance.hasErrors()) {
			render view:'edit',model:[incentivoInstance:incentivoInstance]
		}
		incentivoInstance.save flush:true
		flash.message="Solicitud de incentivo actualizada: "+incentivoInstance.id
		redirect action:'index'
	}

	@Transactional
	def autorizar(Incentivo incentivoInstance) {

		def comentario=params.comentarioAutorizacion
		if(comentario) {
			def aut=new Autorizacion(
					autorizo:getAuthenticatedUser(),
					descripcion:comentario,
					modulo:'RH',
					tipo:'INCENTIVO')
			aut.save failOnError:true
			incentivoInstance.autorizacion=aut
			//incentivoInstance.save flush:true
		}
		respond incentivoInstance,[view:'edit']
	}

	@Transactional
	def cancelarAutorizacion(Incentivo incentivoInstance) {
		if(incentivoInstance==null) {
			notFound()
			return
		}
		if(incentivoInstance.autorizacion) {
			def aut=incentivoInstance.autorizacion
			incentivoInstance.autorizacion=null
			aut.delete flush:true

		}
		respond incentivoInstance,[view:'edit']
	}

	@Transactional
	def delete(Incentivo incentivoInstance) {
		if(incentivoInstance==null) {
			notFound()
			return
		}
		incentivoInstance.delete flush:true
		flash.message="Solicitud $incentivoInstance.id eliminada"
		redirect action:'index'
	}
	protected void notFound() {
		request.withFormat {
			form multipartForm {
				flash.message = message(code: 'default.not.found.message'
				, args: [
					message(code: 'vacacinesInstance.label', default: 'Incentivo'),
					params.id
				])
				redirect action: "index", method: "GET"
			}
			'*'{ render status: NOT_FOUND }
		}
	}
	
	def actualizarAjax(String[] folios){
		println 'Actualizando inentivos seleccionados.'
		def val=params.folios
		println 'Folios: '+val+ ' Tipo: '+val.class.name
 		println 'Folios: '+folios
		//println 'Request: '+request
	}

}
