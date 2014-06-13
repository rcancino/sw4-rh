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
	<content tag="gridTitle">Registros de lectora periodo:  ${periodo}  Registros:${checadoTotalCount}</content>
	<content tag="gridTasks">
  		
  		<button class="btn btn-default" data-toggle="modal" data-target="#periodoForm">
			<span class="glyphicon glyphicon-import"></span> Importar
		</button>
		<g:render template="/_common/periodoForm" model="[action:'importarLecturas',periodo:periodo]"/>
		
		<button class="btn btn-default" data-toggle="modal" data-target="#cargaPeriodoForm">
			<span class="glyphicon glyphicon-refresh"></span> Cargar
		</button>
		
  		<g:link class="btn btn-default" action="eliminarRegistrosLectora">
  			<span class="glyphicon glyphicon-trash"></span> Eliminar
  		</g:link>
	</content>
	<content tag="gridPanel">
		<g:render template="lecturasGridPanel"/>
		
		
	</content>
	
	
	


	
</body>
</html>