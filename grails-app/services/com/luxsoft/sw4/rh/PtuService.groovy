package com.luxsoft.sw4.rh

import grails.transaction.Transactional
import com.luxsoft.sw4.Periodo
import com.luxsoft.sw4.rh.tablas.ZonaEconomica
import com.luxsoft.sw4.rh.imss.*
import com.luxsoft.sw4.rh.tablas.SubsidioEmpleo
import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef

import java.math.RoundingMode

@Transactional
class PtuService {
	

	def save(Ptu ptu){
        ptu?.partidas?.clear()
        def empleados=buscarEmpleadosDelEjercicio ptu.ejercicio
        empleados.each{
        	def det=actualizarPercepciones(new PtuDet(empleado:it,noAsignado:false),ptu.ejercicio)
        	ptu.addToPartidas(det)
        }
        ptu.save failOnError:true
        recalcular ptu
        return ptu
	}

	def buscarEmpleadosDelEjercicio(def ejercicio){
		def list=NominaPorEmpleado
            .executeQuery("select distinct n.empleado from NominaPorEmpleado n where n.nomina.ejercicio=?",ejercicio)
        return list
	}
    

    def actualizarPercepciones(PtuDet ptuDet,def ejercicio){
    	
    	def SALARIO='P001'
		def VACACIONES='P025'
		def COMISION='P029'
		def RETARDOS ='D012'
    	def movimientos=NominaPorEmpleadoDet
    		.executeQuery(
    			"from NominaPorEmpleadoDet det where det.parent.nomina.ejercicio=? "
    			+" and det.parent.empleado=? "
    			+" and det.concepto.clave in(?,?,?,?)"
    			,[ejercicio,ptuDet.empleado
    			,SALARIO,VACACIONES,COMISION,RETARDOS
    			])
    	ptuDet.salario=movimientos.sum(0.0){it.concepto.clave==SALARIO?it.getTotal():0.0}
    	ptuDet.vacaciones=movimientos.sum(0.0){it.concepto.clave==VACACIONES?it.getTotal():0.0}
    	ptuDet.comisiones=movimientos.sum(0.0){it.concepto.clave==COMISION?it.getTotal():0.0}
    	ptuDet.retardos=movimientos.sum(0.0){it.concepto.clave==RETARDOS?it.getTotal():0.0}
        if(ptuDet.empleado.id==209){
            ptuDet.retardos-=72.86
        }
        if(ptuDet.empleado.id==54){
            ptuDet.retardos-=6.07
        }
    	ptuDet.total=(ptuDet.salario+ptuDet.vacaciones+ptuDet.comisiones)-ptuDet.retardos
    	log.info " ptuDet $ptuDet.empleado( $ejercicio ) actualizado"
    	return ptuDet
    }

    def recalcular(Ptu ptu){
        def tope=ptu.getSalarioTope()
        ptu.partidas.each{
            it.topeAnual=it.total>tope?tope:it.total
            it.diasDelEjercicio=calcularDiasDelEjercicio it
            it.diasPtu=it.diasDelEjercicio-it.faltas-it.incapacidades-it.permisosP
           //Fix temporal
            if(it.empleado.id==45){
                it.diasPtu=292
            }
            it.noAsignado=it.diasPtu<60 
            if(it.empleado.id==260 || it.empleado.id==280 || it.empleado.id==246){
                it.noAsignado=true
            }
        }

        ptu.montoDias=ptu.total*0.5
        ptu.montoSalario=ptu.total-ptu.montoDias
        ptu.diasPtu=ptu.partidas.sum 0,{
            if(!it.noAsignado) 
                it.diasPtu
            else
                0
        }
        ptu.topeAnualAcumulado=ptu.partidas.sum 0.0,{
            if(!it.noAsignado) 
                it.topeAnual
            else
                0
        }
        ptu.factorDias=ptu.montoDias/ptu.diasPtu
        ptu.factorSalario=ptu.montoSalario/ptu.topeAnualAcumulado
        ptu.partidas.each{
            if(!it.noAsignado){
                it.montoDias=it.diasPtu*ptu.factorDias
                it.montoSalario=it.topeAnual*ptu.factorSalario    
            }else{
                it.montoDias=0.0
                it.montoSalario=0.0
            }
            
        }
        calcularImpuestos ptu
        ptu.sindicalizadoMaximo=ptu.getEmpleadoTope()?.getSalarioNeto()
        ptu.sindicalizadoNombre=ptu.getEmpleadoTope()?.empleado?.nombre
        ptu.save flush:true
        return ptu
    }


