package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import com.luxsoft.sw4.*

@Transactional
class InfonavitService {
	
	

    def calcularCuota(Infonavit infonavit,Integer ejercicio,Integer bimestre) {
		def bimestreAnterior=bimestre-1
		def ejercicioAnterior=ejercicio
		if(bimestreAnterior==6)
			ejercicioAnterior=ejrecicio-1
		log.info "Calculando la cuota INFONAVIT para $infonavit.empleado partiendo del bimestre: $bimestreAnterior $ejercicio"
		def periodo=Bimestre.getBimestre(ejercicio,bimestre)
		def periodoAnterior=Bimestre.getBimestre(ejercicioAnterior,bimestreAnterior)
		def diasDelBimestre=periodoAnterior.fechaFinal-periodoAnterior.fechaInicial
		log.info "Periodo base $periodoAnterior  Dias del periodo: $diasDelBimestre"
		def det=InfonavitDet.find{infonavit==infonavit && ejercicio==ejercicio && bimestre==bimestre}
		if(det==null){
			log.info 'Generando registro de InfonavitDet '
			det=new InfonavitDet(
				ejercicio:ejercicio,
				bimestre:bimestre,
				diasDelBimestre:diasDelBimestre,
				fechaInicial:periodoAnterior.fechaInicial,
				fechaFinal:periodoAnterior.fechaFinal,
				seguroDeVivienda:15.00,
				salarioDiarioIntegrado:infonavit.empleado.salario.salarioDiarioIntegrado,
				salarioMinimoGeneral:67.29
				)
			infonavit.addToPartidas(det)
		}
		def faltas=AsistenciaDet
			.executeQuery("select count(*) from AsistenciaDet d "+
				"where d.asistencia.empleado=? "+
				" and date(d.fecha) between ? and ? and d.tipo=?",
			[infonavit.empleado
			,periodoAnterior.fechaInicial
			,periodoAnterior.fechaFinal
			,'FALTA'])
		det.faltas=faltas.get(0)
		def incapacidad=AsistenciaDet
			.executeQuery("select count(*) from AsistenciaDet d "+
				"where d.asistencia.empleado=? "+
				" and date(d.fecha) between ? and ? and d.tipo=?",
				[infonavit.empleado
					,periodoAnterior.fechaInicial
					,periodoAnterior.fechaFinal
					,'INCAPACIDAD'])
		
		//Evaluando tipo
		switch (infonavit.tipo){
			case 'VSM':
				calcularCuotaVSM(infonavit,det)
				break
			case 'CUOTA_FIJA':
			case 'PORCENTAJE':
			default:
				break
		}
		return det
		
		//Calcular faltas e incapacidades
    }
	
	def calcularCuotaVSM(Infonavit inf,InfonavitDet det){
		def cuotaMensual=inf.cuotaFija*det.salarioMinimoGeneral
		log.info 'Cuota mensual: '+cuotaMensual
		det.importeBimestral=cuotaMensual*2
		log.debug 'Cuota Bimestreal: '+det.importeBimestral
		det.cuotaDiaria=MonedaUtils.round(det.importeBimestral/det.diasDelBimestre)
		inf.cuotaDiaria=det.cuotaDiaria
		det.cuotaBimestral=det.cuotaDiaria*(det.diasDelBimestre-det.faltas-det.incapacidades)
		det.cuotaBimestral+=det.seguroDeVivienda
	}
}
