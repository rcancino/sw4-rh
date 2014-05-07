package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Periodo

class CoreFilterFilters {

    def filters = {
		
		periodo(controller:'*',action:'*'){
			before = {
				//println 'Verificando el periodo de la session'
				if(session.periodo==null) {
					session.periodo=Periodo.getCurrentMonth() 
					//println 'Periodo: '+session.periodo
				}
				//println 'Periodo: '+session.periodo
			}
		}
		/*
        all(controller:'*', action:'*') {
            before = {
				print 'Core filter'
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
        */
    }
}
