package com.luxsoft.sw4.rh


import grails.plugin.springsecurity.annotation.Secured;

import com.luxsoft.sw4.rh.sat.*	    

@Secured(['ROLE_ADMIN','RH_USER'])
class CatalogosDelSatController {

    def index() { }

    def bancos(){
    	def rows=SatBanco.findAll()
    	model:[rows:rows]
    }
    def percepciones(){
    	def rows=SatPercepcion.findAll()
    	model:[rows:rows]
    }

    def deducciones(){
    	def rows=SatDeduccion.findAll()
    	model:[rows:rows]
    }
    def incapacidades(){
    	def rows=SatIncapacidad.findAll()
    	model:[rows:rows]
    }
    def regimenes(){
    	def rows=SatRegimenContratacion.findAll()
    	model:[rows:rows]
    }
    def riesgos(){
    	def rows=SatRiesgoPuesto.findAll()
    	model:[rows:rows]
    }
}
