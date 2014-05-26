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
					<g:if test="${pageProperty(name:'page.gridPanel')}">
						<g:pageProperty name="page.gridPanel"/>
					</g:if>
					<g:else>
					
					
					<div class="btn-group">
						<g:link action="index" class="btn btn-default">
							<span class="glyphicon glyphicon-repeat"></span> Todos
						</g:link>
						<g:link action="create" class="btn btn-default">
							<span class="glyphicon glyphicon-floppy-saved"></span> Nuevo
						</g:link>
						
						<button class="btn btn-default" data-toggle="modal" data-target="#searchForm">
							<span class="glyphicon glyphicon-search"></span> Buscar
						</button>
						
						<g:link action="index" class="btn btn-default">
							<span class="glyphicon glyphicon-filter"></span> Filtrar
						</g:link>
					</div>
					</g:else>
					<div class="grid-panel">
						<g:pageProperty name="page.grid"/>
					</div>
				</div>
			</div>
			
			
		</div>
		
	<!-- Modal para el alta de percepciones -->
	<div class="modal fade" id="searchForm" tabindex="-1" >
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">Buscar entidad</h4>
				</div>
				
				<g:pageProperty name="page.searchForm"/>
				 
			</div> <!-- moda-content -->
		</div> <!-- modal-dialog -->
	</div> <!-- .modal  -->
		
	</body>
</g:applyLayout>