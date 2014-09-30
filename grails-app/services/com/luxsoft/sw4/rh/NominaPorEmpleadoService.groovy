package com.luxsoft.sw4.rh

import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;

import grails.transaction.Transactional
import grails.transaction.NotTransactional
import grails.events.Listener

@Transactional
class NominaPorEmpleadoService {
	
	def static CONCEPTOS_BASICOS=['P001','D001','D002']
	
	def procesadorDeNomina
	
	def prestamoService
	
	@Transactional
	def eliminar(Long id){
		NominaPorEmpleado ne=NominaPorEmpleado.get(id)
		log.info 'Eliminar nomina por empleado: '+ne		
		prestamoService.beforeDelete(ne)
		def nomina=ne.nomina
		nomina.removeFromPartidas(ne)
		nomina.save flush:true		
		return nomina
	}
	
	@Transactional
	def eliminarConcepto(Long id){
		
		NominaPorEmpleadoDet det=NominaPorEmpleadoDet.get(id)
		log.debug 'Eliminando concepto: '+det
		prestamoService.beforeDelete(det)
		prestamoService
		def ne=det.parent
		def target=ne.conceptos.find(){it.id==id}
		log.debug 'Found: '+target
		ne.removeFromConceptos(target)
		ne.save()
		return ne
	}
	
	
	@Transactional
	def actualizarNominaPorEmpleado(NominaPorEmpleado ne) {
		return procesadorDeNomina.procesar(ne)
	}
	
	@Transactional
	def actualizarNominaPorEmpleado(Long id) {
		NominaPorEmpleado ne=NominaPorEmpleado.get(id)
		return procesadorDeNomina.procesar(ne)
	}
	
	/*
	@Listener(namespace='gorm')
	def afterUpdate(NominaPorEmpleado ne){
		def togo=[]
		ne.conceptos.each{
			if(it.total<=0.0){
				togo.add(it)
			}
		}
		println 'Eliminar: '+togo.size()
		togo.each{
			//println 'Eliminando: '+it+' Idx:'+ne.conceptos.indexOf(it)
			
		}
	}
	*/
	
	
		
}
