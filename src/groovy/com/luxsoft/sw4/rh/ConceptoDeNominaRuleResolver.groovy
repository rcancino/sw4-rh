package com.luxsoft.sw4.rh

/**
 * Resolver un ConceptoDeNominaInfo para cada tipo de concepto
 * 
 * @author rcancino
 *
 */ 
class ConceptoDeNominaRuleResolver {
	
	def ruleMap=[:]
	
	def getModel(ConceptoDeNomina concepto) {
		def ruleModel=ruleMap[concepto.clave]
		return   ruleModel
	}

}

