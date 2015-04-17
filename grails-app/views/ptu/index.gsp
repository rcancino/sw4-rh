<html>
<head>
	<meta charset="UTF-8">
	<title>Calculo de PTU</title>
	<r:require modules="datatables"/> 
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="alert alert-info">
					<a href="" data-toggle="modal" data-target="#cambioGlobalDeEjercicioForm">
						<h3>Módulo para el calculo del PTU (${session.ejercicio})</h3>
					</a>
					
					<g:if test="${flash.message}">
						<div class="message" role="status">
							<strong>${flash.message}</strong>
						</div>
					</g:if>
				</div>
			</div>
		</div><!-- end .row 1 -->
		
		<div class="row">
			
			
			<div class="button-panel">
				<div class="btn-group col-md-4">
					<input type='text' id="nombreField" placeholder="Empleado" class="form-control" autofocus="autofocus" autocomplete="off">
				</div>
				<div class="btn-group">
					<input type='text' id="ubicacionField" placeholder="Ubicacion" class="form-control" autocomplete="off" >
				</div>
				<div class="btn-group">
					<button type="button" name="reportes"
							class="btn btn-default dropdown-toggle" data-toggle="dropdown"
							role="menu">
							Operaciones <span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li>
							<g:link action="generar" id="generar">
								<span class="glyphicon glyphicon-plus"></span> Generar/Actualizar
							</g:link>
						</li>
						<li>
							<g:link action="actualizar" class="danger">
								<span class="glyphicon glyphicon-trash"></span> Eliminar
							</g:link>
						</li>
					</ul>
				</div>
				<div class="btn-group">
					<g:link action="index" class="btn btn-default"><span class="glyphicon glyphicon-refresh"></span> Refrescar</g:link>
					<button type="button" name="reportes"
							class="btn btn-default dropdown-toggle" data-toggle="dropdown"
							role="menu">
							Reportes <span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li>
							<g:link action="reporteGlobal" > PTU General</g:link>
							<g:link action="reporteGlobal" > PTU Individual</g:link>
						</li>
					</ul>
				</div>
				
			</div>
			
			
			
		</div><!-- end .row 2 Toolbar -->
		
		<div class="row">
			<g:render template="grid"/>
			
		</div><!--  end .row 3 Grid -->
	</div>
	
	<r:script>
		$(function(){
			var table=$("#grid").dataTable({
		        "paging":   false,
		        "ordering": false,
		        "info":     false,
		         "dom":'t'
				});
				
				$("#nombreField").keyup(function(){
						table.DataTable().column(0).search( $(this).val() ).draw();
				});
				
				$("#ubicacionField").keyup(function(){
						table.DataTable().column(1).search( $(this).val() ).draw();
				});
				
			$("#generar").on('click',function(event){
				var button=$(this);
				if(confirm('Generar/Actualizar PTU para el ejercicio'+"${session.ejercicio}")){
					button.addClass('disabled');
				}
			});
				
		});
	</r:script>
</body>
</html>