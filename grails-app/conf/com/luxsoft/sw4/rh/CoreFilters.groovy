package com.luxsoft.sw4.rh

class CoreFilters {

    def filters = {
        all(controller:'*', action:'*') {
            before = {
				if(!session.calendarioSemana) {
					
					def list=CalendarioDet
					.findAll("from CalendarioDet d where d.calendario.tipo=?",['SEMANA'],[max:1])
					session.calendarioSemana=list?list[0]:null
				}
				if(!session.calendarioQuincena) {
					def list=CalendarioDet
					.findAll("from CalendarioDet d where d.calendario.tipo=?",['QUINCENA'],[max:1])
					session.calendarioQuincena=list?list[0]:null
				}
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
