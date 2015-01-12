package com.luxsoft.sw4.rh

import org.springframework.security.access.annotation.Secured
import grails.transaction.Transactional
import grails.converters.JSON

@Secured(["hasAnyRole('ROLE_ADMIN','RH_USER')"])
@Transactional(readOnly = true)
class CalendarioDetController {
    static scaffold = true

    def show(CalendarioDet calendarioDetInstance){
    	redirect controller:'calendario',action:'edit',params:[id:calendarioDetInstance.calendario.id]
    }

    def getCalendariosAsJSON() {
		def term=params.term.trim()+'%'
		
		/*
		def query=CalendarioDet.where{
			calendario.tipo=~term || folio.toString()=~term 
		}
		def list=query.list(max:100, sort:"folio",order:'desc')
		*/
		def list=CalendarioDet
			.findAll("from CalendarioDet c where c.calendario.ejercicio=? and c.calendario.tipo like ? order by c.folio desc "
				,[session.ejercicio,term.toUpperCase()])
		//println 'Buscando por: '+term
		//println list.size()
		list=list.collect{ calDet->
			def nombre="$calDet.calendario.tipo $calDet.folio  $calDet.calendario.ejercicio"
			[id:calDet.id
				,label:nombre
				,value:nombre
			]
		}
		def res=list as JSON
		
		render res
	}
}
