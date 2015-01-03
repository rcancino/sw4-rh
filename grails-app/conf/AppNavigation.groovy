navigation={
	app{
		home()
		
		catalogos(){
			calendario()
			turnos(controller:'turno',action:'index')
			puesto(controller:'puesto',action:'index')
			empleado(controller:'empleado',action:'index'){
				catalogo(controller:'empleado',action:'index')
				generales()
				datosPersonales()
				perfil()
				salario()
				seguridadSocial()
				contactos()
				pension(controller:'pensionAlimenticia',action:'edit')
				documentos()
				asistencia()
				reportes()

			}
			departamento(controller:'departamento',action:'index')
			ubicacion(controller:'ubicacion',action:'index')
			conceptos(controller:'conceptoDeNomina', action:'index'){
				percepciones()
				deducciones()
			}
			diasFestivos(controller:'diasFestivos',action:'index')
            sat(controller:'catalogosDelSat'){
            	bancos()
            	percepciones()
            	deducciones()
            	incapacidades()
            	regimenes(titleText:'Tipos de Régimen')  
            	riesgos()
            }
			tablas(controller:'tablas',action:'index'){
				tarifaIsr()
				subsidio()
				factorDeIntegracion()
			}
		}
		operaciones(){
			
			nomina(controller:'nomina',action:'index')
			
			recibos(controller:'reciboDeNomina',action:'index',titleText:'Recibos (Quincenal)')
			recibosSemanal(controller:'reciboDeNomina',action:'semanal',titleText:'Recibos (Semanal)')
			asistencia(controller:'asistencia',action:'index',titleText:'Control de asistencia'){
				asistencia(action:'index',titleText:'Asistencia')
				lectora()
				incapacidad(controller:'incapacidad',action:'index',titleText:'Incapacidades')
				incidencias(controller:'incidencia',action:'index')
				vacaciones(controller:'vacaciones',action:'index')
				tiempoExtra(controller:'tiempoExtra',action:'index',titleText:'Compensaciones')
			}
			incentivo(controller:'incentivo',action:'index')
			
			
			prestamo(controller:'prestamo',action:'index',titleText:'Prestamos '){
				vigentes()
				pagados()
			}
			infonavit(controller:'infonavit',action:'index')
			fonacot(controller:'fonacot',action:'index')
			vacaciones(controller:'controlDeVacaciones',action:'index')
			tiempoExtra(controller:'tiempoExtra',action:'index')
			otrasDeducciones(controller:'otraDeduccion',action:'index')
			registroDeComisiones(controller:'registroDeComisiones',action:'index')
			genericas(controller:'operacionGenerica',action:'index')
		}
		procesos(){
			empleados()
			//salarioDiarioIntegrado()
			calculoSdi(controller:'calculoSdi',action:'index',titleText:'Calculo bimestral SDI')
			acumulados(controller:'acumuladoPorConcepto',action:'index')
			modificacionSalarial(controller:'modificacionSalarial',action:'index')
			aguinaldo(controller:'aguinaldo',action:'index')
			calculoAnual(controller:'calculoAnual',action:'index')
			exportador(controller:'exportador',action:'index',titleText:'Layouts')
		}
		configuracion(){
			reglasDeEjecuccion(controller:'brNomina',action:'index',titleText:'Reglas de proceso'){
				porNomina()
				porConcepto()
			}
			concepto(controller:'',action:'index')
			
		}
		reportes(controller:'reporte'){
			nomina(controller:'reporte'){
				impuestoSobreNominas(controller:'reporte',titleText:'Impusto sobre nominas')
				tiempoExtra()
				detallePorConcepto()
				acumuladoDeNominasPorConcepto()
				calificacionDeIncentivos()
			}
			salarios(controller:'reporte'){
				historicoDeSalarios(controller:'reporte',titleText:'Historico de salarios')
				incrementosIndividuales()
			}
			contratacion(controller:'contratacion'){
				contrato()
				solicitud(titleText:'Solicitud de empleo')
				recepcionDeDocumentos()
				induccion()
				entregaDeDocumentos()
				constanciaDeNoEmbarazo()
				constanciaDeNoFamiliares()
				solicitudDeTarjetaDeNomina()
				/*nuevos*/
				referenciasLaborales()
				actualizacionExpedientesPersonales()
				cambioPuesto()
				dc3(titleText:'DC3')
				entrevistaSalida()
				evaluacionNvoIngreso()
				solicitudPrestamos()
				solicitudVacaciones()
			}
			asistencia(controller:'reporte'){
				bitacoraDeChecado()
				vacacionesEjercicio()
				vacacionesEmpleado()
				incapacidades()
				incapacidadesEmpleado()
				faltasIncapacidades()
				faltasIncapacidadesPeriodo()
				minutosPorPagar()
				ausentismoPorDia()
				
			}
			
			prestamos(controller:'reporte',titleText:'Prestamos'){
				historicoDePrestamos()
				
		}
			
		}
		exportadores(controller:'exportador'){
			nominaBanamex()
			altasBajasImss()
			idse(titleText:'IDSE')
			sua(titleText:'SUA')
		}
		 
	}
}