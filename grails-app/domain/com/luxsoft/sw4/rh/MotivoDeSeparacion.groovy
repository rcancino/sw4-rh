package com.luxsoft.sw4.rh

class MotivoDeSeparacion {
	
	String descripcion

    static constraints = {
		descripcion minSize:1,unique:true
    }
	
	String toString() {
		return descripcion
	}
	
	static cargaInicial() {
		new MotivoDeSeparacion(descripcion:'Abandono de empleo').save(flush:true)
		new MotivoDeSeparacion(descripcion:'Faltas').save(flush:true)
		new MotivoDeSeparacion(descripcion:'Encontró una mejor oferta').save(flush:true)
		new MotivoDeSeparacion(descripcion:'Problemas con su jefe inmediato').save(flush:true)
		new MotivoDeSeparacion(descripcion:'Problemas con sus compañeros').save(flush:true)
		new MotivoDeSeparacion(descripcion:'No le gusto el lugar de trabajo').save(flush:true)
		new MotivoDeSeparacion(descripcion:'No le gusto el puesto').save(flush:true)
		new MotivoDeSeparacion(descripcion:'El trabajo le queda lejos (Distancia)').save(flush:true)
		new MotivoDeSeparacion(descripcion:'Estudios').save(flush:true)
		new MotivoDeSeparacion(descripcion:'Busca mejor sueldo').save(flush:true)
		new MotivoDeSeparacion(descripcion:'Cambio de domicilio').save(flush:true)
		new MotivoDeSeparacion(descripcion:'Empleo temporal').save(flush:true)
		new MotivoDeSeparacion(descripcion:'Inconformidad con las politicas').save(flush:true)
		new MotivoDeSeparacion(descripcion:'Problemas familiares').save(flush:true)
		new MotivoDeSeparacion(descripcion:'Problemas de salud').save(flush:true)
		new MotivoDeSeparacion(descripcion:'Familiar enfermo').save(flush:true)
		new MotivoDeSeparacion(descripcion:'Matrimonio').save(flush:true)
		new MotivoDeSeparacion(descripcion:'Cuidado de niños').save(flush:true)
		new MotivoDeSeparacion(descripcion:'Inicia negocio propio').save(flush:true)
		new MotivoDeSeparacion(descripcion:'Pensión').save(flush:true)
		new MotivoDeSeparacion(descripcion:'Otro').save(flush:true)
	}
}
