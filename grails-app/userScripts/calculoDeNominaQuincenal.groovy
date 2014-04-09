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
import com.luxsoft.sw4.rh.imss.*
import java.math.*
  
def ne=NominaPorEmpleado.get(5169)

def det=ne.conceptos.find(){ it.concepto.clave=='D002'}

def percepciones=ne.getPercepcionesGravadas()

def diasEjercicio=365
def diasMes= (diasEjercicio/12).setScale(1,RoundingMode.HALF_EVEN)



def found =TarifaIsr.obtenerTabla(15).find(){(percepciones>it.limiteInferior && percepciones<=it.limiteSuperior)}

//println found

def importeGravado=percepciones-found.limiteInferior
importeGravado*=found.porcentaje
importeGravado/=100
importeGravado+=found.cuotaFija
importeGravado=importeGravado.setScale(2,RoundingMode.HALF_EVEN)
//println found.porcentaje +'  cf:'+found.cuotaFija+ 'Dia mes:'+diasMes
det.importeGravado=importeGravado
det.importeExcento=0.0
ne.actualizar()

