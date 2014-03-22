package com.luxsoft.sw4.rh

import java.util.Date;

import com.luxsoft.sw4.Empresa;

class PerfilDeEmpleado implements Serializable{
	
	Empresa empresa
	Empleado empleado
	Integer numeroDeTrabajador
	Puesto puesto
	Departamento departamento
	Ubicacion ubicacion
	

	Date dateCreated
	Date lastUpdated

    static constraints = {
		empresa()
		empleado()
		numeroDeTrabajador(nullable:true)
		puesto()
		departamento()
		ubicacion()
    }
}
