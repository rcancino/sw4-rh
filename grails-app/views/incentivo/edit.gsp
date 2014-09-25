<html>
<head>
	<meta charset="UTF-8">
	<title>Incentivo ${incentivoInstance.id}</title>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="alert alert-info">
				<h4>${incentivoInstance.empleado}</h4>
				<h5>${incentivoInstance?.fechaInicial?.format('dd/MM/yyyy')} al 
					${incentivoInstance?.fechaFinal?.format('dd/MM/yyyy')}</h5>

			</div>
			<g:hasErrors bean="${incentivoInstance}">
				<div class="alert alert-danger">
					<g:renderErrors bean="${incentivoInstance}" as="list" />
				</div>
			</g:hasErrors>
		</div>
		<div class="row">
			<div class="col-md-3">
				<div class="panel panel-default">
  					<div class="panel-heading">Operaciones</div>
    				<div class="list-group">
    					<g:link action="recalcular" class="list-group-item" id="${incentivoInstance.id}">
    						<span class="glyphicon glyphicon-wrench"></span> Re calcular
    					</g:link>
    				</div>
				</div>
			</div>
			<div class="col-md-6">
				<g:form action="update" id="${incentivoInstance.id}" class="form-horizontal">
				<f:with bean="${incentivoInstance}">
					<f:field property="otorgado" input-class="form-control"/>
					<f:field property="tasaBono1" input-class="form-control" input-type="text"/>
					<f:field property="tasaBono2" input-class="form-control" input-type="text"/>
				</f:with>


				
				<div class="form-group">
					<div class="col-md-offset-2 col-sm-10">
						<g:link class="btn btn-default" action="index" params="[tipo:incentivoInstance.tipo]"> Cancelar</g:link>
						<g:submitButton name="Salvar" class="btn btn-primary " />
					</div>
				</div>
					
				</g:form>
			</div>
		</div>
		
	</div>
	
	<r:script>
		
	</r:script>
	
</body>
</html>