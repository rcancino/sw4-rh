<html>
<head>
	<meta charset="UTF-8">
	<title>Solicitud vacaciones ${vacacionesInstance.id}</title>
</head>
<body>
	<div class="container">

		
		
			<g:if test="${flash.error}">
					<div class="alert alert-danger">
						<h4 class="text-center">${flash.error}</h4>
					</div>
				</g:if>
		
	
		<div class="row">
		
			<ul class="nav nav-tabs">
				<li>
					<g:link action="index" class="list-group-item" params="[tipo:tipo]">
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