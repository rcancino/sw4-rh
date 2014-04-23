<html>
<head>
	<meta charset="UTF-8">
	
</head>
<body>

<content tag="actions">
	<g:link action="edit" class="btn btn-default" id="${empleadoInstance.id}">Modificar</g:link>
</content>

<content tag="content">
	<form action="" class="form-horizontal">
	<div class="col-md-6">
		%{-- <form action="" class="form-horizontal"> --}%
			<fieldset disabled="">
				<f:with bean="empleadoInstance">
					<f:field property="apellidoPaterno" input-class="form-control" />	
					<f:field property="apellidoMaterno" input-class="form-control" />	
					<f:field property="nombres" input-class="form-control" />	
					<f:field property="fechaDeNacimiento" input-class="form-control" />	
					<f:field property="sexo" input-class="form-control" />	
				</f:with>
			</fieldset>
		%{-- </form> --}%
	</div>
	<div class="col-md-6">
		%{-- <form class="form-horizontal" action=""> --}%
			<fieldset disabled="">
			<f:with bean="empleadoInstance">
				<f:field property="alta" input-class="form-control" />	
				<f:field property="curp" input-class="form-control" />	
				<f:field property="rfc" input-class="form-control" />	
				<f:field property="clave" input-class="form-control" />	
				<f:field property="status" input-class="form-control" />	
			</f:with>
			</fieldset>
					
		%{-- </form> --}%
	</div>
	</form>
</content>
		
	
</body>
</html>