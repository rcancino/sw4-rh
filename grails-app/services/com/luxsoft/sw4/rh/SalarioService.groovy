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
		salario.salarioDiarioIntegrado=modificacion.sdiNuevo
    	log.info "${modificacion} detectada Salario ${salario} actualizado"

    }

   @Listener(namespace='gorm')
    def beforeDelete(ModificacionSalarial modificacion){
    	
    	def salario=modificacion.empleado.salario
    	salario.salarioDiario=modificacion.salarioAnterior
    	log.info "${modificacion} detectada Salario ${salario} actualizado"
    }

    @NotTransactional
    def calcularSalarioDiarioIntegrado(Empleado empleado, Date fecha,def salarioNuevo){
		log.info 'Calculando salario diari integrado para: '+empleado+ 'fecha:' +fecha+'  Salario nuevo: '+salarioNuevo
		
		def val=CalendarioDet.executeQuery("select min(d.bimestre) from CalendarioDet d where date(?) between d.inicio and d.fin",[fecha])
		
		def bimestre=val.get(0)-1
		def res=CalendarioDet.executeQuery("select min(d.inicio),max(d.fin) from CalendarioDet d where d.bimestre=?",[bimestre])
		
		def inicio=res.get(0)[0]
		def fin=res.get(0)[1]
		log.info "Periodo: $inicio al $fin"
		
		def query=sdiPorEmpleado.replaceAll('@FECHA_INI',inicio.format('yyyy/MM/dd'))
			.replaceAll('@FECHA_FIN',fin.format('yyyy/MM/dd'))
			.replaceAll('@TIPO', empleado.salario.periodicidad)
			.replaceAll('@SALARIO', salarioNuevo.toString())
			//println query
		Sql sql=new Sql(dataSource)
		def rows= sql.rows(query,[empleado.id])
	}
	
	String sdiPorEmpleado="""
		SELECT (CASE WHEN (25)*(SELECT Z.SALARIO FROM zona_economica Z WHERE Z.CLAVE='A' )<=						
	( ROUND( ( ((SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  S.periodicidad='@TIPO' THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 					
	FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR(N.PERIODO_FECHA_FINAL) AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR(N.PERIODO_FECHA_FINAL) THEN 0 ELSE 2 END) AND 					
	ROUND((-(TIMESTAMPDIFF(MINUTE,DATE(MAX( CASE WHEN B.FECHA<N.PERIODO_FECHA_FINAL AND B.FECHA>X.ALTA THEN B.FECHA ELSE N.PERIODO_FECHA_FINAL END )),X.ALTA)/60)/24),0) BETWEEN F.DIAS_DE AND F.DIAS_HASTA )					
	*	@SALARIO) 	+			
	( IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(22,41,19,42,24,20,44) AND D.ID IS NOT NULL)),0)/(ROUND(((-(TIMESTAMPDIFF(MINUTE,DATE(MAX( CASE WHEN B.FECHA<N.PERIODO_FECHA_FINAL AND B.FECHA>X.ALTA THEN B.FECHA ELSE N.PERIODO_FECHA_FINAL END )),MIN(N.PERIODO_FECHA_INICIAL))/60)/24)+1),0)  -SUM(E.FALTAS)-SUM(E.INCAPACIDADES)) ) )	,2) )			
		THEN		 (25)*(SELECT Z.SALARIO FROM zona_economica Z WHERE Z.CLAVE='A' ) 		ELSE 
	( ROUND( ( ((SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  S.periodicidad='@TIPO' THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 					
	FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR(N.PERIODO_FECHA_FINAL) AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR(N.PERIODO_FECHA_FINAL) THEN 0 ELSE 2 END) AND 					
	ROUND((-(TIMESTAMPDIFF(MINUTE,DATE(MAX( CASE WHEN B.FECHA<N.PERIODO_FECHA_FINAL AND B.FECHA>X.ALTA THEN B.FECHA ELSE N.PERIODO_FECHA_FINAL END )),X.ALTA)/60)/24),0) BETWEEN F.DIAS_DE AND F.DIAS_HASTA )					
	*	@SALARIO) 	+			
	( IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(22,41,19,42,24,20,44) AND D.ID IS NOT NULL)),0)/(ROUND(((-(TIMESTAMPDIFF(MINUTE,DATE(MAX( CASE WHEN B.FECHA<N.PERIODO_FECHA_FINAL AND B.FECHA>X.ALTA THEN B.FECHA ELSE N.PERIODO_FECHA_FINAL END )),MIN(N.PERIODO_FECHA_INICIAL))/60)/24)+1),0)  -SUM(E.FALTAS)-SUM(E.INCAPACIDADES)) ) )	,2) )			
		END ) AS SDI_NVO				
		FROM NOMINA N 						
		JOIN nomina_por_empleado E ON(E.nomina_id=N.ID)
		JOIN empleado X ON(X.ID=E.empleado_id)
		JOIN salario S ON(S.EMPLEADO_ID=X.ID)
		LEFT JOIN baja_de_empleado B ON(B.empleado_id=X.id)
		WHERE X.ID=? AND 
		date(n.periodo_fecha_inicial)>='@FECHA_INI' and date(n.periodo_fecha_final)<='@FECHA_FIN'  AND (B.ID IS NULL OR ( X.ALTA>DATE(B.FECHA) AND X.STATUS<>'BAJA') OR DATE(B.FECHA)>N.PERIODO_FECHA_INICIAL)
		GROUP BY X.ID
		
	"""


    String sdiPorEmpleadoNuevo="""		 
SELECT x.ID,X.CLAVE,x.NOMBRES,x.APELLIDO_PATERNO,X.APELLIDO_MATERNO,X.ALTA,X.STATUS,DATE(B.FECHA) AS F_BAJA --,S.PERIODICIDAD ,S.SALARIO_DIARIO*1 AS SDB,S.SALARIO_DIARIO_INTEGRADO*1 AS SDI_CAT		
,(SELECT Z.SALARIO FROM zona_economica Z WHERE Z.CLAVE='A' ) AS SMG
,(25)*(SELECT Z.SALARIO FROM zona_economica Z WHERE Z.CLAVE='A' ) AS TOPE_SMG
,ROUND((-(TIMESTAMPDIFF(MINUTE,DATE(MAX( CASE WHEN B.FECHA<DATE(NOW()) AND B.FECHA>X.ALTA THEN B.FECHA ELSE DATE(NOW()) END )),X.ALTA)/60)/24)/365,0)  AS YEARS
,ROUND(-(TIMESTAMPDIFF(MINUTE,DATE(MAX( CASE WHEN B.FECHA<DATE(NOW())  AND B.FECHA>X.ALTA THEN B.FECHA ELSE DATE(NOW()) END )),X.ALTA)/60)/24,0)  AS DIAS
,(SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  S.periodicidad='@TIPO' THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 		
	FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR(DATE(NOW())) AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR(DATE(NOW())) THEN 0 ELSE 2 END) AND 	
	ROUND((-(TIMESTAMPDIFF(MINUTE,DATE(MAX( CASE WHEN B.FECHA<DATE(NOW()) AND B.FECHA>X.ALTA THEN B.FECHA ELSE DATE(NOW()) END )),X.ALTA)/60)/24),0) BETWEEN F.DIAS_DE AND F.DIAS_HASTA ) AS FACTOR	
,((SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  S.periodicidad='@TIPO' THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 		
	FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR(DATE(NOW())) AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR(DATE(NOW())) THEN 0 ELSE 2 END) AND 	
	ROUND((-(TIMESTAMPDIFF(MINUTE,DATE(MAX( CASE WHEN B.FECHA<DATE(NOW()) AND B.FECHA>X.ALTA THEN B.FECHA ELSE DATE(NOW()) END )),X.ALTA)/60)/24),0) BETWEEN F.DIAS_DE AND F.DIAS_HASTA )	
	*	@SALARIO) AS SDI_F
,(CASE WHEN (25)*(SELECT Z.SALARIO FROM zona_economica Z WHERE Z.CLAVE='A' )<=						
	( ROUND( ( ((SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  S.periodicidad='@TIPO' THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 					
	FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR(DATE(NOW())) AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR(DATE(NOW())) THEN 0 ELSE 2 END) AND 					
	ROUND((-(TIMESTAMPDIFF(MINUTE,DATE(MAX( CASE WHEN B.FECHA<DATE(NOW()) AND B.FECHA>X.ALTA THEN B.FECHA ELSE DATE(NOW()) END )),X.ALTA)/60)/24),0) BETWEEN F.DIAS_DE AND F.DIAS_HASTA )					
	*	@SALARIO) 	)	,2) )			
		THEN		 (25)*(SELECT Z.SALARIO FROM zona_economica Z WHERE Z.CLAVE='A' ) 		ELSE 
	( ROUND( ( ((SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  S.periodicidad='@TIPO' THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 					
	FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR(DATE(NOW())) AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR(DATE(NOW())) THEN 0 ELSE 2 END) AND 					
	ROUND((-(TIMESTAMPDIFF(MINUTE,DATE(MAX( CASE WHEN B.FECHA<DATE(NOW()) AND B.FECHA>X.ALTA THEN B.FECHA ELSE DATE(NOW()) END )),X.ALTA)/60)/24),0) BETWEEN F.DIAS_DE AND F.DIAS_HASTA )					
	*	@SALARIO)	)	,2) )			
		END ) AS SDI_NVO		
FROM EMPLEADO X
LEFT JOIN salario S ON(S.EMPLEADO_ID=X.ID)
LEFT JOIN baja_de_empleado B ON(B.empleado_id=X.id)
WHERE X.ID=?
	"""

	String bimestreSql="""
		SELECT MIN(INICIO) AS INI,MAX(FIN) AS FIN
		FROM calendario_det D JOIN calendario C ON(C.ID=D.CALENDARIO_ID)
		WHERE C.EJERCICIO=2014 AND BIMESTRE=4
	"""

}

