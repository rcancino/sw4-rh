<html>
<head>
	<meta charset="UTF-8">
	<title>Solicitud vacaciones ${vacacionesInstance.id}</title>
</head>
<body>
	<div class="container">
	
		<div class="row">
			<ul class="nav nav-tabs">
				<li>
					<g:link action="index" class="list-group-item">
						<span class="glyphicon glyphicon-list"></span> Vacaciones
					</g:link>
				</li>
				<li>
					<g:link controller="asistencia" action="index" class="list-group-item">
						<span class="glyphicon glyphicon-list"></span> Control de asistencias
					</g:link>
				</li>
			</ul>
			<div class="col-md-12">
				<g:render template="editForm"/>
			</div>
			
		</div>
	</div>
	
</body>
</html>