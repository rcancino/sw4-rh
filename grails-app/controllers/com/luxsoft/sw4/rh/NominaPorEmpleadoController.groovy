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

    def agregarConcepto(Long id,String tipo){
    	
    	request.withFormat{
    		html {
				def conceptos=ConceptoDeNomina.findAll{tipo==tipo}
    			render (template:'agregarConceptoform'
    			,model:[nominaEmpleadoId:id,
    				nominaPorEmpleadoDetInstance:new NominaPorEmpleadoDet(),
    				conceptosList:conceptos
    				])
    		}
			form {
				//Generar el alta de la nomina por empleado
			}    	
    	}
    	
    	
    }

    
}
