package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import groovy.sql.Sql
import com.luxsoft.sw4.Periodo
import com.luxsoft.sw4.rh.tablas.ZonaEconomica;

@Transactional
class CalculoSdiService {
	
	def dataSource

    def calcularSdi(ModificacionSalarial m) {
		
		def ejercicio=Periodo.obtenerYear(m.fecha)
		
		def val=CalendarioDet.executeQuery("select min(d.bimestre) from CalendarioDet d where date(?) between d.inicio and d.fin",[m.fecha])
		def bimestre=val.get(0)-1
		
		if(bimestre==0){
			bimestre=6
			ejercicio=ejercicio-1
		}
		def tipo=m.empleado.salario.periodicidad
		
		def stipo=tipo=='SEMANAL'?'SEMANA':'QUINCENA'
		def res=CalendarioDet
		.executeQuery(
			"select min(d.inicio),max(d.fin) from CalendarioDet d where d.bimestre=? and d.calendario.tipo=? and d.calendario.ejercicio=?"
		,[bimestre,stipo,ejercicio])
	
		def inicio=res.get(0)[0]
		def fin=res.get(0)[1]
		//println 'Res: '+res
		//println 'Inicio: '+inicio+ 'Fin: '+fin+ ' Tipo: '+stipo+ '  Ejercicio: '+ejercicio+' bBimestre: '+bimestre
		def zona=ZonaEconomica.findByClave('A')
		
		//log.info "Calculo SDI para $m.empleado $ejercicio  bimestre $bimestre tipo $tipo ${inicio.format('dd/MM/yyyy')} a ${fin.format('dd/MM/yyyy')}"
		
		def query=sqlPorBimestre
		.replaceAll('@FECHA_INI'.toLowerCase(),inicio.format('yyyy/MM/dd'))
		.replaceAll('@FECHA_FIN'.toLowerCase(),fin.format('yyyy/MM/dd'))
		.replaceAll('@FECHA_ULT_MODIF'.toLowerCase(),m.fecha.format('yyyy/MM/dd'))
		.replaceAll('@TIPO'.toLowerCase()," s.periodicidad=\'SEMANAL\'")
		.replaceAll('@PERIODO'.toLowerCase(),tipo)
		println query
		Sql sql=new Sql(dataSource)
		sql.eachRow(query,[m.empleado.id]){ row->
			def empleado=m.empleado
			def found=CalculoSdi.findByEmpleadoAndEjercicioAndBimestreAndTipo(empleado,ejercicio,bimestre,m.tipo)
			if(!found){
				found=new CalculoSdi(
					empleado:empleado,
					ejercicio:ejercicio,
					bimestre:bimestre,
					tipo:m.tipo,
					fechaIni:inicio,
					fechaFin:fin
					
					).save flush:true
			}
			found.sdiAnterior=empleado.salario.salarioDiarioIntegrado
			found.sdbAnterior=empleado.salario.salarioDiario
			found.sdb=m.salarioNuevo
			found.years=( (m.fecha-empleado.alta)/365)
			found.dias=m.fecha-empleado.alta+1
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
			found.save failOnError:true
			log.info 'Calculo SDI Generado: '+found.id
			m.calculoSdi=found
			m.sdiNuevo=found.sdiNvo
			m.bimestre=found.bimestre
			m.save failOnError:true
		}

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
/*
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
		WHERE X.ID=? AND S.periodicidad='@PERIODO' AND date(n.periodo_fecha_inicial)>='@FECHA_INI' and date(n.periodo_fecha_final)<='@FECHA_FIN'  AND (B.ID IS NULL OR ( X.ALTA>DATE(B.FECHA) AND X.STATUS<>'BAJA') )
		GROUP BY X.ID

	"""
	*/
	
	String sqlPorBimestre=
"""
select x.id,x.clave,x.alta
		,(select max(f.vac_dias) from factor_de_integracion f 	where round(-(timestampdiff(minute,'@fecha_ult_modif',x.alta)/60)/24,0)+1 between f.dias_de and f.dias_hasta ) as vac_dias	
		,(select max(f.vac_prima) from factor_de_integracion f where round(-(timestampdiff(minute,'@fecha_ult_modif',x.alta)/60)/24,0)+1 between f.dias_de and f.dias_hasta ) as vac_prima	
		,(select max(case 	when x.id=245 then f.cob_dias+2 		
							when x.id in(274,273) then f.cob_dias when  @tipo then f.sem_dias	else f.qna_dias end	) 
			from factor_de_integracion f 	where f.tipo=(case when year(x.alta)=year('@fecha_fin') and month(x.alta)>=3 then 1 when year(x.alta)=year('@fecha_fin') then 0 else 2 end) and
			round(-(timestampdiff(minute,'@fecha_ult_modif',x.alta)/60)/24,0)+1 between f.dias_de and f.dias_hasta 	) as agndo_dias
		,(select max(case 	when x.id=245 then 1+round((((f.vac_dias*f.vac_prima)+f.cob_dias+2)/365),4) 
							when x.id in(274,273) then f.cob_factor when  @tipo then f.sem_factor else f.qna_factor end) 		
			from factor_de_integracion f where f.tipo=(case when year(x.alta)=year('@fecha_fin') and month(x.alta)>=3 then 1 when year(x.alta)=year('@fecha_fin') then 0 else 2 end) and 	
			round((-(timestampdiff(minute,'@fecha_ult_modif',x.alta)/60)/24),0)+1 between f.dias_de and f.dias_hasta ) as factor	
		,ifnull((case	when x.control_de_asistencia is false then  sum((select sum(a.faltas_manuales) from asistencia a where a.id=e.asistencia_id )) 
				when x.alta<='@fecha_ini' then sum(e.faltas) else round((-(timestampdiff(minute,'@fecha_fin',x.alta)/60)/24)+1,0)-round(sum(e.dias_trabajados),0) end),0) as faltas 
		,sum(e.incapacidades) as incapacidades
		,(case	when x.control_de_asistencia is false then  ifnull(round(sum(e.dias_trabajados),0)-sum((select sum(a.faltas_manuales) from asistencia a where a.id=e.asistencia_id )),0) 
				when x.alta<='@fecha_ini' then (round(((-(timestampdiff(minute,'@fecha_fin',(case when x.alta>'@fecha_ini' then x.alta else '@fecha_ini' end))/60)/24)+1),0)  -   sum(e.faltas)	-sum(e.incapacidades))  
				else  ifnull(round(sum(e.dias_trabajados),0)-sum((select sum(a.faltas_manuales) from asistencia a where a.id=e.asistencia_id )),0) end) as dias_lab_bim 
		from nomina n 						
		join nomina_por_empleado e on(e.nomina_id=n.id)
		join empleado x on(x.id=e.empleado_id)
		join salario s on(s.empleado_id=x.id)
		join perfil_de_empleado p on(p.empleado_id=x.id)
		left join baja_de_empleado b on(b.empleado_id=x.id)
		where x.id=? and s.periodicidad='@periodo' and date(n.periodo_fecha_inicial)>='@fecha_ini' and date(n.periodo_fecha_final)<='@fecha_fin'  and (b.id is null or ( x.alta>date(b.fecha) and x.status<>'baja') )
		group by x.id

"""
}