    def calcularDiasDelEjercicio(PtuDet ptuDet){

        def periodo=ptuDet.getPeriodo()
        def alta=ptuDet.empleado.alta
        def baja=ptuDet.empleado?.baja?.fecha

        def de=0
        if(!baja){
          if(alta<periodo.fechaInicial){
            
            de=periodo.fechaFinal-periodo.fechaInicial+1
          }else{
            de=periodo.fechaFinal-alta+1
          }
        }else{
          if(baja<periodo.fechaInicial && (baja>alta)){
            de=0
          }else{
            def fechaSuperior=(baja<periodo.fechaFinal && baja>alta)?baja:periodo.fechaFinal
            def fechaDeInicio=alta<periodo.fechaInicial?periodo.fechaInicial:alta
            de=fechaSuperior-fechaDeInicio+1
          }
        }
    }

    def calcularImpuestos(Ptu ptu){
        def zona=ZonaEconomica.findByClave('A')
        ptu.salarioMinimoGeneral=zona.salario
        ptu.topeSmg=ptu.salarioMinimoGeneral*15
        ptu.partidas.each{
            it.ptuExcento=it.montoPtu>=ptu.topeSmg?ptu.topeSmg:it.montoPtu
            it.ptuGravado=it.montoPtu-it.ptuExcento
            it.salarioDiario=it.empleado.salario.salarioDiario
            if(it.empleado.salario.periodicidad=='QUINCENAL'){
                it.salarioMensual=it.salarioDiario*31
                if(it.noAsignado || it.empleado.baja){
                    it.salarioMensual=0.0
                }
            }
            else{
                it.salarioMensual=it.salarioDiario*28
                if(it.noAsignado || it.empleado.baja){
                    it.salarioMensual=0.0
                }
            }
            it.incentivo=it.salarioMensual*0.10
            it.totalMensualGravado=it.ptuGravado+it.salarioMensual+it.incentivo
            it.with{
                def base=totalMensualGravado
                tmgIsr=calcularImpuesto(base)
                tmgSubsidio=buscarSubsidio(base)?:0.0
                tmgResultado=tmgIsr-tmgSubsidio

                // Impuestos de salarioMensual+incentivo

                base=salarioMensual+incentivo
                smiIsr=calcularImpuesto(base)
                smiSubsidio=buscarSubsidio(base)?:0.0
                smiResultado=smiIsr-smiSubsidio

                isrPorRetener=tmgResultado-smiResultado

            }
            calcularPago it
           
        }
    }

    
    
    private BigDecimal calcularImpuesto(BigDecimal percepciones){
        def tarifa =TarifaIsr.obtenerTabla(30.4)
        .find(){(percepciones>it.limiteInferior && percepciones<=it.limiteSuperior)}
        if(!tarifa) return 0.0
        //assert tarifa,'No encontro tarifa en las tablas de ISR para una percepcion de: '+percepciones
        
        
        def importeGravado=percepciones-tarifa.limiteInferior
        importeGravado*=tarifa.porcentaje
        importeGravado/=100
        importeGravado+=tarifa.cuotaFija
        importeGravado=importeGravado.setScale(2,RoundingMode.HALF_EVEN)
        return importeGravado
    }

    def subsidios

