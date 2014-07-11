package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import grails.transaction.NotTransactional
import grails.events.Listener
import groovy.sql.Sql

@Transactional
class SalarioService {

	def dataSource

	@Listener(namespace='gorm')
    def afterInsert(ModificacionSalarial modificacion) {
    	
    	def salario=modificacion.empleado.salario
    	salario.salarioDiario=modificacion.salarioNuevo
    	log.info "${modificacion} detectada Salario ${salario} actualizado"

    }

   @Listener(namespace='gorm')
    def beforeDelete(ModificacionSalarial modificacion){
    	
    	def salario=modificacion.empleado.salario
    	salario.salarioDiario=modificacion.salarioAnterior
    	log.info "${modificacion} detectada Salario ${salario} actualizado"
    }

    @NotTransactional
    def calcularSalarioDiarioIntegrado(Empleado empleado){
		log.info 'Calculando salario diari integrado para: '+periodo
		def query=sdiPorEmpleado.replaceAll('@FECHA_INI',periodo.fechaInicial.format('yyyy/MM/dd'))
			.replaceAll('@FECHA_FIN',periodo.fechaFinal.format('yyyy/MM/dd'))
		Sql sql=new Sql(dataSource,[empleado.id])
		return sql.rows(query)
	}


    String sdiPorEmpleado="""
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
	WHERE X.ID=? AND date(n.periodo_fecha_inicial)>='@FECHA_INI' and date(n.periodo_fecha_final)<='@FECHA_FIN'  AND X.ALTA=X.ALTA
	GROUP BY X.ID
	"""

	String bimestreSql="""
		SELECT MIN(INICIO) AS INI,MAX(FIN) AS FIN
		FROM calendario_det D JOIN calendario C ON(C.ID=D.CALENDARIO_ID)
		WHERE C.EJERCICIO=2014 AND BIMESTRE=4
	"""

}

