
<%@ page import="com.luxsoft.sw4.rh.Nomina" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Nóminas</title>
	<r:require modules="datatables"/>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="alert alert-info">
					<h3>Lista de nóminas ${periodicidad }</h3>
				</div>
				
				<g:if test="flash.message">
					<span class="label label-warning">${flash.message }</span>
				</g:if>
			</div>
		</div>

		<div class="row">
			<div class="col-md-12">
				<ul class="nav nav-tabs">
					<li class="${(periodicidad=='QUINCENAL' )?'active':''}">
						<g:link action="index" params="[periodicidad:'QUINCENAL'	]" >Quincenal</g:link>
					</li>
					<li class="${(periodicidad=='SEMANAL')?'active':''}">
						<g:link action="index" params="[periodicidad:'SEMANAL']">Semanal</g:link>
					</li>
				</ul>
			</div>
		</div>

		<div class="row">
			<div class="col-md-12">
				<div class="button-panel">
					<div class="btn-group">
						<input type='text' id="folioField" placeholder="folio" class="form-control" autocomplete="off" autofocus="autofocus">
					</div>
					<div class="btn-group">
						<input type='text' id="tipoField" placeholder="tipo" class="form-control" autocomplete="off" >
					</div>
					
				<div class="btn-group">
					<g:link action="index" class="btn btn-default">
						<span class="glyphicon glyphicon-repeat"></span> Refrescar
					</g:link>
							
							<button class="btn btn-default" data-toggle="modal" 
								data-target="#agregarNominaForm">
								<span class="glyphicon glyphicon-calendar"></span> Agregar
							</button>

							<button class="btn btn-default" data-toggle="modal" 
								data-target="#reporteDeNominaForm">
								<span class="glyphicon glyphicon-print"></span> Reporte
							</button>
							<g:link action="importar" class="btn btn-default" params="[periodicidad:periodicidad]" 
								>
								<span class="glyphicon glyphicon-filter"></span> Importar
							</g:link>
							
							
							</div>
						</div>
				<g:render template="nominaGridPanel"/>
			</div>
		</div>
		
		<g:render template="agregarDialog"/>
		<g:render template="reporteDeNomina"/>
	
	<r:script>
			$(function(){
				var table=$("#nominaGrid").dataTable({
			        "paging":   false,
			        "ordering": false,
			        "info":     false,
			         "dom":'t'
    				});
    				
    				$("#folioField").keyup(function(){
      					table.DataTable().column(0).search( $(this).val() ).draw();
					});
					$("#tipoField").keyup(function(){
      					table.DataTable().column(1).search( $(this).val() ).draw();
					});
					
			});
	</r:script>
	
	</div>
	
</body>
</html>
