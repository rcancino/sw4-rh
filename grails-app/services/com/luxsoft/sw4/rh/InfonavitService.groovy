package com.luxsoft.sw4.rh



import grails.transaction.Transactional

import com.luxsoft.sw4.*
import com.luxsoft.sw4.rh.tablas.ZonaEconomica;

@Transactional
class InfonavitService {
	
	def altaDeCuota(Infonavit infonavit,Integer ejercicio,Integer bimestre) {
		
		log.info "Calculando la cuota INFONAVIT para nuevo credito para $infonavit.empleado partiendo del bimestre: $bimestre $ejercicio"
		def periodo=Bimestre.getBimestre(ejercicio,bimestre)
		def diasDelBimestre=periodo.fechaFinal-periodo.fechaInicial+1
		log.info "Periodo base $periodo  Dias del periodo: $diasDelBimestre"
		
	//	def salarioMinimo=ZonaEconomica.valores.find(){it.clave='A'}.salario
	def zona=ZonaEconomica.findByClave('A')

		log.info 'Generando registro de InfonavitDet '
		def	det=new InfonavitDet(
				ejercicio:ejercicio,
				bimestre:bimestre,
				diasDelBimestre:diasDelBimestre,
				fechaInicial:periodo.fechaInicial,
				fechaFinal:periodo.fechaFinal
				,suspension:infonavit.suspension)
			
		
		det.cuota=infonavit.cuotaFija
		det.seguroDeVivienda=15.00
		det.salarioDiarioIntegrado=infonavit.empleado.salario.salarioDiarioIntegrado
		//det.salarioMinimoGeneral=67.29
		//det.salarioMinimoGeneral=salarioMinimo  //********cambio
		det.salarioMinimoGeneral=zona.salario
		det.faltas=0
		det.incapacidades=0
		det.saldo=infonavit.ultimaDiferencia
		
		infonavit.addToPartidas(det)
		//det.validate()
		//println 'Errores: '+det.errors
		infonavit.bimestreActual=bimestre
		infonavit.salarioMinimoGeneral=det.salarioMinimoGeneral
		infonavit.salarioDiarioIntegrado=det.salarioDiarioIntegrado
		infonavit.diasDelBimestre=det.diasDelBimestre
		
		infonavit.seguroDeVivienda=det.seguroDeVivienda
		//Evaluando tipo
		switch (infonavit.tipo){
			case 'VSM':
				calcularDescuentoVSM(det)
				break
			case 'CUOTA_FIJA':
				calcularDescuentoCuotaFija(det)
				break
			case 'PORCENTAJE':
				calcularDescuentoPorcentaje(det)
			default:
				break
		}
		infonavit.importeBimestral=det.importeBimestral
		infonavit=infonavit.save failOnError:true
		return infonavit
		
		//Calcular faltas e incapacidades
	}

    def calcularCuota(Infonavit infonavit,Integer ejercicio,Integer bimestre) {
		
		log.info "Calculando la cuota INFONAVIT para $infonavit.empleado partiendo del bimestre: $bimestre $ejercicio"
		def periodo=Bimestre.getBimestre(ejercicio,bimestre)
		//def periodoAnterior=Bimestre.getBimestre(ejercicioAnterior,bimestreAnterior)
		def diasDelBimestre=periodo.fechaFinal-periodo.fechaInicial+1
		log.info "Periodo base $periodo  Dias del periodo: $diasDelBimestre"
		def det=InfonavitDet.find{infonavit==infonavit && ejercicio==ejercicio && bimestre==bimestre}

		//def salarioMinimo=ZonaEconomica.valores.find(){it.clave='A'}.salario
		def zona=ZonaEconomica.findByClave('A')

		if(det==null){
			log.info 'Generando registro de InfonavitDet '
			det=new InfonavitDet(
				ejercicio:ejercicio,
				bimestre:bimestre,
				diasDelBimestre:diasDelBimestre,
				fechaInicial:periodo.fechaInicial,
				fechaFinal:periodo.fechaFinal)
			infonavit.addToPartidas(det)
		}
		det.cuota=infonavit.cuotaFija
		det.seguroDeVivienda=15.00
		det.salarioDiarioIntegrado=infonavit.empleado.salario.salarioDiarioIntegrado
		det.salarioMinimoGeneral=zona.salario
		det.faltas=0
		det.incapacidades=0
		det.saldo=infonavit.ultimaDiferencia
		infonavit.bimestreActual=bimestre
		infonavit.salarioMinimoGeneral=det.salarioMinimoGeneral
		infonavit.salarioDiarioIntegrado=det.salarioDiarioIntegrado
		infonavit.diasDelBimestre=det.diasDelBimestre
		
		infonavit.seguroDeVivienda=det.seguroDeVivienda
		 
		
		//Evaluando tipo
		switch (infonavit.tipo){
			case 'VSM':
				calcularDescuentoVSM(det)
				break
			case 'CUOTA_FIJA':
				calcularDescuentoCuotaFija(det)
				break
			case 'PORCENTAJE':
				calcularDescuentoPorcentaje(det)
			default:
				break
		}
		infonavit.importeBimestral=det.importeBimestral
		return det
		
		//Calcular faltas e incapacidades
    }
	
