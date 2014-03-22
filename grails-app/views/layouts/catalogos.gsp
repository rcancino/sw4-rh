<g:applyLayout name="application">
	<html>
	<head>
		<title><g:layoutTitle/></title>
		<g:layoutHead/>
	</head>
	</html>
	<body>
		<div class="container">
			<div class="row">
				<div class="col-md-12">
					
					<div class="alert alert-info">
						<g:pageProperty name="page.header"/>
						<g:if test="${flash.message}">
							<div class="message" role="status">
								<strong>${flash.message}</strong>
							</div>
						</g:if>
					</div>
					
					<div class="btn-group">
						<g:link action="index" class="btn btn-default">
							<span class="glyphicon glyphicon-repeat"></span> Todos
						</g:link>
						<g:link action="create" class="btn btn-default">
							<span class="glyphicon glyphicon-floppy-saved"></span> Nuevo
						</g:link>
						<g:link action="create" class="btn btn-default">
							<span class="glyphicon glyphicon-search"></span> Buscar
						</g:link>
						<g:link action="create" class="btn btn-default">
							<span class="glyphicon glyphicon-filter"></span> Filtrar
						</g:link>
					</div>
					<div class="grid-panel">
						<g:pageProperty name="page.grid"/>
					</div>
				</div>
			</div>

			
			
		</div>
	</body>
</g:applyLayout>