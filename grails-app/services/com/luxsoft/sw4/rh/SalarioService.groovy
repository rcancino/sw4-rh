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
	def calcularSalarioDiario(int ejercicio,int bimestre){
		
		def rows=[]
		["QUINCENA","SEMANA"].each{
			
			log.info "SDI para $ejercicio  bimestre $bimestre tipo $it"
			
			def res=CalendarioDet
			.executeQuery("select min(d.inicio),max(d.fin) from CalendarioDet d where d.bimestre=? and d.calendario.tipo=? and d.calendario.ejercicio=?"
			,[bimestre,it,ejercicio])
		
		def inicio=res.get(0)[0]
		def fin=res.get(0)[1]
		log.info "Periodo: $inicio al $fin"
		
		def query=sqlPorBimestre
			.replaceAll('@FECHA_INI',inicio.format('yyyy/MM/dd'))
			.replaceAll('@FECHA_FIN',fin.format('yyyy/MM/dd'))
			.replaceAll('@FECHA_ULT_MODIF',fin.format('yyyy/MM/dd'))
			.replaceAll('@TIPO', it+'L')
			println query
		Sql sql=new Sql(dataSource)
		rows.addAll(sql.rows(query))
		}
		rows=rows.sort{a,b ->
				a.ubicacion<=>b.ubicacion?:a.APELLIDO_PATERNO<=>b.APELLIDO_PATERNO
			}
		//def rows= sql.rows(query)
		return rows
	}

    @NotTransactional
    def calcularSalarioDiarioIntegrado(Empleado empleado, Date fecha,def salarioNuevo,def ejercicio){
		log.info 'Calculando salario diari integrado para: '+empleado+ 'fecha:' +fecha+'  Salario nuevo: '+salarioNuevo
		
		
		def val=CalendarioDet.executeQuery("select min(d.bimestre) from CalendarioDet d where date(?) between d.inicio and d.fin",[fecha])
		def bimestre=val.get(0)-1
		def tipo=empleado.salario.periodicidad=='SEMANAL'?'SEMANA':'QUINCENA'
		def res=CalendarioDet
			.executeQuery("select min(d.inicio),max(d.fin) from CalendarioDet d where d.bimestre=? and d.calendario.tipo=? and d.calendario.ejercicio=?"
			,[bimestre,tipo,ejercicio])
		
		def inicio=res.get(0)[0]
		def fin=res.get(0)[1]
		log.info "Periodo: $inicio al $fin"
		
		def query=sdiPorEmpleado
			.replaceAll('@FECHA_INI',inicio.format('yyyy/MM/dd'))
			.replaceAll('@FECHA_FIN',fin.format('yyyy/MM/dd'))
			.replaceAll('@FECHA_ULT_MODIF',fecha.format('yyyy/MM/dd'))
			.replaceAll('@TIPO', empleado.salario.periodicidad)
			.replaceAll('@SALARIO', salarioNuevo.toString())
			//println query
		Sql sql=new Sql(dataSource)
		def rows= sql.rows(query,[empleado.id])
	}
	
	
	@NotTransactional
	def calcularSalarioDiarioIntegradoNuevo(Empleado empleado,def salarioNuevo,String periodicidad){
		
		log.info 'Calculando salario diari integrado para: '+empleado+ '  Salario nuevo: '+salarioNuevo+ ' Tipo: '+periodicidad
		
		def query=sdiPorEmpleadoNuevo
			.replaceAll('@TIPO', periodicidad)
			.replaceAll('@SALARIO', salarioNuevo.toString())
			//println query
		Sql sql=new Sql(dataSource)
		def rows= sql.rows(query,[empleado.id])
	}
	
	String sdiPorEmpleado="""
SELECT (CASE WHEN (25)*(SELECT Z.SALARIO FROM zona_economica Z WHERE Z.CLAVE='A' )<=						
	( ROUND( ( ( (SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  S.periodicidad='@TIPO' THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 		
		FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') THEN 0 ELSE 2 END) AND 	
		ROUND((-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24),0) BETWEEN F.DIAS_DE AND F.DIAS_HASTA )		*	 @SALARIO )	+			
	( IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(19,22,24,34,35,41,42,44) AND D.ID IS NOT NULL)),0) /	(ROUND(((-(TIMESTAMPDIFF(MINUTE,'@FECHA_FIN',(CASE WHEN X.ALTA>'@FECHA_INI' THEN X.ALTA ELSE '@FECHA_INI' END))/60)/24)+1),0)  -SUM(E.FALTAS)-SUM(E.INCAPACIDADES)) ) ),2) )			
		THEN		 (25)*(SELECT Z.SALARIO FROM zona_economica Z WHERE Z.CLAVE='A' ) 		ELSE 
	( ROUND( ( ( (SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  S.periodicidad='@TIPO' THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 		
		FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') THEN 0 ELSE 2 END) AND 	
		ROUND((-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24),0) BETWEEN F.DIAS_DE AND F.DIAS_HASTA )		*	 @SALARIO )	+			
	( IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(19,22,24,34,35,41,42,44) AND D.ID IS NOT NULL)),0) /	(ROUND(((-(TIMESTAMPDIFF(MINUTE,'@FECHA_FIN',(CASE WHEN X.ALTA>'@FECHA_INI' THEN X.ALTA ELSE '@FECHA_INI' END))/60)/24)+1),0)  -SUM(E.FALTAS)-SUM(E.INCAPACIDADES)) ) ),2) )			
		END ) AS SDI_NVO				
		FROM NOMINA N 						
		JOIN nomina_por_empleado E ON(E.nomina_id=N.ID)
		JOIN empleado X ON(X.ID=E.empleado_id)
		JOIN salario S ON(S.EMPLEADO_ID=X.ID)
		LEFT JOIN baja_de_empleado B ON(B.empleado_id=X.id)
		WHERE X.ID=? AND 
		date(n.periodo_fecha_inicial)>='@FECHA_INI' and date(n.periodo_fecha_final)<='@FECHA_FIN'  AND (B.ID IS NULL OR ( X.ALTA>DATE(B.FECHA) AND X.STATUS<>'BAJA'))
		GROUP BY X.ID
		
	"""


    String sdiPorEmpleadoNuevo="""		 
	SELECT ROUND(((SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  S.periodicidad='@TIPO' THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 		
	FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR(DATE(NOW())) AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR(DATE(NOW())) THEN 0 ELSE 2 END) AND 	
	ROUND((-(TIMESTAMPDIFF(MINUTE,DATE(MAX( CASE WHEN B.FECHA<DATE(NOW()) AND B.FECHA>X.ALTA THEN B.FECHA ELSE DATE(NOW()) END )),X.ALTA)/60)/24),0) BETWEEN F.DIAS_DE AND F.DIAS_HASTA )	
	* @SALARIO),2) AS SDI_NVO
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
	
	
	String sqlPorBimestre="""
		SELECT x.ID,X.CLAVE,x.NOMBRES,x.APELLIDO_PATERNO,X.APELLIDO_MATERNO,X.ALTA ,X.STATUS,DATE(B.FECHA) AS F_BAJA
		,S.PERIODICIDAD,'@FECHA_INI' AS FECHA_INI,'@FECHA_FIN' AS FECHA_FIN   
		,(SELECT U.descripcion FROM ubicacion U WHERE U.ID=P.UBICACION_ID) AS UBICACION,S.SALARIO_DIARIO*1 AS SDB
		,S.SALARIO_DIARIO_INTEGRADO*1 AS SDI_ANTERIOR	-- PARAMETRO DE SDI ANTERIOR
		,(SELECT Z.SALARIO FROM zona_economica Z WHERE Z.CLAVE='A' ) AS SMG		
		,(25)*(SELECT Z.SALARIO FROM zona_economica Z WHERE Z.CLAVE='A' ) AS TOPE_SMG		
		,ROUND((-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24)/365,0)  AS YEARS
		,ROUND(-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24,0)+1  AS DIAS
		,(SELECT MAX(F.VAC_DIAS) FROM factor_de_integracion F 	WHERE ROUND(-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24,0) BETWEEN F.DIAS_DE AND F.DIAS_HASTA ) AS VAC_DIAS	
		,(SELECT MAX(F.VAC_PRIMA) FROM factor_de_integracion F WHERE ROUND(-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24,0) BETWEEN F.DIAS_DE AND F.DIAS_HASTA ) AS VAC_PRIMA	
		,(SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_DIAS WHEN  S.periodicidad='@TIPO' THEN F.SEM_DIAS	ELSE F.QNA_DIAS END	) 
			FROM factor_de_integracion F 	WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') THEN 0 ELSE 2 END) AND
			ROUND(-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24,0) BETWEEN F.DIAS_DE AND F.DIAS_HASTA 	) AS AGNDO_DIAS
		,(SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  S.periodicidad='@TIPO' THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 		
			FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') THEN 0 ELSE 2 END) AND 	
			ROUND((-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24),0) BETWEEN F.DIAS_DE AND F.DIAS_HASTA ) AS FACTOR	
		,((SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  S.periodicidad='@TIPO' THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 		
			FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') THEN 0 ELSE 2 END) AND 	
			ROUND((-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24),0) BETWEEN F.DIAS_DE AND F.DIAS_HASTA )	
			*	  s.salario_diario  ) AS SDI_F 																			
		,ROUND(((-(TIMESTAMPDIFF(MINUTE,'@FECHA_FIN','@FECHA_INI')/60)/24)+1),0) AS DIAS_BIM	
		,SUM(E.FALTAS) AS FALTAS,SUM(E.INCAPACIDADES) AS INCAPACIDADES
		,(ROUND(((-(TIMESTAMPDIFF(MINUTE,'@FECHA_FIN',(CASE WHEN X.ALTA>'@FECHA_INI' THEN X.ALTA ELSE '@FECHA_INI' END))/60)/24)+1),0)  -SUM(E.FALTAS)-SUM(E.INCAPACIDADES)) AS DIAS_LAB_BIM		
		,IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(19) AND D.ID IS NOT NULL)),0) AS COMPENSACION	
		,IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(22) AND D.ID IS NOT NULL)),0) AS INCENTIVO	
		,IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(24) AND D.ID IS NOT NULL)),0) AS BONO_POR_DESEMP	
		,IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(34) AND D.ID IS NOT NULL)),0) AS HRS_EXTRAS_DOBLES
		,IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(35) AND D.ID IS NOT NULL)),0) AS HRS_EXTRAS_TRIPLES
		,IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(41) AND D.ID IS NOT NULL)),0) AS COMISIONES	
		,IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(42) AND D.ID IS NOT NULL)),0) AS PRIMA_DOM	
		,IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(44) AND D.ID IS NOT NULL)),0) AS VACACIONES_P
		,IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(19,22,24,34,35,41,42,44) AND D.ID IS NOT NULL)),0) AS VARIABLE	
		,( IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(19,22,24,34,35,41,42,44) AND D.ID IS NOT NULL)),0)/
			(ROUND(((-(TIMESTAMPDIFF(MINUTE,'@FECHA_FIN',(CASE WHEN X.ALTA>'@FECHA_INI' THEN X.ALTA ELSE '@FECHA_INI' END))/60)/24)+1),0)  -SUM(E.FALTAS)-SUM(E.INCAPACIDADES)) ) AS VAR_DIA					
		,( ROUND( ( ( (SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  S.periodicidad='@TIPO' THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 		
				FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') THEN 0 ELSE 2 END) AND 	
				ROUND((-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24),0) BETWEEN F.DIAS_DE AND F.DIAS_HASTA )		*	  s.salario_diario  )	+			
			( IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(19,22,24,34,35,41,42,44) AND D.ID IS NOT NULL)),0) /	(ROUND(((-(TIMESTAMPDIFF(MINUTE,'@FECHA_FIN',(CASE WHEN X.ALTA>'@FECHA_INI' THEN X.ALTA ELSE '@FECHA_INI' END))/60)/24)+1),0)  -SUM(E.FALTAS)-SUM(E.INCAPACIDADES)) ) ),2) )  AS SDI_CALC			
		,(CASE WHEN (25)*(SELECT Z.SALARIO FROM zona_economica Z WHERE Z.CLAVE='A' )<=						
			( ROUND( ( ( (SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  S.periodicidad='@TIPO' THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 		
				FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') THEN 0 ELSE 2 END) AND 	
				ROUND((-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24),0) BETWEEN F.DIAS_DE AND F.DIAS_HASTA )		*	  s.salario_diario  )	+			
			( IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(19,22,24,34,35,41,42,44) AND D.ID IS NOT NULL)),0) /	(ROUND(((-(TIMESTAMPDIFF(MINUTE,'@FECHA_FIN',(CASE WHEN X.ALTA>'@FECHA_INI' THEN X.ALTA ELSE '@FECHA_INI' END))/60)/24)+1),0)  -SUM(E.FALTAS)-SUM(E.INCAPACIDADES)) ) ),2) )			
				THEN		 (25)*(SELECT Z.SALARIO FROM zona_economica Z WHERE Z.CLAVE='A' ) 		ELSE 
			( ROUND( ( ( (SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  S.periodicidad='@TIPO' THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 		
				FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') THEN 0 ELSE 2 END) AND 	
				ROUND((-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24),0) BETWEEN F.DIAS_DE AND F.DIAS_HASTA )		*	  s.salario_diario  )	+			
			( IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(19,22,24,34,35,41,42,44) AND D.ID IS NOT NULL)),0) /	(ROUND(((-(TIMESTAMPDIFF(MINUTE,'@FECHA_FIN',(CASE WHEN X.ALTA>'@FECHA_INI' THEN X.ALTA ELSE '@FECHA_INI' END))/60)/24)+1),0)  -SUM(E.FALTAS)-SUM(E.INCAPACIDADES)) ) ),2) )			
				END ) AS SDI_NVO				
		,CASE WHEN S.SALARIO_DIARIO_INTEGRADO <> 
			(CASE WHEN (25)*(SELECT Z.SALARIO FROM zona_economica Z WHERE Z.CLAVE='A' )<=						
				( ROUND( ( ( (SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  S.periodicidad='@TIPO' THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 		
				FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') THEN 0 ELSE 2 END) AND 	
				ROUND((-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24),0) BETWEEN F.DIAS_DE AND F.DIAS_HASTA )		*	  s.salario_diario  )	+			
			( IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(19,22,24,34,35,41,42,44) AND D.ID IS NOT NULL)),0) /	(ROUND(((-(TIMESTAMPDIFF(MINUTE,'@FECHA_FIN',(CASE WHEN X.ALTA>'@FECHA_INI' THEN X.ALTA ELSE '@FECHA_INI' END))/60)/24)+1),0)  -SUM(E.FALTAS)-SUM(E.INCAPACIDADES)) ) ),2) )			
				THEN		 (25)*(SELECT Z.SALARIO FROM zona_economica Z WHERE Z.CLAVE='A' ) 		ELSE 
			( ROUND( ( ( (SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  S.periodicidad='@TIPO' THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 		
				FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') THEN 0 ELSE 2 END) AND 	
				ROUND((-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24),0) BETWEEN F.DIAS_DE AND F.DIAS_HASTA )		*	  s.salario_diario  )	+			
			( IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(19,22,24,34,35,41,42,44) AND D.ID IS NOT NULL)),0) /	(ROUND(((-(TIMESTAMPDIFF(MINUTE,'@FECHA_FIN',(CASE WHEN X.ALTA>'@FECHA_INI' THEN X.ALTA ELSE '@FECHA_INI' END))/60)/24)+1),0)  -SUM(E.FALTAS)-SUM(E.INCAPACIDADES)) ) ),2) )			
				END ) 				
			THEN 					
			(CASE WHEN (25)*(SELECT Z.SALARIO FROM zona_economica Z WHERE Z.CLAVE='A' )<=						
				( ROUND( ( ( (SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  S.periodicidad='@TIPO' THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 		
				FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') THEN 0 ELSE 2 END) AND 	
				ROUND((-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24),0) BETWEEN F.DIAS_DE AND F.DIAS_HASTA )		*	  s.salario_diario  )	+			
			( IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(19,22,24,34,35,41,42,44) AND D.ID IS NOT NULL)),0) /	(ROUND(((-(TIMESTAMPDIFF(MINUTE,'@FECHA_FIN',(CASE WHEN X.ALTA>'@FECHA_INI' THEN X.ALTA ELSE '@FECHA_INI' END))/60)/24)+1),0)  -SUM(E.FALTAS)-SUM(E.INCAPACIDADES)) ) ),2) )			
				THEN		 (25)*(SELECT Z.SALARIO FROM zona_economica Z WHERE Z.CLAVE='A' ) 		ELSE 
			( ROUND( ( ( (SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  S.periodicidad='@TIPO' THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 		
				FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') THEN 0 ELSE 2 END) AND 	
				ROUND((-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24),0) BETWEEN F.DIAS_DE AND F.DIAS_HASTA )		*	  s.salario_diario  )	+			
			( IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(19,22,24,34,35,41,42,44) AND D.ID IS NOT NULL)),0) /	(ROUND(((-(TIMESTAMPDIFF(MINUTE,'@FECHA_FIN',(CASE WHEN X.ALTA>'@FECHA_INI' THEN X.ALTA ELSE '@FECHA_INI' END))/60)/24)+1),0)  -SUM(E.FALTAS)-SUM(E.INCAPACIDADES)) ) ),2) )			
				END )				
			ELSE 0 END	AS SDI_INF			
		FROM NOMINA N 						
		JOIN nomina_por_empleado E ON(E.nomina_id=N.ID)
		JOIN empleado X ON(X.ID=E.empleado_id)
		JOIN salario S ON(S.EMPLEADO_ID=X.ID)
		JOIN perfil_de_empleado P ON(P.EMPLEADO_ID=X.ID)
		LEFT JOIN baja_de_empleado B ON(B.empleado_id=X.id)
		WHERE S.periodicidad='@TIPO' AND date(n.periodo_fecha_inicial)>='@FECHA_INI' and date(n.periodo_fecha_final)<='@FECHA_FIN'  AND (B.ID IS NULL OR ( X.ALTA>DATE(B.FECHA) AND X.STATUS<>'BAJA') )
		GROUP BY X.ID
	"""

}

