package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import grails.validation.ValidationException
import org.apache.commons.lang.exception.ExceptionUtils

@Transactional
class EmpleadoService {


    Empleado updateEmpleado(Empleado empleado) {
    	
    	//Salvando empleado
    	try{
    		empleado.save(failOnError:true)
    		return empleado
    		}catch(Exception ex){
    			throw new EmpleadoException(
    				message:ExceptionUtils.getRootCauseMessage(ex),
    				empleado:empleado
    				)
    		}
    }
}


class EmpleadoException extends RuntimeException{
	String message
	Empleado empleado
}