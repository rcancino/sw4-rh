// Groovy Code here

// Implicit variables include:
//     ctx: the Spring application context
//     grailsApplication: the Grails application
//     config: the Grails configuration
//     request: the HTTP request
//     session: the HTTP session

// Shortcuts:
//     Execute: Ctrl-Enter
//     Clear: Esc

import com.luxsoft.sw4.rh.*

def empleado=Empleado.get(245)

def cal=CalendarioDet.get(49)
  
def asistencia=new Asistencia(empleado:empleado,tipo:'QUINCENAL',periodo:cal.asistencia,calendarioDet:cal)
asistencia.save failOnError:true

//def asistencia=Asistencia.findByEmpleado(empleado)

//println 'Asistencia '+asistencia.id

def nomina=Nomina.get(248)
ne=new NominaPorEmpleado(
						empleado:empleado,
						ubicacion:empleado.perfil.ubicacion,
						antiguedadEnSemanas:0,
						nomina:nomina,
						vacaciones:0,
						fraccionDescanso:0,
						orden:1
						)
					ne.antiguedadEnSemanas=ne.getAntiguedad()
					ne.asistencia=asistencia
					nomina.addToPartidas(ne)

