<%@page defaultCodec="none" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	
	<title>Empleados</title>
	
</head>
<body>
	<content tag="header">
		<h3>Empleados activos</h3>
	</content>
	
	<conente tag="buttonBar">
	 Actualizar SDI
		
	</conente>
	
	<content tag="content">
		<div class="btn-group">
			<g:link action="index" class="btn btn-default">
				<span class="glyphicon glyphicon-repeat"></span> Refrescar
			</g:link>
			<g:link action="" class="btn btn-default">
				<span class="glyphicon glyphicon-search"></span> Buscar
			</g:link>
			<g:link action="" class="btn btn-default">
				<span class="glyphicon glyphicon-filter"></span> Filtrar
			</g:link>
			
		</div>
		<g:render template="empleadosGrid"/>
	</content>
</body>
</html>