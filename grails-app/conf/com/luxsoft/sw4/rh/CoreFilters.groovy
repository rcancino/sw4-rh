package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Bimestre;
import com.luxsoft.sw4.Periodo

class CoreFilters {

    def filters = {
        all(controller:'*', action:'*') {
            before = {
            	if(!session.ejercicion){
            		session.ejercicio=Periodo.obtenerYear(new Date())
            	}
				if(!session.calendarioSemana) {
					/*
					def list=CalendarioDet
					.findAll("from CalendarioDet d where d.calendario.tipo=?",['SEMANA'],[max:1])
					session.calendarioSemana=list?list[0]:null
					*/
					def a=Asistencia.find("from Asistencia a where a.tipo='SEMANAL' order by dateCreated desc")
					
					session.calendarioSemana=a?.calendarioDet
					
				}
				if(!session.calendarioQuincena) {
					/*
					def list=CalendarioDet
					.findAll("from CalendarioDet d where d.calendario.tipo=?",['QUINCENA'],[max:1])
					*/
					def a=Asistencia.find("from Asistencia a where a.tipo='QUINCENAL' order by dateCreated desc")
					session.calendarioQuincena=a?.calendarioDet
				}
				if(!session.bimestre){
					session.bimestre=Bimestre.getBimestre(new Date())
				}
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
        incentivoFilter(controller:'incentivo',action:'*'){
        	before={
        		if(!session.calendarioSemana2){
        			session.calendarioSemana2=session.calendarioSemana
        			//println 'Fijando el calendario final para semana... al: '+session.calendarioSemana

        		}
        	}
        }
    }
}
