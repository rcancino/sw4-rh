package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Empresa;
import com.luxsoft.sw4.Periodo;

class Nomina {
	
	Empresa empresa
	FolioDeNomina folio  //??
	Periodo periodo
	Integer diasPagados
	Date fecha
	Date pago // fecha de pago aproximado
	
	/** Inician propiedades que podrian ir en una configuracion general**/
	//ConfiguracionDeNomina configuracion
	String tipo
	String diaDePago  // esto podria ser una bean de dias
	String periodicidad
	String formaDePago
	
	Date dateCreated
	Date lastUpdated
	
	static embedded = ['periodo']

    static constraints = {
		tipo inList:['GENERAL','ESPECIAL','AGINALDO','UTILIDADES']
		periodicidad inList:['SEMANAL','QUINCENAL','MENSUAL','ESPECIAL']
		formaDePago inList:['CHEQUE','TRANSFERENCIA']
    }
}
