package com.luxsoft.sw4.rh

import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef

import com.luxsoft.sw4.Periodo
import grails.gorm.DetachedCriteria
import grails.plugin.springsecurity.annotation.Secured
import grails.converters.JSON

@Secured(['ROLE_ADMIN'])
class AsistenciaController {
	
	def asistenciaService
	def checadoService
	

	def cargarAsistencia(Long calendarioDetId){
		def list=Asistencia.findAll("from Asistencia a where a.calendarioDet.id=? order by a.empleado.perfil.ubicacion.clave asc",[calendarioDetId])
		render template:'asistenciaGridPanel',model:[asistenciaInstanceList:list]
	}

    def index() {
    	def tipo=params.tipo?:'SEMANA'
		def ejercicio=session.ejercicio
    	def calendarioDet
    	if(tipo=='SEMANA'){
    		calendarioDet=session.calendarioSemana
    		
    	}else if(tipo=='QUINCENA'){
    		calendarioDet=session.calendarioQuincena
    	}
    	
    	def partidasMap=[]
    	if(calendarioDet){
    		def list=Asistencia.findAll{calendarioDet==calendarioDet}
    		partidasMap=list.groupBy([{it.empleado.perfil.ubicacion.clave}])
    	}
    	def periodos=CalendarioDet.where{calendario.ejercicio==ejercicio && calendario.tipo==tipo}.list()
    	
    	[calendarioDet:calendarioDet,partidasMap:partidasMap,tipo:tipo,periodos:periodos]
    	//redirect action:'asistenciaQuincenal'
	}
	def cambiarEjercicio(Integer ejercicio){
		//println 'Cambiando ejercicio: '+ejercicio
		session.ejercicio=ejercicio
		redirect action:'index'
	}

	def cambiarCalendario(Long calendarioDetId){
		def cal=CalendarioDet.get(calendarioDetId)
		def tipo='SEMANA'
		if(cal){
			if(cal.calendario.tipo=='SEMANA'){
				session.calendarioSemana=cal
			}else if(cal.calendario.tipo=='QUINCENA'){
				session.calendarioQuincena=cal
				tipo='QUINCENA'
			}
		}
		redirect action:'index',params:[tipo:tipo]
	}
	
	def show(Asistencia asistencia){
		def ejercicio=session.ejercicio
		def tipo=asistencia.calendarioDet.calendario.tipo
		log.info 'Cargando calendarios para: '+tipo
		def periodos=CalendarioDet.findAll{calendario.ejercicio==ejercicio && calendario.tipo==tipo}
		[asistenciaInstance:asistencia,asistenciaDetList:asistencia.partidas.sort(){it.fecha},periodos:periodos]
	}

	def delete(Asistencia asistencia){
		def res=asistenciaService.delete(asistencia)
		log.info res
		flash.message=res
		def tipo=asistencia.calendarioDet.calendario.tipo=='SEMANA'?'SEMANAL':'QUINCENAL'
		redirect view:'index', model:[tipo:tipo]
	}

	def lectora(Integer max){
		params.max = Math.min(max ?: 50, 100)
		def periodo=session.periodoDeLecturas
		if(!periodo){
			periodo=new Periodo()
			session.periodoDeLEcturas=periodo
		}
		def query=Checado.where{fecha>=periodo.fechaInicial &&
			fecha<=periodo.fechaFinal
		}
		[checadoInstanceList:query.list(params),checadoTotalCount:query.count(),periodo:periodo]

	}
	
	def actualizarPeriodoDeLecturas(Periodo periodo){
		session.periodoDeLecturas=periodo
		redirect action:'lectora'
	}
	
	def importarLecturas(Periodo periodo){
		checadoService.importarLecturas(periodo)
		session.periodoDeLecturas=periodo
		redirect action:'lectora'
	}

