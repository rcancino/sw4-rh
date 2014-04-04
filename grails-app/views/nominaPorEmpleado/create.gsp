<%@ page import="com.luxsoft.sw4.rh.NominaPorEmpleado" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="dashboard_1"/>
	<title>Nomina</title>
</head>
<body>
	<content tag="header">
		<g:link controller="nomina" action="show" id="${nominaInstance.id}">
			<h4>Nómina: ${nominaInstance.folio} ${nominaInstance.periodicidad} </h4>
		</g:link>
	</content>
	<content tag="buttonBar">
		%{-- <div class="button-panel">
			<div class="btn-group">
			<g:link action="index" class="btn btn-default">
				<span class="glyphicon glyphicon-repeat"></span> Refrescar
			</g:link>
			<g:link action="create" class="btn btn-primary">
				<span class="glyphicon glyphicon-floppy-saved"></span> Alta
			</g:link>
			<g:link action="create" class="btn btn-default">
				<span class="glyphicon glyphicon-search"></span> Buscar
			</g:link>
			<g:link action="create" class="btn btn-default">
				<span class="glyphicon glyphicon-filter"></span> Filtrar
			</g:link>
			</div>
		</div> --}%
		
	</content>
	<content tag="grid">
		<div class="panel panel-default">
			<div class="panel-heading">Alta de nomina </div>
			<div class="panel-body">
				<form action="agregarPartida" class="form-horizontal" role="form">
					<formset>
						<f:with bean="nominaPorEmpleadoInstance">
							<f:field property="empleado"/>
						</f:with>
					</formset>
				</form>
			</div>
		</div>
	</content>

	

</body>
</html>
