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
import com.luxsoft.sw4.*
import com.luxsoft.sw4.rh.*

/* Paso 1
def empleado=Empleado.findWhere(apellidoPaterno:'PRADO')
def concepto=ConceptoDeNomina.findWhere(clave:'D012')
def incidencia=Incidencia.build(concepto:concepto,empleado:empleado,comentario:'PRUEBAS')
def incidenciaId=incidencia.id
*/
/** Paso 2 
def incidencia=Incidencia.get(1)
IncidenciaDet.build(incidencia:incidencia,
                            tipo:'RETARDO',
                            fecha:Date.parse('dd/MM/yyyy','05/01/2014'),
                            horas:1.5)
IncidenciaDet.build(incidencia:incidencia,
                            tipo:'RETARDO',
                            fecha:Date.parse('dd/MM/yyyy','07/01/2014'),
                            horas:2)

def incidencia=Incidencia.get(1)
  def salario=incidencia.empleado.salario.salarioDiario
  incidencia.salarioDiario=salario
  incidencia.partidas.each{ it ->
    switch (it.tipo){
    case "FALTA":
    	println 'Descontar todo el dia y la parte proporcional al dia de descanso'
   		break
    case 'RETARDO':
    	
      	def res=(salario/8)*it.horas
      	it.importe=res
      	def prop=(salario*it.horas)/(6*8)
      	it.importeProporcional=prop
      	println "Descontando $res de $it.horas y la parte proporcional $prop al dia de descanso"
    	break
    case 'PERMISO':
    	println "Descontanto $it.horas y la parte proporcional al dia de descanso"
    	break
    default:
          break
          }
     
    
    
  }
**/