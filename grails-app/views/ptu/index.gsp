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
						<h3>Módulo para el calculo del PTU </h3>
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
			
			<div class="col-md-12">
				<div class="button-panel">
					<div class="btn-group">
						<g:if test="${!com.luxsoft.sw4.rh.Ptu.findByEjercicio(session.ejercicio-1)}">
							<g:link action="index" class="btn btn-default">
								<span class="glyphicon glyphicon-plus"></span> Nuevo
							</g:link>
						</g:if>
						
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
			</div>
			
		</div><!-- end .row 2 Toolbar -->
		
		<div class="row">
			<div class="col-md-12">
				<g:render template="grid"/>
			</div>
			
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
				
				
				
			
				
		});
	</r:script>
</body>
</html>