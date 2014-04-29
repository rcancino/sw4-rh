<html>
<head>
	<meta charset="UTF-8">
	<title>Alta de empleado</title>
</head>
<body>

<content tag="actions">
	
</content>

<content tag="content">
	<form class="form-horizontal" method="post">
		
		<div class="col-md-6">
			<fieldset >
				<f:with bean="${empleadoInstance}">
					<f:field property="apellidoPaterno" input-class="form-control" />	
					<f:field property="apellidoMaterno" input-class="form-control" />	
					<f:field property="nombres" input-class="form-control" />	
					<f:field property="fechaDeNacimiento" input-class="form-control" label="F.Nacimiento"/>	
					<f:field property="sexo" input-class="form-control" />	
				</f:with>
			</fieldset>
		
		</div>
		
		<div class="col-md-6">
		
			<fieldset >
				<f:with bean="${empleadoInstance}">
						
					<f:field property="curp" input-class="form-control" />	
					<f:field property="rfc" input-class="form-control" />	
					<f:field property="clave" input-class="form-control" />	
					
					<f:field property="alta" input-class="form-control" />	
				</f:with>
			</fieldset>
			
			
			
			
			
		</div>
	
	
	<div class="form-group">
    	<div class="col-sm-offset-8 col-sm-12">
      		<g:actionSubmit class="btn btn-primary"  value="Salvar" action="save"/>
      		<g:link class="btn btn-default" action="generales" id="${empleadoInstance.id}" >Cancelar</g:link>
    	</div>
	</div>
	

	

	</form>
</content>

</body>
</html>