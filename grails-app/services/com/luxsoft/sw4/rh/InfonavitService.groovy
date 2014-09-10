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
		log.info "Periodo $periodo"
		def det=InfonavitDet.find{infonavit==infonavit && ejercicio==ejercicio && bimestre==bimestre}
		if(det==null){
			log.info 'Generando registro de InfonavitDet '
			det=new InfonavitDet(
				ejercicio:ejercicio,
				bimestre:bimestre)
		}
    }
}
