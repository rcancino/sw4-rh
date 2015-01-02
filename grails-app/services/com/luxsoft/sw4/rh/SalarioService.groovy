package com.luxsoft.sw4.rh





import com.luxsoft.sw4.rh.tablas.ZonaEconomica;

import grails.transaction.Transactional
import grails.transaction.NotTransactional
import grails.events.Listener
import groovy.sql.Sql

@Transactional
class SalarioService {

	def dataSource

	//@Listener(namespace='gorm')
    def afterInsert(ModificacionSalarial modificacion) {
    	
    	def salario=modificacion.empleado.salario
    	salario.salarioDiario=modificacion.salarioNuevo
		salario.salarioDiarioIntegrado=modificacion.sdiNuevo
    	log.info "${modificacion} detectada Salario ${salario} actualizado"

    }

   @Listener(namespace='gorm')
    def beforeDelete(ModificacionSalarial modificacion){
		def calculoSdi=modificacion.calculoSdi
		if(calculoSdi){
			if(calculoSdi.status=='APLICADO')
				throw new RuntimeException("Modificacion aplicada no se puede eliminar")
			else{
				calculoSdi.delete()
			}
		}
    	//def salario=modificacion.empleado.salario
    	//salario.salarioDiario=modificacion.salarioAnterior
		//salario.salarioDiarioIntegrado=modificacion.sdiAnterior
    	//log.info "${modificacion} detectada Salario ${salario} actualizado"
    }
	
