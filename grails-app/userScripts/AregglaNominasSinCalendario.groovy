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
import com.luxsoft.sw4.rh.Nomina

def list=Nomina.findAll()
println list.size()

list.each{
  //println 'Arreglando nomina: '+it
  if(!it.partidas){
    it.delete()
    
  }else{
    for(nd in it.partidas){
    if(nd.cfdi){
      println 'Timbrada'+it
      it.status='CERRADA'
      break
    }
  }
  }
  
  
}