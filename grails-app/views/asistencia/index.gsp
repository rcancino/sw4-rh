<%@ page import="com.luxsoft.sw4.rh.Asistencia" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operaciones"/>
	<title>Lista de asistencia</title>
</head>
<body>
	<content tag="header">
		<h3>Control de asistencia</h3>
	</content>
	<content tag="consultas">
		<nav:menu scope="app/operaciones/asistencia" class="nav nav-tabs nav-stacked" path=""/>
	</content>
	<content tag="gridTitle">Lista de asistencia periodo:  ${session?.periodo} </content>
	<content tag="gridTasks">
  		<g:link class="btn btn-default" action="actualizarAsistencia">
  			Actualizar
  		</g:link>
	</content>
	<content tag="gridPanel">
		<g:render template="asistenciaGridPanel"/>
	</content>
	
</body>
</html>