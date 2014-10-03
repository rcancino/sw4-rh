package com.luxsoft.sw4.rh



import com.luxsoft.sw4.Empresa
import com.luxsoft.sw4.rh.sat.SatRegimenContratacion;
import com.luxsoft.sw4.rh.sat.SatRiesgoPuesto;

class PerfilDeEmpleado implements Serializable{
	
	Empresa empresa
	Empleado empleado
	String tipo
	String numeroDeTrabajador
	Puesto puesto
	Departamento departamento
	Ubicacion ubicacion
	String tipoDeContrato
	String jornada

	Turno turno

	String tipoDeIncentivo
	
	
	SatRegimenContratacion regimenContratacion
	SatRiesgoPuesto riesgoPuesto

	Date dateCreated
	Date lastUpdated

    static constraints = {
		empresa()
		empleado()
		tipo inList:['SINDICALIZADO','CONFIANZA']
		numeroDeTrabajador(nullable:true)
		puesto()
		departamento()
		ubicacion()
		tipoDeContrato inList:['BASE','EVENTUAL','CONFIANZA','SINDICALIZADO','A PRUEBA']
		jornada inList:['MEDIA','COMPLETA','DIURNA','NOCTURAN','MIXTA','POR HORA','REDUCIDA','CONTINUADA','POR TURNOS']
    	turno nullable:true
    	tipoDeIncentivo nullable:true,inList:['SEMANAL','QUINCENAL','MENSUAL','NO_APLICA']
    }
}
