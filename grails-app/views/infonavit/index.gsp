<html>
<head>
	<meta charset="UTF-8">
	<title>Infonavit</title>
	<r:require modules="datatables"/> 
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="alert alert-info">
					<h3>Retistro de prestamos del INFONAVIT </h3>
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
					<input type='text' id="nombreField" placeholder="Empleado" class="form-control">
				</div>
				<div class="btn-group">
					<input type='text' id="ubicacionField" placeholder="Ubicacion" class="form-control" autofocus="autofocus">
				</div>
				<div class="btn-group">
					<button type="button" name="reportes"
							class="btn btn-default dropdown-toggle" data-toggle="dropdown"
							role="menu">
							Reportes <span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li>
							<g:jasperReport jasper="InfonavitGeneralVigentes"
										format="PDF" name="Vigentes">
							</g:jasperReport>
						</li>
					</ul>
				</div>
				<div class="btn-group">
					<button type="button" name="reportes"
							class="btn btn-default dropdown-toggle" data-toggle="dropdown"
							role="menu">
							Operaciones <span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li>
							<g:link action="calcularBimestre" 
								onclick="return confirm('Calcular cuota para  bimestre actual?')">
								<span class="glyphicon glyphicon-cog"></span> Re calcular
							</g:link>
						</li>
						<li>
							<g:link action="create">
								<span class="glyphicon glyphicon-floppy-saved"></span> Nuevo
							</g:link>
						</li>
					</ul>
				</div>
			</div>
			
			
			
		</div><!-- end .row 2 Toolbar -->
		
		<div class="row">
			<div class="col-md-12">
				<g:render template="gridPanel"/>
			</div>
			
		</div><!--  end .row 3 Grid -->
	</div>
	
	<r:script>
			$(function(){
				var table=$("#infonavitGrid").dataTable({
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
			});
	</r:script>
</body>
</html>