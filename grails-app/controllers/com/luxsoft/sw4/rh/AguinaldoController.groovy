package com.luxsoft.sw4.rh

class AguinaldoController {
    static scaffold = true

    def aguinaldoService

    def index(){
    	def ejercicio=session.ejercicio
    	def list=Aguinaldo.findAll("from Aguinaldo a where a.ejercicio=?",[ejercicio])
        list=list.sort{a,b ->
            a.empleado.perfil.ubicacion.clave<=>b.empleado.perfil.ubicacion.clave?:a.empleado.nombre<=>b.empleado.nombre
        }
    	[aguinaldoInstanceList:list]
    }

    def actualizar(){
    	def ejercicio=session.ejercicio
    	aguinaldoService.calcular(ejercicio)
    	redirect action:'index'
    }
}
