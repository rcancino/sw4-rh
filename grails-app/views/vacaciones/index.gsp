<%@ page import="com.luxsoft.sw4.rh.Vacaciones" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operaciones"/>
	<title>Vacaciones</title>
</head>
<body>
	<content tag="header">
		<h3>Control de vacaciones</h3>
	</content>
	<content tag="consultas">
		<nav:menu scope="app/operaciones/asistencia" class="nav nav-tabs nav-stacked" path=""/>
	</content>
	<content tag="gridTitle">Solicitudes de vacaciones (${tipo})</content>
	
	<content tag="gridTasks">
		<g:link action="index" class="btn btn-default">
			<span class="glyphicon glyphicon-repeat"></span> Refrescar
		</g:link>
		<g:link action="create" class="btn btn-default">
			<span class="glyphicon glyphicon-floppy-saved"></span> Nuevo
		</g:link>
			<button class="btn btn-default" data-toggle="modal" data-target="#searchForm">
				<span class="glyphicon glyphicon-search"></span> Buscar
			</button>
		<g:link action="index" class="btn btn-default">
			<span class="glyphicon glyphicon-filter"></span> Filtrar
		</g:link>
		<g:link action="index" class="btn btn-default ${tipo=='QUINCENAL'?'active':''}" params="[tipo:'QUINCENAL']">
			<span class="glyphicon glyphicon-filter"></span> Quincenal
		</g:link>
		<g:link action="index" class="btn btn-default ${tipo=='SEMANAL'?'active':''}" params="[tipo:'SEMANAL']">
			<span class="glyphicon glyphicon-filter"></span> Semanal
		</g:link>
	</content>
	
	<content tag="gridPanel">
		<g:render template="gridPanel"/>
	</content>
	
	
	
</body>
</html>