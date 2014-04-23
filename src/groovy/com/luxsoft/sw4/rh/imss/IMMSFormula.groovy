package com.luxsoft.sw4.rh.imss

import com.luxsoft.sw4.*
import com.luxsoft.sw4.rh.*

import java.math.*

/**
 * Clase temporal para calcular la retencion del seguro social
 * 
 * @author Ruben Cancino 
 *
 */
class IMMSFormula {
	
	
	def actualizarDeduccion(NominaPorEmpleado nominaEmpleado){
		
		def empleado=nominaEmpleado.empleado
		
		def salarioMinimo=ZonaEconomica.valores.find(){it.clave='A'}.salario
		def sdi=empleado.salario.salarioDiarioIntegrado
		def diasTrabajados=nominaEmpleado.nomina.getDiasPagados()
		
		def prima=0.5 //Numer magico por el momento
		
		def aporacionAsegurado=0.0
		
		//EyM sobre 1 SMGDF
		//aportacionAsegurado=0
		
	
		//'EyM sobre dif. entre SBC y 3 SMGDF
		def emd=0.0
		if(sdi<(salarioMinimo*25)){
			if(sdi<(salarioMinimo*3)){
				emd=0.0
			}else{
				emd=sdi-(salarioMinimo*3)
			}
		}else{
			emd=(salarioMinimo*25)-(salarioMinimo*3)
		}
		emd=(emd*0.40*diasTrabajados)/100
		emd=emd.setScale(2,RoundingMode.HALF_EVEN)
		println 'EyM sobre dif. entre SBC y 3 SMGDF: '+emd
		aporacionAsegurado+=emd
	
		'Prestaciones en dinero EyM sobre SBC'
		def val3
		if(sdi<(salarioMinimo*1.0452)){
			val3=(salarioMinimo*1.0452)
		}else if(sdi<(salarioMinimo*25)){
			val3=sdi
		}else{
			val3=(salarioMinimo*25)
		}
		def pd=((val3*0.25)*diasTrabajados)/100
		pd=pd.setScale(2,RoundingMode.HALF_EVEN)
		aporacionAsegurado+=pd
		println 'Prestaciones en dinero EyM sobre SBC: '+pd
	
		def gmp=((val3*0.375)*diasTrabajados)/100
		gmp=gmp.setScale(2,RoundingMode.HALF_EVEN)
		aporacionAsegurado+=gmp
		println 'Gastos mdicos pensionado sobre SBC: '+gmp
	
		def iv=((val3*0.625)*diasTrabajados)/100
		iv=iv.setScale(2,RoundingMode.HALF_EVEN)
		aporacionAsegurado+=iv
		println 'Invalidez y Vida sobre SBC: '+iv
	
		def sr=0
		aporacionAsegurado+=sr
		println 'Seguro de Retiro: '+sr
	
		def cv=((val3*1.125)*diasTrabajados)/100
		cv=cv.setScale(2,RoundingMode.HALF_EVEN)
		aporacionAsegurado+=cv
		println 'Cesanta edad avanzada y vejez sobre SBC: '+cv
	
		def sg=0
		aporacionAsegurado+=sg
		println 'Seguro de Guarderas sobre SBC: '+sg
	
		def rt=0
		aporacionAsegurado+=rt
		println 'Riesgos de trabajo: '+rt
	
		def inf=0
		aporacionAsegurado+=inf
		println 'Infonavit: '+inf
	
		nominaEmpleadoDet.importeGravado=aporacionAsegurado
		
	}
	


	

}
