<%@ page import="com.luxsoft.sw4.rh.Checado" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operaciones"/>
	<title>Lectora</title>
</head>
<body>
	<content tag="header">
		<h3>Control de asistencia</h3>
	</content>
	<content tag="consultas">
		
  		<nav:menu scope="app/operaciones/asistencia" class="nav nav-tabs nav-stacked" path=""/>
		
	</content>
	<content tag="gridTitle">Registros de lectora periodo:  ${session?.periodo}  Registros:${checadoTotalCount}</content>
	<content tag="gridTasks">
  		<g:link class="btn btn-default" action="importarLecturas">
  			<span class="glyphicon glyphicon-import"></span> Importar
  		</g:link>
  		<g:link class="btn btn-default" action="eliminarRegistrosLectora">
  			<span class="glyphicon glyphicon-trash"></span> Eliminar
  		</g:link>
	</content>
	<content tag="gridPanel">
		<g:render template="lecturasGridPanel"/>
	</content>
	
</body>
</html>