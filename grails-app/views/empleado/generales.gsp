<html>
<head>
	<meta charset="UTF-8">
	
</head>
<body>

<content tag="actions">
	
	<g:if test="${!edit}">
		%{-- <g:link action="update" class="btn btn-primary" id="${empleadoInstance.id}">Salvar</g:link> --}%
		<g:link class="btn btn-default" action="generales" id="${empleadoInstance.id}" params="[edit:'true']">Modificar</g:link>
	</g:if>
	
</content>

<content tag="content">
	<form class="form-horizontal" method="post">
		<g:hiddenField name="id" value="${empleadoInstance.id}"/>
		<g:hiddenField name="version" value="${empleadoInstance?.version}" />
		
		<div class="col-md-6">
		
			<fieldset ${!edit?'disabled':''}>
				<f:with bean="empleadoInstance">
					<f:field property="apellidoPaterno" input-class="form-control" />	
					<f:field property="apellidoMaterno" input-class="form-control" />	
					<f:field property="nombres" input-class="form-control" />	
					<f:field property="fechaDeNacimiento" input-class="form-control" />	
					<f:field property="sexo" input-class="form-control" />	
				</f:with>
			</fieldset>
		
		</div>
		
		<div class="col-md-6">
		
			<fieldset ${!edit?'disabled=""':''}>
				<f:with bean="empleadoInstance">
						
					<f:field property="curp" input-class="form-control" />	
					<f:field property="rfc" input-class="form-control" />	
					<f:field property="clave" input-class="form-control" />	
					<f:field property="status" input-class="form-control" />
					<f:field property="alta" input-class="form-control" />	
				</f:with>
			</fieldset>
			
			
			
			<fieldset ${!edit?'disabled=""':''}>
				
				<f:with bean="${bajaInstance}">
					<f:field property="fecha" input-class="form-control" />	
					<f:field property="motivo" input-class="form-control" />
					<f:field property="causa" input-class="form-control" />
					<f:field property="comentario" input-class="form-control" />	
				</f:with>
			</fieldset>
			
		</div>
	
	<g:if test="${edit}">
	<div class="form-group">
    	<div class="col-sm-offset-8 col-sm-12">
      		<g:actionSubmit class="btn btn-primary"  value="Actualizar" action="update"/>
      		<g:link class="btn btn-default" action="generales" id="${empleadoInstance.id}" >Cancelar</g:link>
    	</div>
	</div>
	</g:if>

	

	</form>
</content>
		
	
</body>
</html>