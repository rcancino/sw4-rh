<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="catalogos"/>
	<title>Modificaciones salariales</title>
</head>
<body>
	<content tag="header">
		<h3>Modificación salarial Bimestre: ${session.bimestre } (${session.ejercicio })</h3>
		
	</content>
	<content tag="grid">
		<g:render template="grid"/>
	</content>
	
	<content tag="gridPanel">
		<div class="btn-group">
						<g:link action="index" class="btn btn-default">
							<span class="glyphicon glyphicon-repeat"></span> Todos
						</g:link>
						<g:link action="create" class="btn btn-default">
							<span class="glyphicon glyphicon-floppy-saved"></span> Nuevo
						</g:link>
						
						<button class="btn btn-default" data-toggle="modal" data-target="#filterDialog">
							<span class="glyphicon glyphicon-search"></span> Buscar
						</button>
						<a href="#cambioDeBimestreDialog" class="btn btn-default" data-toggle="modal">
							<span class="glyphicon glyphicon-calendar"></span> Cambiar bimestre
						</a>
						
						<g:render template="filter"/>
						<g:render template="cambioDeBimestreDialog"/>
					</div>
	</content>
	
</body>
</html>