	def actualizarAsistencias(Long id){
		def calendarioDet=CalendarioDet.get(id)
		
		if(calendarioDet){
			log.info 'Actualizando lista de asistencias para el periodo: '+calendarioDet.asistencia
			asistenciaService.actualizarAsistencia(calendarioDet)
			redirect action:'depurar',params:[id:calendarioDet.id]
			return
		}
		
		def tipo=calendarioDet.calendario.tipo
		redirect action:'index',params:[tipo:tipo]
		
	}
	
	
	def actualizar(Long id) {
		
		def asistencia=Asistencia.get(id)
		def d=params.double('diasTrabajados')
		def f=params['faltasManuales'] as BigDecimal
		//println 'Dias trabajados: '+d
		//println params
		bindData(asistencia, params, [include: ['minutosPorDescontar']])
		if(d>=0.0){
			log.info "Actualizando dias trabajados: $d faltas manuales: $f"
			asistencia.diasTrabajados=d
			asistencia.faltasManuales=f
			asistencia=asistencia.save flush:true
			//render view:'show',model:[asistenciaInstance:asistencia,asistenciaDetList:asistencia.partidas.sort(){it.fecha}]
			redirect action:'show',params:[id:asistencia.id]
			return
		}
		
		if(asistencia){
			//asistencia.diasTrabajados=0.0
			asistencia=asistenciaService.actualizarAsistencia(asistencia)
			//render view:'show',model:[asistenciaInstance:asistencia,asistenciaDetList:asistencia.partidas.sort(){it.fecha}]
			redirect action:'show',params:[id:asistencia.id]
		}
	}	
	
	
	def nomina(Long id){
		def asistencia=Asistencia.get(id)
		def ne=NominaPorEmpleado.findByAsistencia(asistencia)
		if(ne){
			redirect controller:'nominaPorEmpleado',action:'edit',params:[id:ne.id]
		}else{
			redirect action:'show',params:[id:id]
		}
		
	}
	
	def jasperService
	
	def reporteMensual(){
		def reportDef = new JasperReportDef(
			name:'RetardoMensualPorEmpleado',
			fileFormat:JasperExportFormat.PDF_FORMAT,
			parameters:params
			)
	}

	def getEmpleadosDeAsistencia(Long id) {
		def term=params.term.trim()+'%'
		term=term.toLowerCase()
		def list=Asistencia
			.executeQuery("from Asistencia a where a.calendarioDet.id=? and (lower(a.empleado.apellidoPaterno) like ? or lower(a.empleado.apellidoMaterno) like ? or lower(a.empleado.nombres) like ?)"
				,[id,term,term,term])
		list=list.collect{ ne->
			def emp=ne.empleado
			def nombre="$emp.apellidoPaterno $emp.apellidoMaterno $emp.nombres"
			[id:ne.id
				,label:nombre
				,value:nombre
				,numeroDeTrabajador:emp.perfil.numeroDeTrabajador.trim()
			]
		}
		def res=list as JSON
		
		render res
	}
	
	def eliminarAsistencias(CalendarioDet det){
		log.info 'Eliminando asistencias para el calendario: '+det
		def found=NominaPorEmpleado.find("from NominaPorEmpleado ne where ne.nomina.calendarioDet=?",[det])
		if(found)
			flash.message="Asistencias usadas en una nomina por lo que no se puede eliminar"
		else{
			def rows=Asistencia.findAllByCalendarioDet(det)
			rows.each{a->
				a.delete flush:true
			}
			flash.message="Asistencias eliminadas"
		}
		redirect action:'index',params:params
		
	}
	
	def next(Asistencia a){
		def found=Asistencia.findByCalendarioDetAndOrden(a.calendarioDet,a.orden+1)
		if(!found){
			found=Asistencia.findByCalendarioDetAndOrden(a.calendarioDet,1)
		}
		if(found){
			redirect action:'show', params:[id:found.id]
			return
		}
		redirect action:'index'
	}
	
	def previous(Asistencia a){
		def found=Asistencia.findByCalendarioDetAndOrden(a.calendarioDet,a.orden-1)
		if(!found){
			found=Asistencia.findByCalendarioDetAndOrden(a.calendarioDet,1)
		}
		if(found){
			redirect action:'show', params:[id:found.id]
			return
		}
		redirect action:'index'
	}
	
	def depurar(CalendarioDet det){
		def list=Asistencia.findAllByCalendarioDet(det)
		list=list.sort{a,b ->
			a.empleado.perfil.ubicacion.clave<=>b.empleado.perfil.ubicacion.clave?:a.empleado.nombre<=>b.empleado.nombre
		}
		for(int i=0;i<list.size();i++){
			def a=list[i]
			a.orden=i+1
			a.save()
		}
		
		redirect action:'index'
	}
	
}
