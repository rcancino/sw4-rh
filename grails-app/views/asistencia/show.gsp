<%@ page import="com.luxsoft.sw4.rh.Asistencia" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<%-- <meta name="layout" content="dashboard_1"/>--%>
	<title>Asistencia</title>
</head>
<body>

	<div class="container">

		<div class="row">
			<div class="col-md-12">
				<div class="page-header">
					<h3>
						Control de asistencia:
						${asistenciaInstance.empleado} (${asistenciaInstance.empleado.perfil.numeroDeTrabajador})
						
					</h3>
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
						<g:link action="index" class="btn btn-default">
							<span class="glyphicon glyphicon-repeat"></span> Refrescar
						</g:link>

						<g:link action="create" class="btn btn-default">
							<span class="glyphicon glyphicon-search"></span> Buscar
						</g:link>
						
						<g:link action="create" class="btn btn-default">
							<span class="glyphicon glyphicon-filter"></span> Filtrar
						</g:link>
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
									<g:hiddenField name="ID" value="${asistenciaInstance.id}" />
									<g:hiddenField name="FECHA_INI" 
										value="${g.formatDate(date:asistenciaInstance.periodo.fechaInicial,format:'yyyy/MM/dd')}" />
									<g:hiddenField name="FECHA_FIN" 
										value="${g.formatDate(date:asistenciaInstance.periodo.fechaFinal,format:'yyyy/MM/dd')}" />
								</g:jasperReport>
							</li>
							<li><g:jasperReport jasper="AsistenciaRH"
									format="PDF" name="Asistencia RH">
									<g:hiddenField name="SFECHA_INI" 
										value="${g.formatDate(date:asistenciaInstance.periodo.fechaInicial,format:'dd/MM/yyyy')}" />
									<g:hiddenField name="SFECHA_FIN" 
										value="${g.formatDate(date:asistenciaInstance.periodo.fechaFinal,format:'dd/MM/yyyy')}" />
								</g:jasperReport>
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
		
	</div> <!-- End container -->
	
	

	
	

	

</body>
</html>
