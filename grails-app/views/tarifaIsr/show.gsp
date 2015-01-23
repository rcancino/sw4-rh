<html>
<head>
	<meta charset="UTF-8">
	<title>Tarifa ISR</title>
</head>
<body>
	<div class="container">
		

		<div class="row">
			<div class="col-md-3">
				<div class="list-group">
					<a href=" link_1" class="list-group-item active">Operaciones</a>
					<g:link action="index" class="list-group-item">
						<span class="glyphicon glyphicon-list"></span> Tarifas
					</g:link>
					<g:link action="index" class="list-group-item">
						<span class="glyphicon glyphicon-plus"></span> Nueva
					</g:link>
					<g:link action="edit" class="list-group-item" id="${tarifaIsrInstance.id}">
						<span class="glyphicon glyphicon-pencil"></span> Editar
					</g:link>
					<g:link action="delete" class="list-group-item" id="${tarifaIsrInstance.id}"
						onclick="return confirm('Eliminar la tarifa?');">
						<span class="glyphicon glyphicon-trash"></span> Eliminar
					</g:link>
				</div>
			</div>
			<div class="col-md-9">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">Tarifa ISR</h3>
					</div>
					<div class="panel-body">	
					
						<g:form action="null" 
							role="form" class="form-horizontal">
							<fieldset disabled>
							<f:with bean="tarifaIsrInstance">
								<f:field property="ejercicio" input-class="form-control" />
								<f:field property="tipo" input-class="form-control" />
								
								<f:field property="limiteInferior" input-class="form-control" 
									input-type="text"/>
								<f:field property="limiteSuperior" input-class="form-control"
									input-type="text"/>
								<f:field property="cuotaFija" input-class="form-control"
									input-type="text"/>
								<f:field property="porcentaje" input-class="form-control"
									input-type="text"/>

								
							</f:with>
							</fieldset>
							
						</g:form>
					</div>
					<div class="panel-footer"></div>
				</div>
			</div>
		</div>
	</div>
	
</body>
</html>