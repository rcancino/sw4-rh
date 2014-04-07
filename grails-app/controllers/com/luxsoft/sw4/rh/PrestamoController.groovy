package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Empresa

class PrestamoController {

    def index() { 

    }

    def create(){
		def empresa=Empresa.first()
    	[prestamoInstance:new Prestamo(empresa:empresa)]
    }
	
	def save(Prestamo prestamoInstance) {
		if(!prestamoInstance)
			notFound()
		
		prestamoInstance.validate()
		if(prestamoInstance.hasErrors()) {
			flash.message="Prestamo invalido"
			render view:'create',model:[prestamoInstance:prestamoInstance]
		}
		
		prestamoInstance.save(failOnError:true)
		flash.message="Prestamo generado: "+prestamoInstance.id
		redirect action:'index'
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



