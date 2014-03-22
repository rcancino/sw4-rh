<html>
<head>
<meta charset="UTF-8">
<meta name="layout" content="empleado/wizard/empleadoWizard" />
<title>Alta de empleado</title>
</head>
<body>
	
	<content tag="heading">
		<h3 class="panel-title">Registro de empleado ${empleado}</h3>
	</content>
	
	<content tag="form">
		<g:form role="form" class="form-horizontal">
			<fieldset>
				<f:with bean="${empleado}">
					
				</f:with>
			</fieldset>
				<div class="form-group">
					<div class="col-md-offset-6 col-md-6 ">
						<g:submitButton name="cancelar" value="Cancelar" class="btn btn-default"/>
						<g:submitButton name="anterior" value="Anterior" class="btn btn-default"/>
    					<g:submitButton name="siguiente" value="Siguiente" class="btn btn-default"/>
    					<g:submitButton name="salvar" value="Salvar" class="btn btn-default"/>
					</div>
				</div>
		</g:form>
	</content>
	
</body>
</html>