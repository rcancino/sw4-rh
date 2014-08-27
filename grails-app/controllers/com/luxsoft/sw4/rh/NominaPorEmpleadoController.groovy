package com.luxsoft.sw4.rh

import grails.transaction.Transactional;
import grails.plugin.springsecurity.annotation.Secured
import grails.converters.JSON

//@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
@Secured(['ROLE_ADMIN'])
class NominaPorEmpleadoController {
	
	def nominaPorEmpleadoService
	
	def nominaService 
	
	def conceptoDeNominaRuleResolver
	
	def ajusteIsr
	

    def index() { }

    def create(Long id){
    	def nomina=Nomina.get(id)
    	def ne=new NominaPorEmpleado(params)
    	[nominaInstance:nomina,nominaPorEmpleadoInstance:ne]
    }

    def edit(Long id){
    	def ne=NominaPorEmpleado.get(id)
    	def partidas=ne.nomina.partidas.sort{it.orden}
    	[nominaPorEmpleadoInstance:ne,nextItem:getNext(ne,partidas),prevItem:getPrev(ne,partidas)]
    }
	
	@Transactional
	def update(NominaPorEmpleado ne){
		log.info 'Actualizando ne '+ne
		//def ne=nominaPorEmpleadoService.actualizarNominaPorEmpleado(id)
		ne.save(failOnError:true)
		ne=nominaPorEmpleadoService.actualizarNominaPorEmpleado(ne.id)
		redirect action:'edit' ,params:[id:ne.id]
		//render view:'edit',model:[nominaPorEmpleadoInstance:ne]
	}

	@Transactional
    def agregarConcepto(Long id,String tipo){
		println 'Localizando conceptos para: '+tipo
    	request.withFormat{
    		html {
				
				def conceptos=ConceptoDeNomina.findAll{tipo==tipo}
    			render (template:'agregarPercepcionform'
    			,model:[nominaEmpleadoId:id,
    				nominaPorEmpleadoDetInstance:new NominaPorEmpleadoDet(),
    				conceptosList:conceptos
    				])
    		}
			form {
				
				NominaPorEmpleado ne=NominaPorEmpleado.get(id)
				log.info 'Agrgndo concepto: '+ne
				NominaPorEmpleadoDet det=new NominaPorEmpleadoDet(params)
				
				if(det?.concepto?.importeExcento){
					det.importeExcento=det.importeGravado
					det.importeGravado=0.0
				}
				if(det.comentario==null){
					det.comentario=" "
				}
				ne.addToConceptos(det)
				ne.actualizar()
				ne.save(failOnError:true)
				//ne=nominaPorEmpleadoService.actualizarNominaPorEmpleado(ne)
				redirect action:'edit',params:[id:ne.id]
			}    	
    	}
    	
    	
    }
	
	@Transactional
	def eliminarConcepto(Long id){
		def ne=nominaPorEmpleadoService.eliminarConcepto(id)
		redirect action:'edit',params:[id:ne.id]
	}
	
	def actualizarNominaPorEmpleado(Long id){
		def ne=nominaPorEmpleadoService.actualizarNominaPorEmpleado(id)
		//event("ActualizacionDeNominaDet")
		redirect action:'edit',params:[id:ne.id]
	}
	
	protected void notFound() {
		request.withFormat {
			form multipartForm {
				flash.message = message(code: 'default.not.found.message'
					, args: [message(code: 'nominaPorEmpleadoInstance.label', default: 'Nómina por Empleado'), params.id])
				redirect action: "index", method: "GET"
			}
			'*'{ render status: NOT_FOUND }
		}
	}
	
	def timbrar(Long id){
		def ne=NominaPorEmpleado.get(id)
		if(ne){
			//ne=nominaService.timbrar(ne)
			redirect action:'edit',params:[id:id]
		}
	}
	
	
	def informacionDeConcepto(Long id) {
		
		def  neDet=NominaPorEmpleadoDet.get(id)
		println 'Localizando informacion para el calculo del concepto: '+neDet.concepto
		def ruleModel=conceptoDeNominaRuleResolver.getModel(neDet.concepto)
		if(ruleModel) {
			render template:ruleModel.getTemplate(),model:ruleModel.getModel(neDet)
		}else {
			render {div("PENDIENTE DE IMPLEMENTAR")}
		}
		
	}

	private Long getNext(NominaPorEmpleado ne,def partidas){
		
		def next=ne.orden+1
		if(next>partidas.size())
			next=1
		def found=partidas.find{it.orden==next}
		return found?found.id:0
	}
	
	private Long getPrev(NominaPorEmpleado ne,def partidas){
		def prev=ne.orden-1
		if(prev<1)
			prev=partidas.size()
		def found=partidas.find{it.orden==prev}
		return found?found.id:0
	}
	
	def delete(Long id){
		def nomina=nominaPorEmpleadoService.eliminar(id)
		flash.message="Nomina por empleado eliminada: {$id}"
		redirect controller:'nomina',action:'show',params:[id:nomina.id]
	}
	
	
	
	def getEmpleadosDeNomina(Long id) {
		def term=params.term.trim()+'%'
		term=term.toLowerCase()
		
		
		def list=NominaPorEmpleado
			.executeQuery("from NominaPorEmpleado ne where ne.nomina.id=? and (lower(ne.empleado.apellidoPaterno) like ? or lower(ne.empleado.apellidoMaterno) like ? or lower(ne.empleado.nombres) like ?)"
				,[id,term,term,term])
		
		//println query.count()
		
		list=list.collect{ ne->
			def emp=ne.empleado
			def nombre="$emp.apellidoPaterno $emp.apellidoMaterno $emp.nombres"
			[id:ne.id
				,label:nombre
				,value:nombre
				,numeroDeTrabajador:emp.perfil.numeroDeTrabajador.trim()
			]
		}
		def res=list as JSON
		
		render res
	}
	
	@Transactional
	def ajusteMensualIsr(NominaPorEmpleado ne){
		ajusteIsr.ajusteMensual(ne)
		redirect action:'edit',params:[id:ne.id]
	}
    
}
