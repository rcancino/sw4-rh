<%@ page import="com.luxsoft.sw4.rh.Asistencia" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<%-- <meta name="layout" content="dashboard_1"/>--%>
	<title>Asistencia</title>
	<r:require module="forms"/>
	<r:require module="jquery-ui"/>
</head>
<body>

	<div class="container">

		<div class="row">
			<div class="col-md-12">
				<div class="page-header well">
					<h4>
						Control de asistencia:
						${asistenciaInstance.empleado} (${asistenciaInstance.empleado.perfil.numeroDeTrabajador})
						- ${asistenciaInstance.tipo} ${asistenciaInstance?.calendarioDet?.folio} ${asistenciaInstance.empleado.perfil.ubicacion.clave}
						
					</h4>
					<g:if test="${ flash.message }">
						<span class="label label-warning text-center"> ${flash.message}
						</span>
					</g:if>
					<g:render template="form"/>
				</div>
			</div>
		</div>
		
		<!-- end .row 1-->

		<div class="row">
			<div class="col-md-12">
				<div class="button-panel">
					
					<div class="btn-group">
						<g:link class="btn btn-default" action="previous" id="${asistenciaInstance.id}"> 
							<span class="glyphicon glyphicon-arrow-left"></span> Anterior 
						</g:link>
						<g:link class="btn btn-default" action="next" id="${asistenciaInstance.id}"> 
							<span class="glyphicon glyphicon-arrow-right"></span> Siguiente 
						</g:link>
						<g:link class="btn btn-default" action="index" id="${asistenciaInstance.id}" 
							params="[calendario:asistenciaInstance?.calendarioDet?.id]"> 
							<span class="glyphicon glyphicon-list"></span> Asistencias 
						</g:link>
						
						<g:link class="btn btn-default" action="nomina" id="${asistenciaInstance.id}"> 
							<span class="glyphicon glyphicon-usd"></span> Nómina 
						</g:link>
						
						<a class="btn btn-default" data-toggle="modal" data-target="#cambiarEmpleadoDialog"> 
							<span class="glyphicon glyphicon-search"></span> Buscar
						</a>
						
					</div>

					<div class="btn-group">
						<button type="button" name="reportes"
							class="btn btn-default dropdown-toggle" data-toggle="dropdown"
							role="menu">
							Reportes <span class="caret"></span>
						</button>
						<ul class="dropdown-menu">
							<li><g:jasperReport jasper="TarjetaDeAsistencia"
									format="PDF" name="Tarjeta">
									<g:hiddenField name="ID" value="${asistenciaInstance.empleado.id}" />
									<g:hiddenField name="CALENDARIO_ID" 
										value="${asistenciaInstance.calendarioDet.id}" />
									
								</g:jasperReport>
							</li>
							<li><g:jasperReport jasper="RetardoMensualPorEmpleado"
									format="PDF" name="Retardo mensual">
									<g:hiddenField name="ID" 
										value="${asistenciaInstance.empleado.id}" />
									
								</g:jasperReport>
							</li>
							<li>
							
								<a  data-toggle="modal" data-target="#calendarioForm"> Mensual X Empleado</a>
							</li>
							
							<li>
								<a data-toggle="modal" data-target="#periodoCalendarioForm"> Tarjeta X Periodo</a>
							</li>
						</ul>
					</div>
					
					<!-- Fin .btn-group -->

					<div class="btn-group">
						<button type="button" name="reportes"
							class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							Operaciones <span class="caret"></span>
						</button>
						<ul class="dropdown-menu">
							<li><g:link 
									action="actualizar" id="${asistenciaInstance.id}">
									<span class="glyphicon glyphicon-cog"></span> Actualizar
						</g:link></li>
							<li><g:link action="delete" id="${asistenciaInstance.id}"
									onclick="return confirm('Eliminar toda la asistencia?');">
									<span class="glyphicon glyphicon-trash"></span> Eliminar
						</g:link></li>
						</ul>
					</div>
					<div class="btn-group">
						
						<g:form class="form-inline" role="form" 
							controller="asistencia" 
							action="actualizar" id="${asistenciaInstance.id}">
							
							<label class="" for="diasTrabajadosField">Dias </label>
  							<div class="form-group">
    							<input type="text"
    								name="diasTrabajados" 
    								class="form-control" 
    								id="diasTrabajadosField" 
    								value="${asistenciaInstance.diasTrabajados}">
  							</div>
  							
  							<label class="" for="faltasManualesField">Faltas manuales </label>
  							<div class="form-group">
    							<input type="text"
    								name="faltasManuales" 
    								class="form-control" 
    								id="faltasManualesField" 
    								value="${asistenciaInstance.faltasManuales}">
  							</div>
  							<div class="form-group">
  								<label class="" for="diasTrabajadosField">Min a desc </label>
    							<input type="text"
    								name="minutosPorDescontar" 
    								class="form-control " 
    								id="minutosPorDescontarField" 
    								value="${asistenciaInstance.minutosPorDescontar}">
    							<button type="submit" class="btn btn-default">
									<span class="glyphicon glyphicon-ok"></span> 
								</button>
  							</div>
  							
  						</g:form>
					</div>
					
					<!-- Fin .btn-group -->

				</div>
			</div>
		</div> <!-- End row 2 -->
		
		<div class="row">
			<div class="col-md-12">
				<div class="grid-panel">
					<g:render template="asistenciaDetGridPanel" />
				</div>
			</div>
		</div>
		<!-- End row 3 -->

		<g:render template="cambiarEmpleadoDialog"/>
		
	</div> <!-- End container -->
	
	

<r:script>
	$(function(){
		$(".decimalField").attr("type",'text');
		$(".decimalField").autoNumeric({vMin:'0.00',wEmpty:'zero',mDec:'4'});
	
	});
</r:script>
	
<g:render template="calendarioPeriodoDialog" model="[periodos:periodos]"/>
<g:render template="periodoDialog" model="[periodos:periodos]"/>
	

</body>
</html>
