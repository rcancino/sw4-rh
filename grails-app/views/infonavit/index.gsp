<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="catalogos"/>
	<title>Infonavit</title>
</head>
<body>
	<content tag="header">
			<h3>Retistro de prestamos del INFONAVIT (${tipo })</h3>
	</content>
	
	<content tag="gridPanel">
		<div class="btn-group">
						
						<g:link action="index" class="btn btn-default ${tipo=='SEMANAL'?'btn-primary':'' }" params="[tipo:'SEMANAL']">
							<span class="glyphicon glyphicon-calendar"></span> Semana
						</g:link>
						
						<g:link action="index" class="btn btn-default ${tipo=='QUINCENAL'?'btn-primary':'' }" params="[tipo:'QUINCENAL']">
							<span class="glyphicon glyphicon-calendar"></span> Quincenta
						</g:link>
						
						<g:link action="create" class="btn btn-default">
							<span class="glyphicon glyphicon-floppy-saved"></span> Nuevo
						</g:link>
						
						
						
						<button class="btn btn-default" data-toggle="modal" data-target="#searchForm">
							<span class="glyphicon glyphicon-search"></span> Buscar
						</button>
					
			</div>
	</content>
	
	<content tag="grid">
		<g:render template="gridPanel"/>
	</content>
</body>
</html>