package com.luxsoft.sw4.rh

import grails.transaction.Transactional

@Transactional
class CalculoSdiService {

    def calcularSdi(ModificacionSalarial m) {

    	log.info "Calculando SDI para empledo: $m.empleado"
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
    	
    		def query=sqlPorBimestre_old
    			.replaceAll('@FECHA_INI',inicio.format('yyyy/MM/dd'))
    			.replaceAll('@FECHA_FIN',fin.format('yyyy/MM/dd'))
    			.replaceAll('@FECHA_ULT_MODIF',fin.format('yyyy/MM/dd'))
    			.replaceAll('@TIPO', it=='SEMANA'? 'S.periodicidad=\'SEMANAL\'' : 'S.periodicidad<>\'QUINCENAL\'')
    			.replaceAll('@PERIODO',it+'L')
    								
    		//println query
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
    							found.diasBim=fin-empleado.alta
    						}else{
    							found.diasBim=fin-inicio
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

    String sqlPorBimestre="""
		SELECT x.ID,X.CLAVE,X.ALTA
		,(SELECT MAX(F.VAC_DIAS) FROM factor_de_integracion F 	WHERE ROUND(-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24,0)+1 BETWEEN F.DIAS_DE AND F.DIAS_HASTA ) AS VAC_DIAS	
		,(SELECT MAX(F.VAC_PRIMA) FROM factor_de_integracion F WHERE ROUND(-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24,0)+1 BETWEEN F.DIAS_DE AND F.DIAS_HASTA ) AS VAC_PRIMA	
		,(SELECT MAX(CASE 	WHEN X.ID=245 THEN F.COB_DIAS+2 		
							WHEN X.ID IN(274,273) THEN F.COB_DIAS WHEN  @TIPO THEN F.SEM_DIAS	ELSE F.QNA_DIAS END	) 
			FROM factor_de_integracion F 	WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') THEN 0 ELSE 2 END) AND
			ROUND(-(TIMESTAMPDIFF(MINUTE,'@FECHA_ULT_MODIF',X.ALTA)/60)/24,0)+1 BETWEEN F.DIAS_DE AND F.DIAS_HASTA 	) AS AGNDO_DIAS
		,(SELECT MAX(CASE 	WHEN X.ID=245 THEN 1+ROUND((((F.VAC_DIAS*F.VAC_PRIMA)+F.COB_DIAS+2)/365),4) 
							WHEN X.ID IN(274,273) THEN F.COB_FACTOR WHEN  @TIPO THEN F.SEM_FACTOR ELSE F.QNA_FACTOR END) 		
			FROM factor_de_integracion F WHERE F.TIPO=(CASE WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') AND MONTH(X.ALTA)>=3 THEN 1 WHEN YEAR(X.ALTA)=YEAR('@FECHA_FIN') THEN 0 ELSE 2 END) AND 	
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
