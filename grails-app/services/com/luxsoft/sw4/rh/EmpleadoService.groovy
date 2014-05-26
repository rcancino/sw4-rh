package com.luxsoft.sw4.rh

import grails.plugin.cache.Cacheable;
import grails.transaction.Transactional
import grails.validation.ValidationException
import groovy.sql.Sql;

import org.apache.commons.lang.exception.ExceptionUtils

import com.luxsoft.sw4.Periodo;

@Transactional
class EmpleadoService {
	
	def dataSource
	
	@Cacheable('catalogoDeEmpleados')
	def getEmpleados() {
		return Empleado.findAll{status=='ALTA'}
	}
	
	
	
	Empleado save(Empleado empleado) {
		empleado.status='ALTA'
		empleado.validate()
		if(empleado.hasErrors()) {
			throw new EmpleadoException(message:'Error de validacion',empleado:empleado)
		}
		empleado.save failOnError:true
		return empleado
	}

    Empleado updateEmpleado(Empleado empleado) {
    	
    	//Salvando empleado
    	try{
			if(empleado.status!='BAJA'){
				empleado.baja=null
			}
    		empleado.save(failOnError:true)
    		return empleado
    		}catch(Exception ex){
    			throw new EmpleadoException(
    				message:ExceptionUtils.getRootCauseMessage(ex),
    				empleado:empleado
    				)
    		}
    }
	
	/**
	 * Proceso para actualizar el salario diario integrado
	 * 
	 * @return
	 */
	def calcularSalarioDiarioIntegrado(Periodo periodo){
		log.info 'Calculando salario diari integrado para: '+periodo
		def query=sdiSQL.replaceAll('@FECHA_INI',periodo.fechaInicial.format('yyyy/MM/dd'))
			.replaceAll('@FECHA_FIN',periodo.fechaFinal.format('yyyy/MM/dd'))
		Sql sql=new Sql(dataSource)
		return sql.rows(query)
	}
	
	def actualizarSalarioDiarioIntegrado(Periodo periodo){
		log.info 'Actualizando salario diari integrado para: '+periodo
		def query=sdiSQL.replaceAll('@FECHA_INI',periodo.fechaInicial.format('yyyy/MM/dd'))
			.replaceAll('@FECHA_FIN',periodo.fechaFinal.format('yyyy/MM/dd'))
		Sql sql=new Sql(dataSource)
		def res=[]
		sql.eachRow(query){ row ->
			def empleado=Empleado.findByClave(row.CLAVE)
			if(empleado){
				def old=empleado.salario.salarioDiarioIntegrado
				empleado.salario.salarioDiarioIntegrado=row.SDI_NVO
				empleado.save(flush:true)
				res.add(empleado)
				log.info "SDI actuaizado para ${empleado} SDI anterior: ${old} SDI nvo: ${empleado.salario.salarioDiarioIntegrado}"
			}
		}
	}
	
