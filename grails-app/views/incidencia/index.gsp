<%@ page import="com.luxsoft.sw4.rh.Incidencia" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operaciones"/>
	<title>Incidencias</title>
</head>
<body>
	<content tag="header">
		<h3>Control de incidencias</h3>
	</content>
	<content tag="consultas">
		<nav:menu scope="app/operaciones/asistencia" class="nav nav-tabs nav-stacked" path=""/>
	</content>
	<content tag="gridTitle">Incidencias</content>
	
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
		<button class="btn btn-default" data-toggle="modal" data-target="#filterForm">
			<span class="glyphicon glyphicon-filter"></span> Filtrar
		</button>
		
	</content>
	
	<content tag="gridPanel">
		<g:render template="gridPanel"/>
	</content>
	
	
	
</body>
</html>