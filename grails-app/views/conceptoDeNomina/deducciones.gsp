<html>
<head>
	<meta charset="UTF-8">
	
	<title>Deducciones</title>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="alert alert-info">
					<h3>${"Deducciones"}</h3>
					<g:render template="/_common/alertMessage"/>
				</div>
				<nav:menu scope="app/catalogos/conceptos" class="nav nav-tabs"/> 
				<div class="table-panel">
					<div class="btn-group">
						<g:link action="percepciones" class="btn btn-default">
							<span class="glyphicon glyphicon-repeat"></span> Todos
						</g:link>
						<g:link action="create" class="btn btn-default" params="[tipo:'PERCEPCION']">
							<span class="glyphicon glyphicon-floppy-saved"></span> Nuevo
						</g:link>
						<g:link action="percepciones" class="btn btn-default">
							<span class="glyphicon glyphicon-search"></span> Buscar
						</g:link>
						<g:link action="percepciones" class="btn btn-default">
							<span class="glyphicon glyphicon-filter"></span> Filtrar
						</g:link>
					</div>
					<g:render template="grid"/>
				</div> 
			</div>
		</div>
	</div>
		
	</content>
</body>
</html>