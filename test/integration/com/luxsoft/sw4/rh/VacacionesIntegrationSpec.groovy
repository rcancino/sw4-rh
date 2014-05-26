package com.luxsoft.sw4.rh



import spock.lang.*

/**
 *
 */
class VacacionesIntegrationSpec extends Specification {
	
	def empleado

	def setup() {
		empleado=Empleado.build(apellidoPaterno:'ROGERS')
	}

	def cleanup() {
		empleado.delete()
	}

    void "Alta de Vacaciones"() {
		
		given:'Una entidad de vacaciones totalmente nueva'
		def vacaciones=new Vacaciones(
			empleado:empleado,
			solicitud:new Date(),
			comentario:'INTEGRATION TEST')
		
		when:'Salvamos la entidad'
		vacaciones.save flush:true
		
		then:'La entidad es persistida exitosamente'
		!vacaciones.hasErrors()
		vacaciones.id
		Vacaciones.get(vacaciones.id).comentario==vacaciones.comentario
		
    }
	
	void "Alta de Vacaciones con fechas"(){
		given:'Una entidad de vacaciones totalmente nueva'
		def vacaciones=new Vacaciones(
			empleado:empleado,
			solicitud:new Date(),
			comentario:'INTEGRATION TEST')
		
		when:'Agregamos fechas y salvamos'
		vacaciones.dias=new HashSet()
		vacaciones.dias.add Date.parse('dd/MM/yyyy','02/05/2014')
		vacaciones.dias.add Date.parse('dd/MM/yyyy','03/05/2014')
		vacaciones.dias.add Date.parse('dd/MM/yyyy','04/05/2014')
		vacaciones.dias.add Date.parse('dd/MM/yyyy','05/05/2014')
		vacaciones.save flush:true
		
		then:'Los dias se persisten exitosamente'
		!vacaciones.hasErrors()
		vacaciones.id
		Vacaciones.get(vacaciones.id).dias.size()==4
		
	}
}
