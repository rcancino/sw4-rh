<html>
<head>
	<meta charset="UTF-8">
	<title>Turno ${turnoInstance.id}</title>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-3">
				<div class="list-group">
					<a href=" link_1" class="list-group-item active">Operaciones</a>
					<g:link action="index" class="list-group-item">
						<span class="glyphicon glyphicon-list"></span> Catálogo
					</g:link>
					<g:link action="edit" class="list-group-item"
						id="${turnoInstance.id}">
						<span class="glyphicon glyphicon-pencil"></span> Editar
					</g:link>
					<g:link action="delete" class="list-group-item" onclick="return confirm('Eliminar turno ?');" 
						id="${turnoInstance.id}">
						<span class="glyphicon glyphicon-trash"></span> Eliminar
					</g:link>
				</div>
			</div>
			<div class="col-md-9">
				<g:render template="showForm"/>
			</div>
		</div>
	</div>
	
</body>
</html>