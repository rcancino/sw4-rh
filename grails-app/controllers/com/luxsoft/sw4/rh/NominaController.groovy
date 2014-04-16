package com.luxsoft.sw4.rh




import grails.transaction.Transactional
import grails.validation.Validateable
import groovy.transform.ToString
import grails.plugin.springsecurity.annotation.Secured


//@Transactional(readOnly = true)
@Secured(["hasRole('RH_USER')"])
class NominaController {

    //static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    
    def index(Integer max) {
        params.max = Math.min(max ?: 20, 100)
		params.periodicidad=params.periodicidad?:'QUINCENAL'
		//println 'Localizando nominas: '+params
		def query=Nomina.where{periodicidad==params.periodicidad}
		[nominaInstanceList:query.list(),nominaInstanceListTotal:query.count()]
    }

    def show(Long id) {
		def nominaInstance=Nomina.get(id)
		
		[nominaInstance:nominaInstance]
		        
    }

    def agregar(){
		def nomina=Nomina.get(params.id)
		params.periodicidad=nomina.periodicidad
		params.folio=nomina.folio
        redirect view:'agregarPartida', params:params
    }

    def agregarPartidaFlow={
        initialize {
            action{
				
                log.info 'Inicianlizando flow para agregar partidas a nomina params: '+params
                
                def nominaCmd=new NominaCmd()
                bindData(nominaCmd, params)
				
                //flow.persistenceContext.flush()
                //flow.persistenceContext.persistenceContext.clear()
               
                def empleados=findEmpleadosDisponibles(nominaCmd.id,nominaCmd.periodicidad)
                flow.nominaCmd=nominaCmd
				NominaEmpleadoCmd flow.nominaEmpleado=new NominaEmpleadoCmd()
                [nominaInstance:flow.nominaCmd,
                nominaEmpleado:flow.nominaEmpleado,
                empleadosList:empleados]

            }
            on("success").to "seleccionDeEmpleado"
			
        }
		
        seleccionDeEmpleado {
            on("siguiente"){NominaEmpleadoCmd cmd ->
				
				bindData(flow.nominaEmpleado,params)
				flow.nominaEmpleado.validate()
				println 'Empleado en flow: '+ flow.nominaEmpleado
				if(flow.nominaEmpleado.hasErrors()) {
					flash.message="Errores de validacion"
					println 'Errores de validacion en registro: '+flow.nominaEmpleado
					return error()
				}
				println 'Empleado salario: '+flow.nominaEmpleado?.empleado?.salario?.salarioDiario
                flow.salario=new SalarioCmd(
                    salarioDiario:flow.nominaEmpleado?.empleado?.salario?.salarioDiario,
                    salarioDiarioIntegrado:flow.nominaEmpleado?.empleado?.salario?.salarioDiarioIntegrado)
				[nominaEmpleado:flow.nominaEmpleado,salario:flow.salario]
				
			}.to("salarios")
            on("cancelar").to("cancelar")

        }
        salarios{
			
			on("anterior").to("seleccionDeEmpleado")
			on("siguiente"){ SalarioCmd command ->
                    println 'Validando: '+command
					flow.salario=command
					command.validate()
					if(command.hasErrors()) {
						flash.message="Errores de validacion en el salario"
						return error()
					}
                }.to("percepciones")
			on("cancelar").to("cancelar")
		}
		percepciones{
			on("anterior").to("salarios")
			on("siguiente").to("deducciones")
			on("cancelar").to("cancelar")
		}
		deducciones{
			on("anterior").to("percepciones")
			on("siguiente").to("verificar")
			on("cancelar").to("cancelar")
		}
		verificar{
			on("anterior").to("deducciones")
			on("siguiente").to("verificar")
			on("cancelar").to("cancelar")
		}
        cancelar{
			/*def nom=flow.nominaCmd
			println 'Cancelando agrgado nomina: '+nom.class
            redirect action:'show',params:[id:nom.id]
            */
			
			redirect action:'index'
        }


    }

    private List findEmpleadosDisponibles(Long nominaId,String periodicidad){
        def res=Empleado.findAll(
            "from Empleado e where e.status ='ALTA' and e.salario.periodicidad=? and e.id not in(select ne.empleado.id from NominaPorEmpleado ne where ne.nomina.id=?) "
            ,[periodicidad,nominaId])
    }

    
}

@Validateable
@ToString(includePackage=false,includeNames=true)
class NominaCmd implements Serializable{
    
    Long id
    Integer folio  
    String periodicidad
    

}



@ToString(includePackage=false,includeNames=true)
class NominaEmpleadoCmd implements Serializable{
    Long nominaId
    Empleado empleado
    Ubicacion ubicacion
	
	static constraints = {
		empleado nullable:false
		ubicacion nullable:false
	}

}

@ToString(includePackage=false,includeNames=true)
class SalarioCmd implements Serializable{
    BigDecimal salarioDiario
    BigDecimal salarioDiarioIntegrado

    static constraints={
        salarioDiario nullable:false,min:1.0
        salarioDiarioIntegrado nullable:false,min:1.0
    }
}

