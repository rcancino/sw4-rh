<%@page defaultCodec="none" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	
	<title>SDI</title>
	
</head>
<body>
	<content tag="header">
		<h3>Salario diario integrado periodo: ${periodo}</h3>
	</content>
	
	<conente tag="buttonBar">
		
	</conente>
	
	<content tag="content">
		<div class="btn-group">
			<g:link action="" class="btn btn-default">
				<span class="glyphicon glyphicon-search"></span> Buscar
			</g:link>
			<g:link action="" class="btn btn-default">
				<span class="glyphicon glyphicon-filter"></span> Filtrar
			</g:link>
			<g:link action="calcularSalarioDiarioIntegrado" class="btn btn-default" >
				 Calcular
			</g:link>
			<g:link action="aplicarSalarioDiarioIntegrado" class="btn btn-default" target="_blank">
				 Aplicar
			</g:link>
		</div>
		<g:render template="sdiGrid"/>
	</content>
</body>
</html>