package com.luxsoft.sw4.rh

import grails.transaction.Transactional

@Transactional
class IncapacidadService {

    def salvar(Incapacidad incapacidad) {
		incapacidad.save failOnError:true
		return incapacidad
    }
	
	
}