	def calcularDescuentoVSM(InfonavitDet det){
		def cuotaMensual=det.infonavit.cuotaFija*det.salarioMinimoGeneral
		det.importeBimestral=cuotaMensual*2
		def bimestre=det.importeBimestral+det.seguroDeVivienda+det.saldo
		det.cuotaDiaria=bimestre/det.diasDelBimestre
		det.infonavit.cuotaDiaria=det.cuotaDiaria
		log.debug "(Descuento VSM) Cuota diaria:$det.cuotaDiaria Importe bimestral:$det.importeBimestral Cuota bimestral:$det.cuotaBimestral"
	}
	
	def calcularDescuentoPorcentaje(InfonavitDet det){
		def sdi=det.salarioDiarioIntegrado
		det.cuotaDiaria=MonedaUtils.round(sdi*(det.infonavit.cuotaFija/100),4)
		//log.debug 'Cuota diaria calculada: '+(sdi*(det.infonavit.cuotaFija/100))
		det.infonavit.cuotaDiaria=det.cuotaDiaria
		det.importeBimestral=det.cuotaDiaria*det.diasDelBimestre
		def bimestre=det.importeBimestral+det.seguroDeVivienda+det.saldo
		det.cuotaDiaria=bimestre/det.diasDelBimestre
		det.infonavit.cuotaDiaria=det.cuotaDiaria
		log.debug "(Descuento en Porentaje) Cuota diaria:$det.cuotaDiaria Importe bimestral:$det.importeBimestral Cuota bimestral:$det.cuotaBimestral"
		
	}
	
	def calcularDescuentoCuotaFija(InfonavitDet det){
		def cuotaMensual=det.infonavit.cuotaFija*2
		det.cuotaDiaria=MonedaUtils.round(cuotaMensual/det.diasDelBimestre,4)
		det.importeBimestral=det.cuotaDiaria*det.diasDelBimestre
		def bimestre=det.importeBimestral+det.seguroDeVivienda+det.saldo
		det.cuotaDiaria=bimestre/det.diasDelBimestre
		det.infonavit.cuotaDiaria=det.cuotaDiaria
		log.info "(Descuento en Cuota Fija) Cuota diaria:$det.cuotaDiaria Importe bimestral:$det.importeBimestral Cuota bimestral:$det.cuotaBimestral"
	}
	
	def geAcumuladoActual(Infonavit infonavit,Integer ejercicio){
		if(infonavit.bimestreActual){
			def bimestre=infonavit.bimestreActual
			String hql="select sum(d.importeExcento) from NominaPorEmpleadoDet d where "+
					" d.concepto.clave=? and d.parent.nomina.calendarioDet.calendario.ejercicio=? "+
					" and d.parent.nomina.calendarioDet.bimestre=? "+
					" and d.parent.empleado=? "
			def abonos=NominaPorEmpleadoDet.executeQuery(hql,['D006',ejercicio,bimestre,infonavit.empleado]).get(0)
			log.info 'Abonos: '+abonos
			return abonos?:0.0
		}else{return 0.0}
		
	}
}
