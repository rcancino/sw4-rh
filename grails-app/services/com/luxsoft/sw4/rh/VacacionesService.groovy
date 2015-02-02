package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import grails.transaction.NotTransactional
import groovy.sql.Sql
import grails.events.Listener

import com.luxsoft.sw4.Periodo

@Transactional
class VacacionesService {
	
	def dataSource

	

    @NotTransactional
	def procesar(Asistencia asistencia){
		asistencia.vacaciones=0
		asistencia.partidas.each{
			def found=Vacaciones.find("from Vacaciones v where v.empleado=? and ? in elements(v.dias) ",[asistencia.empleado,it.fecha])
			if(found){
				it.comentario='VACACIONES'
				it.tipo='VACACIONES'
				asistencia.vacaciones++
			}
			def pagadas=Vacaciones.find("from Vacaciones v where v.empleado=? and v.pg=true and v.acreditada=false and v.calendarioDet=?"
				,[asistencia.empleado,asistencia.calendarioDet])
			if(pagadas){
				asistencia.vacacionesp=pagadas.diasPagados
				pagadas.acreditada=true
			}
		}
		def res=asistencia.faltas-asistencia.vacaciones
		asistencia.faltas=res>0?res:0
		
	}
	
	def generarControl(Integer ejercicio){
		
		def periodo=Periodo.getPeriodoAnual(ejercicio)
		def query=controlAltaSql.replaceAll('@FECHA_INI',periodo.fechaInicial.format('yyyy/MM/dd'))
		log.info query
		Sql sql=new Sql(dataSource)
		sql.eachRow(query){row-> 
			log.info 'Procesando: '+ row
			def empleado=Empleado.findById(row.id)
			def ca=new ControlDeVacaciones(
					empleado:empleado,
					ejercicio:ejercicio,
					acumuladoExcento:0,
					acumuladoGravado:0,
					antiguedadDias:row.antiguedadDias,
					antiguedadYears:row.years,
					diasVacaciones:row.diasVacaciones,
					diasTomados:row.tomados,
					aniversario:row.aniversario)
			ca.save failOnError:true
		}
		
		
	}
	
	
	@grails.events.Listener(topic='VacacionesTopic')
	def actualizarControl(Long id) {
		def vacaciones=Vacaciones.get(id)
		if(vacaciones.control){
			actualizarControl(vacaciones.control)
		}

		log.info 'Actualizando control de vacaciones....'
	}

	def actualizarControl(ControlDeVacaciones control){
		def vacaciones=Vacaciones.findAllByControl(control)
		def pagados=vacaciones.sum 0,{it.diasPagados}
		def normales=vacaciones.sum 0,{it.dias.size()}
		control.diasTomados=normales
		control.diasPagados=pagados
		control.save failOnError:true
	}


	def actualizarPrimaVacacional(Nomina nomina){
		def found=nomina.partidas.findAll{it}
	}



	
	@Listener(namespace='gorm')
	def afterInsert(Empleado e){
		log.info 'Nuevo empleado: '+e
	}
	
	/*
	@Listener(topic = 'afterUpdate', namespace='gorm')
	def afterUdate(Vacaciones vacaciones){
		log.info 'Modificacion de vacaciones: '+vacaciones
		def found=ControlDeVacaciones.findByEmpleadoAndEjercicio(vacaciones.empleado,2014)
		if(found){
			log.info 'Actualizando control de vacaciones..'
			found.diasTomados=vacaciones.dias.size()
			//found.save()
		}
	}
	*/
	
	String controlAltaSql="""
		SELECT YEAR('@FECHA_INI') AS ejercicio,e.alta,e.status,E.id,E.clave,P.numero_de_trabajador,CONCAT(E.apellido_paterno,' ',E.apellido_materno,' ',E.nombres) AS NOMBRE,P.jornada,s.periodicidad
,date(concat(YEAR('2014/01/01'),'-',month(e.alta),'-',day(e.alta))) as aniversario
,(SELECT U.descripcion FROM ubicacion U WHERE U.ID=P.ubicacion_id) AS ubicacion
,(SELECT U.descripcion FROM departamento U WHERE U.ID=P.departamento_id) AS departamento
,(floor(((-(TIMESTAMPDIFF(MINUTE,DATE(MAX( CASE WHEN YEAR(E.ALTA)=YEAR('@FECHA_INI') THEN E.ALTA ELSE '@FECHA_INI' END )),E.ALTA)/60)/24)/365))+1) AS years
,ROUND(-(TIMESTAMPDIFF(MINUTE,DATE(MAX( CASE WHEN YEAR(E.ALTA)=YEAR('@FECHA_INI') THEN E.ALTA ELSE '@FECHA_INI' END )),E.ALTA)/60)/24,0)  AS antiguedadDias
,(SELECT MIN(F.VAC_DIAS) FROM factor_de_integracion F 	WHERE 
	(floor(((-(TIMESTAMPDIFF(MINUTE,DATE(MAX( CASE WHEN YEAR(E.ALTA)=YEAR('@FECHA_INI') THEN E.ALTA ELSE '@FECHA_INI' END )),E.ALTA)/60)/24)/365))+1) 
	BETWEEN F.YEAR_DE AND F.YEAR_HASTA ) AS diasVacaciones
,(SELECT count(*) FROM vacaciones v join vacaciones_dias d on(d.vacaciones_id=v.id) where v.empleado_id=e.id) as tomados
,(SELECT MIN(F.VAC_DIAS) FROM factor_de_integracion F 	WHERE (floor(((-(TIMESTAMPDIFF(MINUTE,DATE(MAX( CASE WHEN YEAR(E.ALTA)=YEAR('@FECHA_INI') THEN E.ALTA ELSE '@FECHA_INI' END )),E.ALTA)/60)/24)/365))+1) BETWEEN F.YEAR_DE AND F.YEAR_HASTA ) -
	(SELECT count(*) FROM vacaciones v join vacaciones_dias d on(d.vacaciones_id=v.id) where v.empleado_id=e.id) as xtomar
FROM empleado E 
JOIN perfil_de_empleado P ON(P.empleado_id=E.ID)
JOIN salario S ON(S.empleado_id=E.id)
LEFT JOIN baja_de_empleado B ON(E.ID=B.empleado_id)
WHERE YEAR(E.ALTA)<YEAR('@FECHA_INI') and (B.ID IS NULL OR YEAR(B.FECHA)>=YEAR('@FECHA_INI') )
GROUP BY E.ID
order by (SELECT U.descripcion FROM ubicacion U WHERE U.ID=P.ubicacion_id),e.clave
	"""
}
