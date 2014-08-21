<%@ page import="com.luxsoft.sw4.rh.Incentivo" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operaciones2"/>
	<title>Incentivos</title>
	<r:require modules="datatables,forms"/> 
</head>
<body>
	<content tag="header">
		<h3>Control de incentivos ${tipo}</h3>
	</content>
	
	<content tag="consultas">
		<div class="list-group">
 			<g:link action="semanal" class="list-group-item" >
					Semanal
			</g:link>
			<g:link action="quincenal" class="list-group-item" >
					Quincenal
			</g:link>
  			<g:link action="mensual" class="list-group-item" >
					Mensual
			</g:link>
			
		</div>
	</content>
	
	<content tag="gridTitle">
		<a href="" data-toggle="modal" data-target="#calendarioSemanalForm">
			${calendarioDet.calendario.tipo}  ${calendarioDet.folio } (${ejercicio })
		</a>
	</content>
	
	<content tag="toolbarPanel">
		
		<div class="row button-panel">
			
			<div class="col-md-4 ">
				<input id="searchField" class="form-control" type="text" placeholder="Empleado" autofocus="autofocus">
			</div>

			<div class="col-md-8">
				<div class="btn-group">
					<g:link action="semanal" 
							class="btn btn-default">
							<span class="glyphicon glyphicon-refresh"></span> Refresacar
					</g:link>
					<g:link action="generarIncentivoSemanal" 
							class="btn btn-default" 
							onclick="return confirm('Generar/Actualizar incentivos semana: '+' ${calendarioDet?.folio}');" 
							id="${calendarioDet.id}">
							<span class="glyphicon glyphicon-cog"></span> Generar
					</g:link>
				</div>
				<div class="btn-group">
					<button type="button" name="reportes"
						class="btn btn-default dropdown-toggle" data-toggle="dropdown"
						role="menu">
						Reportes <span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li>
							<a href="#">Pendiente</a>
						</li>
					</ul>
				</div><%-- end .btn-group reportes --%>
			</div><%-- end .col-md button panel --%>
			
		</div>	
  		
  		
	</content><!-- end .gridTask -->
	
	<content tag="panelBody">
		<g:render template="gridPanel" />
		
		
		<g:render template="calendarioSemanalDialog"/>
		<r:script>
			$(function(){
				var table=$(".incentivoGrid").dataTable({
			        "paging":   false,
			        "ordering": false,
			        "info":     false,
			        "dom":'t'
    				});
    				
    				$("#searchField").keyup(function(){
      					table.DataTable().search( $(this).val() ).draw();
					});
			});
		</r:script>
	</content>

</body>
</html>