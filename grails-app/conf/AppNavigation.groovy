navigation={
	app{
		home()
		
		catalogos(){
			calendario()
			puesto(controller:'puesto',action:'index')
			empleado(controller:'empleado',action:'index'){
				catalogo(controller:'empleado',action:'index')
				generales()
				datosPersonales()
				perfil()
				salario()
				seguridadSocial()
				contactos()
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
				asistenciaQuincenal(action:'asistenciaQuincenal',titleText:'Quincenal')
				asistenciaSemanal(action:'asistenciaSemanal',titleText:'Semanal')
				lectora()
				incapacidad(controller:'incapacidad',action:'index',titleText:'Incapacidades')
				incidencias(controller:'incidencia',action:'index')
				vacaciones(controller:'vacaciones',action:'index')
				tiempoExtra(controller:'tiempoExtra',action:'index',titleText:'Compensaciones')
			}
			incentivo(controller:'incentivo',action:'index')
			aportaciones()
			
			prestamo(controller:'prestamo',action:'index',titleText:'Prestamos '){
				vigentes()
				pagados()
				reportes()

			}
			
			
			bonos(){
				productividad()
				desempeno()
				comisiones()
				incentivo()
				compensaciones()
			}
			solicitudes()
		}
		procesos(){
			empleados()
			salarioDiarioIntegrado()
			acumulados(controller:'acumuladoPorConcepto',action:'index')
		}
		configuracion(){
			reglasDeEjecuccion(controller:'brNomina',action:'index',titleText:'Reglas de proceso'){
				porNomina()
				porConcepto()
			}
			concepto(controller:'',action:'index')
			
		}
	}
}