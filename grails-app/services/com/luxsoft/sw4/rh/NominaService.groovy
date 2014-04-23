package com.luxsoft.sw4.rh

import com.luxsoft.sw4.Empresa;
import com.luxsoft.sw4.Periodo;

import grails.transaction.Transactional
import groovy.time.TimeCategory;

@Transactional
class NominaService {

    def generarNominas(String tipo,String periodicidad) {
		
		def year=Calendar.getInstance().get(Calendar.YEAR)
		
		
		switch(periodicidad){
			
			case 'QUINCENAL':
				def periodo=Periodo.getPeriodoAnual(year)
				Date fecha=periodo.fechaInicial
				Integer folio=1
				(0..11).each{ 
					def mes=Periodo.getPeriodoEnUnMes(it,year)
					def q1=new Periodo(fechaInicial:mes.fechaInicial,fechaFinal:mes.fechaInicial+14)
					log.info "Generando nomina quincenale periodo: "+q1
					generarNomina(tipo,periodicidad,q1,folio++)
					def q2=new Periodo(fechaInicial:q1.fechaFinal+1,fechaFinal:mes.fechaFinal)
					log.info "Generando nomina quincenale periodo: "+q2
					generarNomina(tipo,periodicidad,q2,folio++)
					
				}
				break
			case 'SEMANAL':
				def periodo=Periodo.getPeriodoAnual(year)
				log.info 'Generando nominas semanales periodo: '+periodo
				Date fecha=periodo.fechaInicial
				
				def folio=1
				
				while(fecha<periodo.fechaFinal){
					def fechaFinal=fecha+7
					if(folio==1)
						fechaFinal=Date.parse('dd/MM/yyyy','05/01/2014')
					def per=new Periodo(fechaInicial:fecha+1,fechaFinal:fechaFinal)
					
					
					
					if(folio==52){
						per.fechaFinal=periodo.fechaFinal
						log.info "ULITMA SEMANA Semana ${folio} "+per
						generarNomina(tipo,periodicidad,per,folio++)
						break
					}
					log.info "Semana ${folio} "+per
					generarNomina(tipo,periodicidad,per,folio++)
					
					fecha=fechaFinal
					//folio++
				}
				break
			default:
				break
		}
    }
	
	private Nomina generarNomina(String tipo,String periodicidad,Periodo periodo,Integer folio){
		
		Empresa empresa=Empresa.first()
		assert empresa,'No existe la empresa del sistema'
		def nomina=Nomina.findWhere(empresa:empresa,tipo:tipo,periodicidad:periodicidad,folio:folio)
		if(!nomina){
			//println 'Nomina ya existente.....+'+nomina
			nomina=new Nomina(tipo:tipo,periodicidad:periodicidad,folio:folio)
			nomina.status="PENDIENTE"
			nomina.periodo=periodo
			nomina.pago=periodo.fechaFinal
			nomina.diaDePago='JUEVES'
			nomina.formaDePago='TRANSFERENCIA'
			nomina.empresa=empresa
			nomina.total=0.0
			nomina.save(failOnError:true)
			
			return nomina
		}
	}
}