    def buscarSubsidio(def valor){
       
        if(!subsidios)
            subsidios=SubsidioEmpleo.findAllByEjercicio(2015)
        def found= subsidios.find(){ it ->
            (valor>it.desde && valor<=it.hasta)
        }
        
        return found?.subsidio
    }

    def calcularPago(PtuDet ptuDet){

        if(!ptuDet.noAsignado){
            def calculoAnual=CalculoAnual.findByEjercicioAndEmpleado(ptuDet.ptu.ejercicio,ptuDet.empleado)
            if(calculoAnual){
                ptuDet.isrAcreditable=(calculoAnual.resultado-calculoAnual.aplicado)!=0.0?(calculoAnual.resultado-calculoAnual.aplicado):0.0
            }  
        }else{
            ptuDet.isrAcreditable=0.0
        }  

        
        
        ptuDet.porPagarBruto=ptuDet.ptuExcento+ptuDet.ptuGravado+ptuDet.isrAcreditable-ptuDet.isrPorRetener
        
        def percepcion=ptuDet.porPagarBruto
        
        def pension=buscarPension(ptuDet.empleado)
        if(pension){
            def importeP=0.0
            
            if(!pension.neto){
                importeP=(ptuDet.ptuExcento+ptuDet.ptuGravado)*(pension.porcentaje/100)
            }else{
                importeP=(ptuDet.porPagarBruto)*(pension.porcentaje/100)
            }
            ptuDet.pensionA=importeP
            percepcion-=ptuDet.pensionA
        }

        if(ptuDet.empleado.status!='BAJA'){
            percepcion*=0.75
        }else{
            percepcion*=0.90
        }
        
        def otraDeducciones=buscarOtrasDeducciones(ptuDet.empleado)
        
        if(otraDeducciones){
            
            if(otraDeducciones<=percepcion){
                ptuDet.otrasDed=otraDeducciones
                percepcion-=ptuDet.otrasDed
            }else{
                ptuDet.otrasDed=percepcion
            }
        }
        
        def prestamo=buscarPrestamo(ptuDet.empleado)
        
        if(prestamo){
            if(prestamo<=percepcion){
                ptuDet.prestamo=prestamo
            }else{
                ptuDet.prestamo=percepcion
            }
        }
        ptuDet.porPagarNeto=ptuDet.porPagarBruto-ptuDet.pensionA-ptuDet.otrasDed-ptuDet.prestamo
        
    }
    
    private PensionAlimenticia buscarPension(Empleado e) {
        def pensiones=PensionAlimenticia.findAll("from PensionAlimenticia p where p.empleado=?"
            ,[e],[max:1])
        return pensiones?pensiones[0]:null
    }
    
    private  buscarPrestamo(Empleado e) {
        def prestamos=Prestamo.findAll("from Prestamo p where p.saldo>0 and p.empleado=? order by p.saldo desc"
            ,[e])
        return prestamos.sum {it.saldo}
    }
    
    private  buscarOtrasDeducciones(Empleado e){
        def rows=OtraDeduccion.findAll("from OtraDeduccion d where d.saldo>0 and d.empleado=? order by d.saldo desc"
            ,[e])
        return rows.sum {it.saldo}
    }


   
    def reporte(Ptu ptu){
        params.reportName='PtuGeneral'
        params['EJERCICIO']=session.ejercicio
        ByteArrayOutputStream  pdfStream=runReport(params)
        String fileName=params.reportName+'_'+(new Date().format('dd_mm_yyyy_hh_MM'))
        render(file: pdfStream.toByteArray(), contentType: 'application/pdf',fileName:params.reportName)
    }

    private runReport(Map repParams){
        log.info 'Ejecutando reporte  '+repParams
        def nombre=repParams.reportName
        def reportDef=new JasperReportDef(
            name:nombre
            ,fileFormat:JasperExportFormat.PDF_FORMAT
            ,parameters:repParams
            )
        ByteArrayOutputStream  pdfStream=jasperService.generateReport(reportDef)
        return pdfStream
        
    }

    
}
