package com.luxsoft.sw4.rh

class NominaPorEmpleadoController {

    def index() { }

    def create(Long id){
    	def nomina=Nomina.get(id)
    	def ne=new NominaPorEmpleado(params)
    	[nominaInstance:nomina,nominaPorEmpleadoInstance:ne]
    }

    def edit(Long id){
    	def ne=NominaPorEmpleado.get(id)
    	[nominaPorEmpleadoInstance:ne]
    }

    
}
