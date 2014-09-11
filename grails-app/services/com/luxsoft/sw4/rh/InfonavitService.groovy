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
				fechaFinal:periodoAnterior.fechaFinal)
			infonavit.addToPartidas(det)
		}
		det.cuota=infonavit.cuotaFija
		det.seguroDeVivienda=15.00
		det.salarioDiarioIntegrado=infonavit.empleado.salario.salarioDiarioIntegrado
		det.salarioMinimoGeneral=67.29
		
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
				calcularDescuentoVSM(infonavit,det)
				break
			case 'CUOTA_FIJA':
				calcularDescuentoCuotaFija(det)
				break
			case 'PORCENTAJE':
				calcularDescuentoPorcentaje(det)
			default:
				break
		}
		return det
		
		//Calcular faltas e incapacidades
    }
	
	def calcularDescuentoVSM(Infonavit inf,InfonavitDet det){
		def cuotaMensual=inf.cuotaFija*det.salarioMinimoGeneral
		det.importeBimestral=cuotaMensual*2
		det.cuotaDiaria=MonedaUtils.round(det.importeBimestral/det.diasDelBimestre)
		inf.cuotaDiaria=det.cuotaDiaria
		det.cuotaBimestral=det.cuotaDiaria*(det.diasDelBimestre-det.faltas-det.incapacidades)
		det.cuotaBimestral+=det.seguroDeVivienda
		log.debug "(Descuento VSM) Cuota diaria:$det.cuotaDiaria Importe bimestral:$det.importeBimestral Cuota bimestral:$det.cuotaBimestral"
	}
	
	def calcularDescuentoPorcentaje(InfonavitDet det){
		def sdi=det.salarioDiarioIntegrado
		det.cuotaDiaria=MonedaUtils.round(sdi*(det.infonavit.cuotaFija/100))
		det.infonavit.cuotaDiaria=det.cuotaDiaria
		det.importeBimestral=det.cuotaDiaria*det.DiasDelBimestre
		det.cuotaBimestral=det.cuotaDiaria*(det.diasDelBimestre-det.faltas-det.incapacidades)
		det.cuotaBimestral+=det.seguroDeVivienda
		log.debug "(Descuento en Porentaje) Cuota diaria:$det.cuotaDiaria Importe bimestral:$det.importeBimestral Cuota bimestral:$det.cuotaBimestral"
		
	}
	
	def calcularDescuentoCuotaFija(InfonavitDet det){
		def cuotaMensual=inf.cuotaFija*2
		det.cuotaDiaria=MonedaUtils.round(cuotaMensual/det.diasDelBimestre)
		det.infonavit.cuotaDiaria=det.cuotaDiaria
		det.importeBimestral=det.cuotaDiaria*det.diasDelBimestre
		det.cuotaBimestral=det.cuotaDiaria*(det.diasDelBimestre-det.faltas-det.incapacidades)
		det.cuotaBimestral+=det.seguroDeVivienda
		log.info "(Descuento en Cuota Fija) Cuota diaria:$det.cuotaDiaria Importe bimestral:$det.importeBimestral Cuota bimestral:$det.cuotaBimestral"
	}
}
