package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Empresa
import grails.transaction.Transactional

@Transactional(readOnly = true)
class PrestamoController {

    def index(Integer max) { 
		params.max = Math.min(max ?: 10, 100)
		[prestamoInstanceList:Prestamo.list(params),prestamoInstanceCount:Prestamo.count()]
    }

    def create(){
		
    	[prestamoInstance:new Prestamo()]
    }
	
	@Transactional
	def save(Prestamo prestamoInstance) {
		if(!prestamoInstance){
			notFound()
			return
		}
		
		prestamoInstance.validate()
		if(prestamoInstance.hasErrors()) {
			flash.message="Prestamo invalido"
			render view:'create',model:[prestamoInstance:prestamoInstance]
		}		
		prestamoInstance.save(failOnError:true)
		flash.message="Prestamo generado: "+prestamoInstance.id
		redirect action:'index'
	}
	
	def edit(Long id){
		def prestamoInstance=Prestamo.get(id)
		[prestamoInstance:prestamoInstance,abonos:prestamoInstance.getAbonos()]
	}
	
	@Transactional
	def update(Prestamo prestamo){
		if(!prestamo){
			notFound()
			return
		}
		prestamo.save(failOnError:true)
		flash.message="Prestamo actualizado: "+prestamo.id
		redirect action:'edit',params:[id:prestamo.id]
	}
	
	@Transactional
	def eliminarPartida(PrestamoAbono abono){
		def prestamo=abono.prestamo
		prestamo.removeFromAbonos(abono)
		prestamo.save(flush:true)
		redirect action:'edit',params:[id:prestamo.id]
	}
	
	
	def agregarAbono(Prestamo prestamoInstance){
		[prestamoInstance:prestamoInstance,prestamoAbonoInstance:new PrestamoAbono()]
		
	}
	
	@Transactional
	def salvarAbono(PrestamoAbono abono){
		def prestamo=Prestamo.get(params.prestamo.id)
		abono.validate()
		if(abono.hasErrors()){
			flash.message="Error de validacion en abono"
			render view:'agregarAbono',model:[prestamoInstance:prestamo,prestamoAbonoInstance:abono]
		}
	//	abono.save(flush:true)
		prestamo.addToAbonos(abono)
		prestamo.save(flush:true)
		redirect action:'edit',params:[id:prestamo.id]
	}
	
	protected void notFound() {
		request.withFormat {
			form multipartForm {
				flash.message = "No existe el prestamo  ${params.id}"
				redirect action: "index", method: "GET"
			}
			'*'{ render status: NOT_FOUND }
		}
	}
}



