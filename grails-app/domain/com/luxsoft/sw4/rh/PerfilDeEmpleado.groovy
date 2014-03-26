package com.luxsoft.sw4.rh

import java.util.Date;

import com.luxsoft.sw4.Empresa;

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
		tipoDeContrato inList:['EVENTUAL','MEDIO TIEMPO','TIEMPO COMPLETO']
		jornada inList:['MEDIA','COMPLETA']
    }
}
