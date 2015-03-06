<%@ page import="com.luxsoft.sw4.rh.Incidencia" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operaciones"/>
	<r:require modules="datatables"/>
	<title>Incidencias</title>
</head>
<body>
	<content tag="header">
		<h3>Control de incidencias</h3>
	</content>
	<content tag="consultas">
		<nav:menu scope="app/operaciones/asistencia" class="nav nav-tabs nav-stacked" path=""/>
	</content>
	<content tag="gridTitle">Incidencias  (${session.ejercicio})</content>
	
	<content tag="gridTasks">

		<div class="btn-group">
			<input type='text' id="nombreField" placeholder="Empleado" class="form-control" autofocus="autofocus">
		</div>
		<div class="btn-group">
			<input type='text' id="ubicacionField" placeholder="Ubicacion" class="form-control" >
		</div>
		<div class="btn-group">
			<input type='text' id="periodicidadField" placeholder="Periodicidad" class="form-control" >
		</div>
		<g:link action="index" class="btn btn-default">
			<span class="glyphicon glyphicon-repeat"></span> Refrescar
		</g:link>
		<g:link action="create" class="btn btn-default">
			<span class="glyphicon glyphicon-floppy-saved"></span> Nuevo
		</g:link>

		
	</content>
	
	<content tag="gridPanel">
		<g:render template="gridPanel"/>
	</content>
	
	
	
</body>
</html>