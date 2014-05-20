<html>
<head>
	<meta charset="UTF-8">
	<title>Alta de incapacidad</title>
</head>
<body>
	<div class="container">
	
		<div class="row">
			<div class="col-md-3">
				<div class="list-group">
					<a href=" link_1" class="list-group-item active">Operaciones</a>
					<g:link action="index" class="list-group-item">
						<span class="glyphicon glyphicon-list"></span> Incapacidades
					</g:link>
					<a href=" link_3" class="list-group-item">Buscar</a>
				</div>
			</div>
			<div class="col-md-9">
				<g:render template="createForm"/>
			</div>
		</div>
	</div>
	
</body>
</html>