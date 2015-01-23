<html>
<head>
	<meta charset="UTF-8">
	<title>Subsidio al empleo</title>
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
				</div>
			</div>
			<div class="col-md-9">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">Alta de Tarifa Subsidio</h3>
					</div>
					<div class="panel-body">	
					
						<g:form action="save" 
							role="form" class="form-horizontal">
							<fieldset>
							<f:with bean="subsidioEmpleoInstance">
								<f:field property="ejercicio" input-class="form-control" />
								<f:field property="desde" input-class="form-control" 
									input-type="text"/>
								<f:field property="hasta" input-class="form-control"
									input-type="text"/>
								<f:field property="subsidio" input-class="form-control"
									input-type="text"/>
								
								
							</f:with>
							</fieldset>
							<div class="form-group">
						    	<div class="col-sm-offset-9 col-sm-2">
						      		<button type="submit" class="btn btn-default">
						      			<span class="glyphicon glyphicon-floppy-save"></span> Guardar
						      		</button>
						    	</div>
						  	</div>
						</g:form>
					</div>
					<div class="panel-footer"></div>
				</div>
			</div>
		</div>
	</div>
	
</body>
</html>