<%@ page import="com.luxsoft.sw4.rh.Vacaciones" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operaciones"/>
	<title>Vacaciones</title>
	<r:require modules="datatables"/>
</head>
<body>
	<content tag="header">
		<h3>Control de vacaciones ${session.ejercicio }</h3>
	</content>
	<content tag="consultas">
		<nav:menu scope="app/operaciones/asistencia" class="nav nav-tabs nav-stacked" path=""/>
	</content>
	<content tag="gridTitle">Solicitudes de vacaciones </content>
	
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
			<r:script>
					$(function(){
						var table=$("#vacacionesGrid").dataTable({
					        "paging":   false,
					        "ordering": false,
					        "info":     false,
					         "dom":'t'
		    				});
		    				
		    				$("#ubicacionField").keyup(function(){
		      					table.DataTable().column(2).search( $(this).val() ).draw();
							});
							$("#nombreField").keyup(function(){
		      					table.DataTable().column(1).search( $(this).val() ).draw();
							});
							$("#periodicidadField").keyup(function(){
		      					table.DataTable().column(3).search( $(this).val() ).draw();
							});
							
					});
			</r:script>
	</content>
	
	
	
</body>
</html>