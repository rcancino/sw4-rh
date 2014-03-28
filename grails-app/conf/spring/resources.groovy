import grails.util.Environment

import com.luxsoft.sw4.cfdi.CfdiCadenaBuilder
import com.luxsoft.sw4.cfdi.CfdiSellador
import com.luxsoft.sw4.cfdi.CfdiTimbrador


// Place your Spring DSL code here
beans = {
	switch(Environment.current){
		case Environment.PRODUCTION:
			cfdiTimbrador(CfdiTimbrador){
				timbradoDePrueba=false
			}
			break
		case Environment.DEVELOPMENT:
			cfdiTimbrador(CfdiTimbrador){
				timbradoDePrueba=true
			}
			break
		case Environment.TEST:
			cfdiTimbrador(CfdiTimbrador){
			timbradoDePrueba=true
		}
	}
	
	cfdiCadenaBuilder(CfdiCadenaBuilder){
		//xsltFileName="web-app/sat/cadenaoriginal_3_2.xslt"
	}

	cfdiSellador(CfdiSellador){
		cadenaBuilder=ref("cfdiCadenaBuilder")
	}
}
