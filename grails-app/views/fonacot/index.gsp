<html>
<head>
	<meta charset="UTF-8">
	<title>Prestamos</title>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="page-header">
					<h3>Prestamos FONACTO</h3>
				</div>
			</div>
		</div>

		<div class="row">
			<div class="col-md-12">
				<div class="button-panel">
					<div class="btn-group">
						<input type='text' id="nombreField" placeholder="Empleado" class="form-control">
					</div>
					<div class="btn-group">
						<input type='text' id="ubicacionField" placeholder="Ubicacion" class="form-control" autofocus="autofocus">
					</div>
					
					<div class="btn-group">
						<g:link action="index" class="btn btn-default">
							<span class="glyphicon glyphicon-repeat"></span> Todos
						</g:link>
						<g:link action="create" class="btn btn-default">
							<span class="glyphicon glyphicon-floppy-saved"></span> Nuevo
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
								<g:jasperReport jasper="FonactotGeneral"
											format="PDF" name="Vigentes">
								</g:jasperReport>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div><!-- end .row 2 -->

		<div class="row">
			<div class="col-md-12">
				<g:render template="gridPanel"/>
			</div>
		</div>

	</div>
	
	
	
	
	
</body>
</html>