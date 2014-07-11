import grails.util.Environment

import com.luxsoft.sw4.cfdi.CfdiCadenaBuilder
import com.luxsoft.sw4.cfdi.CfdiSellador
import com.luxsoft.sw4.cfdi.CfdiTimbrador
import com.luxsoft.sw4.rh.ConceptoDeNominaRuleResolver
import com.luxsoft.sw4.rh.ProcesadorDeISTP
import com.luxsoft.sw4.rh.ProcesadorDeNomina
import com.luxsoft.sw4.rh.ProcesadorDePrestamosPersonales;
import com.luxsoft.sw4.rh.ProcesadorDePrimaVacacional;
import com.luxsoft.sw4.rh.ProcesadorDeSueldo
import com.luxsoft.sw4.rh.ProcesadorDeVacaciones
import com.luxsoft.sw4.rh.ProcesadorSeguroSocial
import com.luxsoft.sw4.rh.ProcesadorDeIncentivo



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
	
	procesadorDeNomina(ProcesadorDeNomina){
		reglas=[ref('procesadorDeSueldo'),
			ref('procesadorDeVacaciones'),
			ref('procesadorDePrimaVacacional'),
			ref('procesadorDeIncentivo'),
			ref('procesadorSeguroSocial'),
			ref('procesadorDeISTP'),
			ref('procesadorDePrestamosPersonales')
			]
	}
	
	conceptoDeNominaRuleResolver(ConceptoDeNominaRuleResolver){
		ruleMap=['P001':ref('procesadorDeSueldo'),
				 'D002':ref('procesadorDeISTP'),
				 'P021':ref('procesadorDeISTP'),
				 'P010':ref('procesadorDeIncentivo'),
				 'P025':ref('procesadorDeVacaciones'),
				 'P024':ref('procesadorDePrimaVacacional'),
				 'D004':ref('procesadorDePrestamosPersonales')
				 ]
	}
	
	procesadorDeSueldo(ProcesadorDeSueldo){}
	procesadorDeISTP(ProcesadorDeISTP){}
	procesadorSeguroSocial(ProcesadorSeguroSocial){}
	procesadorDeIncentivo(ProcesadorDeIncentivo){}
	procesadorDeVacaciones(ProcesadorDeVacaciones){}
	procesadorDePrimaVacacional(ProcesadorDePrimaVacacional){}
	procesadorDePrestamosPersonales(ProcesadorDePrestamosPersonales){}
}