	String sdiSQL="""
		SELECT X.STATUS,x.ID,X.CLAVE,x.NOMBRES,x.APELLIDO_PATERNO,X.APELLIDO_MATERNO,X.ALTA,N.PERIODICIDAD,MIN(N.PERIODO_FECHA_INICIAL) AS FECHA_INI,MAX(N.PERIODO_FECHA_FINAL) AS FECHA_FIN,S.SALARIO_DIARIO AS SDB,S.SALARIO_DIARIO_INTEGRADO AS SDI_CAT
,ROUND((-(TIMESTAMPDIFF(MINUTE,DATE(MAX( CASE WHEN X.ALTA=X.ALTA THEN N.PERIODO_FECHA_FINAL ELSE '@FECHA_INI' END )),X.ALTA)/60)/24)/365,0)  AS YEARS
,ROUND(-(TIMESTAMPDIFF(MINUTE,DATE(MAX( CASE WHEN X.ALTA=X.ALTA THEN N.PERIODO_FECHA_FINAL ELSE '@FECHA_INI' END )),X.ALTA)/60)/24,0)  AS DIAS
,(SELECT MAX(F.VAC_DIAS) FROM factor_de_integracion F 	WHERE ROUND(((-(TIMESTAMPDIFF(MINUTE,DATE(MAX( CASE WHEN X.ALTA=X.ALTA THEN N.PERIODO_FECHA_FINAL ELSE '@FECHA_INI' END )),X.ALTA)/60)/24)/365),0) BETWEEN F.YEAR_DE AND F.YEAR_HASTA ) AS VAC_DIAS
,(SELECT MAX(F.VAC_PRIMA) FROM factor_de_integracion F 	WHERE ROUND(((-(TIMESTAMPDIFF(MINUTE,DATE(MAX( CASE WHEN X.ALTA=X.ALTA THEN N.PERIODO_FECHA_FINAL ELSE '@FECHA_INI' END )),X.ALTA)/60)/24)/365),0) BETWEEN F.YEAR_DE AND F.YEAR_HASTA ) AS VAC_PRIMA
,(SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_DIAS WHEN  N.periodicidad='SEMANA' THEN F.SEM_DIAS ELSE F.QNA_DIAS END) FROM factor_de_integracion F 	WHERE ROUND(((-(TIMESTAMPDIFF(MINUTE,DATE(MAX( CASE WHEN X.ALTA=X.ALTA THEN N.PERIODO_FECHA_FINAL ELSE '@FECHA_INI' END )),X.ALTA)/60)/24)/365),0) BETWEEN F.YEAR_DE AND F.YEAR_HASTA ) AS AGNDO_DIAS
,(SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  N.periodicidad='SEMANA' THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) FROM factor_de_integracion F WHERE ROUND((-(TIMESTAMPDIFF(MINUTE,DATE(MAX( CASE WHEN X.ALTA=X.ALTA THEN N.PERIODO_FECHA_FINAL ELSE '@FECHA_INI' END )),X.ALTA)/60)/24),0) BETWEEN F.DIAS_DE AND F.DIAS_HASTA ) AS FACTOR
,e.salario_diario_base*(SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  N.periodicidad='SEMANA' THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) FROM factor_de_integracion F WHERE ROUND((-(TIMESTAMPDIFF(MINUTE,DATE(MAX( CASE WHEN X.ALTA=X.ALTA THEN N.PERIODO_FECHA_FINAL ELSE '@FECHA_INI' END )),X.ALTA)/60)/24),0) BETWEEN F.DIAS_DE AND F.DIAS_HASTA ) AS SDI_F
,ROUND(((-(TIMESTAMPDIFF(MINUTE,DATE(MAX(N.PERIODO_FECHA_FINAL)),MIN(N.PERIODO_FECHA_INICIAL))/60)/24)+1),0) AS DIAS_BIM
,SUM(E.FALTAS) AS FALTAS,SUM(E.INCAPACIDADES) AS INCAPACIDADES
,ROUND(((-(TIMESTAMPDIFF(MINUTE,DATE(MAX(N.PERIODO_FECHA_FINAL)),MIN(N.PERIODO_FECHA_INICIAL))/60)/24)+1),0)  -SUM(E.FALTAS)-SUM(E.INCAPACIDADES) AS DIAS_LAB_BIM
,IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(22,41,19,42,24,20) AND D.ID IS NOT NULL)),0) AS VARIABLE
,IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(22,41,19,42,24,20) AND D.ID IS NOT NULL)),0)/ROUND(((-(TIMESTAMPDIFF(MINUTE,DATE(MAX(N.PERIODO_FECHA_FINAL)),MIN(N.PERIODO_FECHA_INICIAL))/60)/24)+1),0)  -SUM(E.FALTAS)-SUM(E.INCAPACIDADES) AS VAR_DIA
,ROUND((e.salario_diario_base*(SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  N.periodicidad='SEMANA' THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) FROM factor_de_integracion F WHERE ROUND((-(TIMESTAMPDIFF(MINUTE,DATE(MAX( CASE WHEN X.ALTA=X.ALTA THEN N.PERIODO_FECHA_FINAL ELSE '@FECHA_INI' END )),X.ALTA)/60)/24),0) BETWEEN F.DIAS_DE AND F.DIAS_HASTA ) +
IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(22,41,19,42,24,20) AND D.ID IS NOT NULL)),0)/ROUND(((-(TIMESTAMPDIFF(MINUTE,DATE(MAX(N.PERIODO_FECHA_FINAL)),MIN(N.PERIODO_FECHA_INICIAL))/60)/24)+1),0)  -SUM(E.FALTAS)-SUM(E.INCAPACIDADES)),2) AS SDI_NVO
FROM NOMINA N 
JOIN nomina_por_empleado E ON(E.nomina_id=N.ID)
JOIN empleado X ON(X.ID=E.empleado_id)
JOIN salario S ON(S.EMPLEADO_ID=X.ID)
WHERE date(n.periodo_fecha_inicial)>='@FECHA_INI' and date(n.periodo_fecha_final)<='@FECHA_FIN'  AND X.ALTA=X.ALTA
GROUP BY X.ID
	"""

}




class EmpleadoException extends RuntimeException{
	String message
	Empleado empleado
}