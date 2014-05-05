navigation={
	app{
		home()
		catalogos(){
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
				asistencia(action:'index')
				lectora()
				
				
			}
			aportaciones()
			incapacidades()
			prestamo(controller:'prestamo',action:'index',titleText:'Prestamos '){
				vigentes()
				pagados()
				reportes()

			}
			vacaciones()
			tiempoExtras(controller:'tiempoExtra',action:'index')
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