	@NotTransactional
	def calcularSalarioDiarioOld(int ejercicio,int bimestre){
		
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
			.replaceAll('@TIPO', it=='SEMANA'? 'S.periodicidad=\'SEMANAL\'' : 'S.periodicidad<>\'QUINCENAL\'')
			.replaceAll('@PERIODO',it+'L')
									
			//println query
		Sql sql=new Sql(dataSource)
		rows.addAll(sql.rows(query))
		}
		rows=rows.sort{a,b ->
				a.ubicacion<=>b.ubicacion?:a.APELLIDO_PATERNO<=>b.APELLIDO_PATERNO
			}
		//def rows= sql.rows(query)
		return rows
	}
	
	//@NotTransactional
	def aplicarCalculoDeSalarioDiario(int ejercicio,int bimestre){
		def rows=calcularSalarioDiario(ejercicio,bimestre)
		def res=[]
		
		rows.each{ row ->
			def empleado=Empleado.findByClave(row.CLAVE)
			if(empleado){
				def old=empleado.salario.salarioDiarioIntegrado
				
				//empleado.salario.salarioDiarioIntegrado=row.SDI_NVO
				//empleado.save(flush:true)
				//Los listeners de modificacion salarial se encargan de actualizar el salario del empleado
				def ms=new ModificacionSalarial(empleado:empleado,tipo:'CALCULO_SDI')
				
				ms.salarioAnterior=empleado.salario.salarioDiario // Esto no cambia
				ms.salarioNuevo=empleado.salario.salarioDiario // Esto no cambia
				
				ms.sdiAnterior=old
				ms.sdiNuevo=empleado.salario.salarioDiarioIntegrado
				ms.fecha=new Date()
				ms.save failOnError:true
				
				res.add(empleado)
				log.info "SDI actuaizado para ${empleado} SDI anterior: ${old} SDI nvo: ${empleado.salario.salarioDiarioIntegrado}"
			}
		}
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
	
	@NotTransactional
	def calcularSalarioDiario(int ejercicio,int bimestre){
		println "Generando calculo SDI bimestre:$bimestre ejercicio:$ejercicio"
		def rows=[]
		["QUINCENA","SEMANA"].each{
			
			log.info "SDI para $ejercicio  bimestre $bimestre tipo $it"
			
			def res=CalendarioDet
			.executeQuery("select min(d.inicio),max(d.fin) from CalendarioDet d where d.bimestre=? and d.calendario.tipo=? and d.calendario.ejercicio=?"
			,[bimestre,it,ejercicio])
		
			def inicio=res.get(0)[0]
			def fin=res.get(0)[1]
			log.info "Periodo: $inicio al $fin"
			
			def zona=ZonaEconomica.findByClave('A')
		
			def query=sqlPorBimestre
				.replaceAll('@FECHA_INI',inicio.format('yyyy/MM/dd'))
				.replaceAll('@FECHA_FIN',fin.format('yyyy/MM/dd'))
				.replaceAll('@FECHA_ULT_MODIF',fin.format('yyyy/MM/dd'))
				.replaceAll('@TIPO', it=='SEMANA'? 'S.periodicidad=\'SEMANAL\'' : 'S.periodicidad<>\'QUINCENAL\'')
				.replaceAll('@PERIODO',it+'L')
									
			println query
				Sql sql=new Sql(dataSource)
				sql.eachRow(query){ row->
					
					def empleado=Empleado.findById(row.id)
					if(empleado){
						//println 'SDI para: '+empleado
						def found=CalculoSdi.findByEmpleadoAndEjercicioAndBimestreAndTipo(empleado,ejercicio,bimestre,'CALCULO_SDI')
						if(!found){
							found=new CalculoSdi(
								empleado:empleado,
								ejercicio:ejercicio,
								bimestre:bimestre,
								tipo:'CALCULO_SDI',
								fechaIni:inicio,
								fechaFin:fin
								
								).save flush:true
						}
						
							found.sdiAnterior=empleado.salario.salarioDiarioIntegrado
							found.sdbAnterior=empleado.salario.salarioDiario
							found.sdb=empleado.salario.salarioDiario
							found.years=( (fin-empleado.alta)/365)
							found.dias=fin-empleado.alta+1
							found.vacDias=row.VAC_DIAS
							found.vacPrima=row.VAC_PRIMA
							found.agndoDias=row.AGNDO_DIAS
							found.factor=row.FACTOR
							found.sdiF=found.sdb*found.factor
							found.diasLabBim=row.DIAS_LAB_BIM
							
							
							
							
							
							found.compensacion=0.0
							found.incentivo=0.0
							found.bonoPorDesemp=0.0
							found.hrsExtrasDobles=0.0
							found.hrsExtrasTriples=0.0
							found.comisiones=0.0
							found.primaDom=0.0
							found.vacacionesP=0.0
							actualizarVariables(found)
							registrarTiempoExtraDoble(found)
							found.with{
								variable=compensacion+incentivo+bonoPorDesemp+hrsExtrasDobles+hrsExtrasTriples+comisiones+primaDom+vacacionesP
							}
							
							
							
							found.varDia=found.variable/found.diasLabBim
							
							def sdiNvo=found.sdiF+found.varDia
							found.sdiCalc=sdiNvo
							
							if(found.sdb==0.0){
								sdiNvo=found.varDia*found.factor
							}
							
							def topoSalarial=25*zona.salario
							found.topeSmg=topoSalarial
							
							if(sdiNvo>topoSalarial)
								found.sdiNvo=topoSalarial
							else{
								found.sdiNvo=sdiNvo
							}
							
							found.smg=zona.salario
							if(found.sdiAnterior==found.sdiNvo){
								found.sdiInf=0.0
							}else{
								found.sdiInf=found.sdiNvo
							}
							if(empleado.alta>inicio){
								found.diasBim=fin-empleado.alta+1
							}else{
								found.diasBim=fin-inicio+1
							}
							found.incapacidades=row.INCAPACIDADES
							found.faltas=row.FALTAS
							
					
						
					}
					
				}
		}
		
		return rows
	}
	
	private actualizarVariables(CalculoSdi sdi){
		
		def partidas=NominaPorEmpleadoDet
		.findAll("from NominaPorEmpleadoDet d where d.parent.empleado=? and d.concepto.id in(19,22,24,41,42,44) and d.parent.nomina.ejercicio=? and d.parent.nomina.calendarioDet.bimestre=?"
				 ,[sdi.empleado,sdi.ejercicio,sdi.bimestre])
	
		sdi.compensacion=0.0
		sdi.incentivo=0.0
		sdi.bonoPorDesemp=0.0
		
		sdi.comisiones=0.0
		sdi.primaDom=0.0
		sdi.vacacionesP=0.0
	
		partidas.each{it->
			switch(it.concepto.id){
				case 19:
					sdi.compensacion+=it.importeGravado+it.importeExcento
					break
				case 22:
					sdi.incentivo+=it.importeGravado+it.importeExcento
					break
				case 24:
					sdi.bonoPorDesemp+=it.importeGravado+it.importeExcento
					break
				case 41:
					sdi.comisiones+=it.importeGravado+it.importeExcento
					break
				case 42:
					sdi.primaDom+=it.importeGravado+it.importeExcento
					break
				case 44:
					sdi.vacacionesP+=it.importeGravado+it.importeExcento
					break
			}
		}
		
	}
	
	private registrarTiempoExtraDoble(CalculoSdi sdi){
		def partidas=TiempoExtraDet
		.executeQuery("from TiempoExtraDet d where d.tiempoExtra.empleado=? and d.tiempoExtra.asistencia.calendarioDet.bimestre=? and d.tiempoExtra.asistencia.calendarioDet.calendario.ejercicio=?"
					 ,[sdi.empleado,sdi.bimestre,sdi.ejercicio])
		
		def triples=partidas.sum 0.0 ,{
			it.tiempoExtraImss.integraTriples
		}
		def dobles=partidas.sum 0.0,{
			if(it.tiempoExtraImss){
			  it.tiempoExtraImss.integra
			}
		} 
		sdi.hrsExtrasDobles=dobles
		sdi.hrsExtrasTriples=triples
	
	}
	
	
	def aplicarCalculo(CalculoSdi sdi){
		log.info "${sdi.empleado}  SDI (Anterior) ${sdi.sdiAnterior} SDI (nuevo)${sdi.sdiNvo}"
		def salario=sdi.empleado.salario
		sdi.status='APLICADO'
		salario.salarioDiarioIntegrado=sdi.sdiNvo
		salario.save()
		
	}
	
	String sdiPorEmpleado="""
SELECT (CASE WHEN (25)*(SELECT Z.SALARIO FROM zona_economica Z WHERE Z.CLAVE='A' )<=						
	( ROUND( ( ( (SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  S.periodicidad='SEMANAL' THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 		
		FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') THEN 0 ELSE 2 END) AND 	
		ROUND((-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24),0) BETWEEN F.DIAS_DE AND F.DIAS_HASTA )		*	 @SALARIO )	+			
	( IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(19,22,24,34,35,41,42,44) AND D.ID IS NOT NULL)),0) /	(ROUND(((-(TIMESTAMPDIFF(MINUTE,'@FECHA_FIN',(CASE WHEN X.ALTA>'@FECHA_INI' THEN X.ALTA ELSE '@FECHA_INI' END))/60)/24)+1),0)  -SUM(E.FALTAS)-SUM(E.INCAPACIDADES)) ) ),2) )			
		THEN		 (25)*(SELECT Z.SALARIO FROM zona_economica Z WHERE Z.CLAVE='A' ) 		ELSE 
	( ROUND( ( ( (SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  S.periodicidad='SEMANAL' THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 		
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
	
	
	String sqlPorBimestre_old="""
		SELECT x.ID,X.CLAVE,x.NOMBRES,x.APELLIDO_PATERNO,X.APELLIDO_MATERNO,X.ALTA ,X.STATUS,DATE(B.FECHA) AS F_BAJA
		,S.PERIODICIDAD,'@FECHA_INI' AS FECHA_INI,'@FECHA_FIN' AS FECHA_FIN   
		,(SELECT U.descripcion FROM ubicacion U WHERE U.ID=P.UBICACION_ID) AS UBICACION,S.SALARIO_DIARIO*1 AS SDB
		,S.SALARIO_DIARIO_INTEGRADO*1 AS SDI_ANTERIOR	-- PARAMETRO DE SDI ANTERIOR
		,(SELECT Z.SALARIO FROM zona_economica Z WHERE Z.CLAVE='A' ) AS SMG		
		,(25)*(SELECT Z.SALARIO FROM zona_economica Z WHERE Z.CLAVE='A' ) AS TOPE_SMG		
//		,ROUND((-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24)/365,0)  AS YEARS
//		,ROUND(-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24,0)+1  AS DIAS
		,(SELECT MAX(F.VAC_DIAS) FROM factor_de_integracion F 	WHERE ROUND(-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24,0)+1 BETWEEN F.DIAS_DE AND F.DIAS_HASTA ) AS VAC_DIAS	
		,(SELECT MAX(F.VAC_PRIMA) FROM factor_de_integracion F WHERE ROUND(-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24,0)+1 BETWEEN F.DIAS_DE AND F.DIAS_HASTA ) AS VAC_PRIMA	
		,(SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_DIAS WHEN  @TIPO THEN F.SEM_DIAS	ELSE F.QNA_DIAS END	) 
			FROM factor_de_integracion F 	WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') THEN 0 ELSE 2 END) AND
			ROUND(-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24,0)+1 BETWEEN F.DIAS_DE AND F.DIAS_HASTA 	) AS AGNDO_DIAS
		,(SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  @TIPO THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 		
			FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') THEN 0 ELSE 2 END) AND 	
			ROUND((-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24),0)+1 BETWEEN F.DIAS_DE AND F.DIAS_HASTA ) AS FACTOR	
		,((SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  @TIPO THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 		
			FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') THEN 0 ELSE 2 END) AND 	
			ROUND((-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24),0)+1 BETWEEN F.DIAS_DE AND F.DIAS_HASTA )	
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
		,( ROUND( ( ( (SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  @TIPO THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 		
				FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') THEN 0 ELSE 2 END) AND 	
				ROUND((-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24),0)+1 BETWEEN F.DIAS_DE AND F.DIAS_HASTA )		*	  s.salario_diario  )	+			
			( IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(19,22,24,34,35,41,42,44) AND D.ID IS NOT NULL)),0) /	(ROUND(((-(TIMESTAMPDIFF(MINUTE,'@FECHA_FIN',(CASE WHEN X.ALTA>'@FECHA_INI' THEN X.ALTA ELSE '@FECHA_INI' END))/60)/24)+1),0)  -SUM(E.FALTAS)-SUM(E.INCAPACIDADES)) ) ),2) )  AS SDI_CALC			
		,(CASE WHEN (25)*(SELECT Z.SALARIO FROM zona_economica Z WHERE Z.CLAVE='A' )<=						
			( ROUND( ( ( (SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  @TIPO THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 		
				FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') THEN 0 ELSE 2 END) AND 	
				ROUND((-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24),0)+1 BETWEEN F.DIAS_DE AND F.DIAS_HASTA )		*	  s.salario_diario  )	+			
			( IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(19,22,24,34,35,41,42,44) AND D.ID IS NOT NULL)),0) /	(ROUND(((-(TIMESTAMPDIFF(MINUTE,'@FECHA_FIN',(CASE WHEN X.ALTA>'@FECHA_INI' THEN X.ALTA ELSE '@FECHA_INI' END))/60)/24)+1),0)  -SUM(E.FALTAS)-SUM(E.INCAPACIDADES)) ) ),2) )			
				THEN		 (25)*(SELECT Z.SALARIO FROM zona_economica Z WHERE Z.CLAVE='A' ) 		ELSE 
			( ROUND( ( ( (SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  @TIPO THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 		
				FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') THEN 0 ELSE 2 END) AND 	
				ROUND((-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24),0)+1 BETWEEN F.DIAS_DE AND F.DIAS_HASTA )		*	  s.salario_diario  )	+			
			( IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(19,22,24,34,35,41,42,44) AND D.ID IS NOT NULL)),0) /	(ROUND(((-(TIMESTAMPDIFF(MINUTE,'@FECHA_FIN',(CASE WHEN X.ALTA>'@FECHA_INI' THEN X.ALTA ELSE '@FECHA_INI' END))/60)/24)+1),0)  -SUM(E.FALTAS)-SUM(E.INCAPACIDADES)) ) ),2) )			
				END ) AS SDI_NVO				
		,CASE WHEN S.SALARIO_DIARIO_INTEGRADO <> 
			(CASE WHEN (25)*(SELECT Z.SALARIO FROM zona_economica Z WHERE Z.CLAVE='A' )<=						
				( ROUND( ( ( (SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  @TIPO THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 		
				FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') THEN 0 ELSE 2 END) AND 	
				ROUND((-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24),0)+1 BETWEEN F.DIAS_DE AND F.DIAS_HASTA )		*	  s.salario_diario  )	+			
			( IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(19,22,24,34,35,41,42,44) AND D.ID IS NOT NULL)),0) /	(ROUND(((-(TIMESTAMPDIFF(MINUTE,'@FECHA_FIN',(CASE WHEN X.ALTA>'@FECHA_INI' THEN X.ALTA ELSE '@FECHA_INI' END))/60)/24)+1),0)  -SUM(E.FALTAS)-SUM(E.INCAPACIDADES)) ) ),2) )			
				THEN		 (25)*(SELECT Z.SALARIO FROM zona_economica Z WHERE Z.CLAVE='A' ) 		ELSE 
			( ROUND( ( ( (SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  @TIPO THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 		
				FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') THEN 0 ELSE 2 END) AND 	
				ROUND((-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24),0)+1 BETWEEN F.DIAS_DE AND F.DIAS_HASTA )		*	  s.salario_diario  )	+			
			( IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(19,22,24,34,35,41,42,44) AND D.ID IS NOT NULL)),0) /	(ROUND(((-(TIMESTAMPDIFF(MINUTE,'@FECHA_FIN',(CASE WHEN X.ALTA>'@FECHA_INI' THEN X.ALTA ELSE '@FECHA_INI' END))/60)/24)+1),0)  -SUM(E.FALTAS)-SUM(E.INCAPACIDADES)) ) ),2) )			
				END ) 				
			THEN 					
			(CASE WHEN (25)*(SELECT Z.SALARIO FROM zona_economica Z WHERE Z.CLAVE='A' )<=						
				( ROUND( ( ( (SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  @TIPO THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 		
				FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') THEN 0 ELSE 2 END) AND 	
				ROUND((-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24),0)+1 BETWEEN F.DIAS_DE AND F.DIAS_HASTA )		*	  s.salario_diario  )	+			
			( IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(19,22,24,34,35,41,42,44) AND D.ID IS NOT NULL)),0) /	(ROUND(((-(TIMESTAMPDIFF(MINUTE,'@FECHA_FIN',(CASE WHEN X.ALTA>'@FECHA_INI' THEN X.ALTA ELSE '@FECHA_INI' END))/60)/24)+1),0)  -SUM(E.FALTAS)-SUM(E.INCAPACIDADES)) ) ),2) )			
				THEN		 (25)*(SELECT Z.SALARIO FROM zona_economica Z WHERE Z.CLAVE='A' ) 		ELSE 
			( ROUND( ( ( (SELECT MAX(CASE WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  @TIPO THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 		
				FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') THEN 0 ELSE 2 END) AND 	
				ROUND((-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24),0)+1 BETWEEN F.DIAS_DE AND F.DIAS_HASTA )		*	  s.salario_diario  )	+			
			( IFNULL(SUM((SELECT SUM(ifnull(D.importe_excento,0)+ifnull(D.importe_gravado,0)) AS VARIABLE FROM nomina_por_empleado_det D 	WHERE E.ID=D.PARENT_ID AND  D.CONCEPTO_ID IN(19,22,24,34,35,41,42,44) AND D.ID IS NOT NULL)),0) /	(ROUND(((-(TIMESTAMPDIFF(MINUTE,'@FECHA_FIN',(CASE WHEN X.ALTA>'@FECHA_INI' THEN X.ALTA ELSE '@FECHA_INI' END))/60)/24)+1),0)  -SUM(E.FALTAS)-SUM(E.INCAPACIDADES)) ) ),2) )			
				END )				
			ELSE 0 END	AS SDI_INF			
		FROM NOMINA N 						
		JOIN nomina_por_empleado E ON(E.nomina_id=N.ID)
		JOIN empleado X ON(X.ID=E.empleado_id)
		JOIN salario S ON(S.EMPLEADO_ID=X.ID)
		JOIN perfil_de_empleado P ON(P.EMPLEADO_ID=X.ID)
		LEFT JOIN baja_de_empleado B ON(B.empleado_id=X.id)
		WHERE S.periodicidad='@PERIODO' AND date(n.periodo_fecha_inicial)>='@FECHA_INI' and date(n.periodo_fecha_final)<='@FECHA_FIN'  AND (B.ID IS NULL OR ( X.ALTA>DATE(B.FECHA) AND X.STATUS<>'BAJA') )
		GROUP BY X.ID
	"""
	
	String sqlPorBimestre="""
		SELECT x.ID,X.CLAVE,X.ALTA
		,(SELECT MAX(F.VAC_DIAS) FROM factor_de_integracion F 	WHERE ROUND(-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24,0)+1 BETWEEN F.DIAS_DE AND F.DIAS_HASTA ) AS VAC_DIAS	
		,(SELECT MAX(F.VAC_PRIMA) FROM factor_de_integracion F WHERE ROUND(-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24,0)+1 BETWEEN F.DIAS_DE AND F.DIAS_HASTA ) AS VAC_PRIMA	
		,(SELECT MAX(CASE 	WHEN X.ID=245 THEN F.COB_DIAS+2 		
							WHEN X.ID IN(274,273) THEN F.COB_DIAS WHEN  @TIPO THEN F.SEM_DIAS	ELSE F.QNA_DIAS END	) 
			FROM factor_de_integracion F 	WHERE F.TIPO=(CASE WHEN MONTH('@FECHA_FIN')=12 THEN 2  WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') THEN 0 ELSE 2 END) AND
			ROUND(-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24,0)+1 BETWEEN F.DIAS_DE AND F.DIAS_HASTA 	) AS AGNDO_DIAS
		,(SELECT MAX(CASE 	WHEN X.ID=245 THEN 1+ROUND((((F.VAC_DIAS*F.VAC_PRIMA)+F.COB_DIAS+2)/365),4) 
							WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  @TIPO THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 		
			FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN MONTH('@FECHA_FIN')=12 THEN 2  WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') THEN 0 ELSE 2 END) AND 	
			ROUND((-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24),0)+1 BETWEEN F.DIAS_DE AND F.DIAS_HASTA ) AS FACTOR	
		,IFNULL((CASE	WHEN X.CONTROL_DE_ASISTENCIA IS FALSE THEN  SUM((SELECT SUM(A.FALTAS_MANUALES) FROM asistencia A WHERE A.ID=E.ASISTENCIA_ID )) 
				WHEN X.ALTA<='@FECHA_INI' THEN SUM(E.FALTAS) ELSE ROUND((-(TIMESTAMPDIFF(MINUTE,'@FECHA_FIN',X.ALTA)/60)/24)+1,0)-ROUND(SUM(E.DIAS_TRABAJADOS),0) END),0) AS FALTAS 
		,SUM(E.INCAPACIDADES) AS INCAPACIDADES
		,(CASE	WHEN X.CONTROL_DE_ASISTENCIA IS FALSE THEN  IFNULL(ROUND(SUM(E.DIAS_TRABAJADOS),0)-SUM((SELECT SUM(A.FALTAS_MANUALES) FROM asistencia A WHERE A.ID=E.ASISTENCIA_ID )),0) 
				WHEN X.ALTA<='@FECHA_INI' THEN (ROUND(((-(TIMESTAMPDIFF(MINUTE,'@FECHA_FIN',(CASE WHEN X.ALTA>'@FECHA_INI' THEN X.ALTA ELSE '@FECHA_INI' END))/60)/24)+1),0)  -   SUM(E.FALTAS)	-SUM(E.INCAPACIDADES))  
				ELSE  IFNULL(ROUND(SUM(E.DIAS_TRABAJADOS),0)-SUM((SELECT SUM(A.FALTAS_MANUALES) FROM asistencia A WHERE A.ID=E.ASISTENCIA_ID )),0) END) AS DIAS_LAB_BIM 
		FROM NOMINA N 						
		JOIN nomina_por_empleado E ON(E.nomina_id=N.ID)
		JOIN empleado X ON(X.ID=E.empleado_id)
		JOIN salario S ON(S.EMPLEADO_ID=X.ID)
		JOIN perfil_de_empleado P ON(P.EMPLEADO_ID=X.ID)
		LEFT JOIN baja_de_empleado B ON(B.empleado_id=X.id)
		WHERE S.periodicidad='@PERIODO' AND date(n.periodo_fecha_inicial)>='@FECHA_INI' and date(n.periodo_fecha_final)<='@FECHA_FIN'  AND (B.ID IS NULL OR ( X.ALTA>DATE(B.FECHA) AND X.STATUS<>'BAJA') )
		GROUP BY X.ID

	"""
}

