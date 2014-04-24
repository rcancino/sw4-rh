<html>
<head>
	<meta charset="UTF-8">
	<r:require module="forms"/>
</head>
<body>

<content tag="contentTitle">
	Contactos: ${empleadoInstance.nombre} (${empleadoInstance.id})
</content>
<content tag="actions">
	
	<g:if test="${!edit}">
		<g:link class="btn btn-default" action="contactos" id="${empleadoInstance.id}" params="[edit:'true']">Modificar</g:link>
	</g:if>
	
</content>

<content tag="content">
	<form  class="form-horizontal numeric-form" method="post">
		<g:hiddenField name="id" value="${empleadoInstance.id}"/>
		<g:hiddenField name="version" value="${empleadoInstance?.version}" />
		
		<div class="col-md-10">
			<fieldset ${!edit?'disabled=""':''}>
				<f:with bean="empleadoInstance">
					<f:field property="contacto.nombre" input-class="form-control" />	
					<f:field property="contacto.parentesco" input-class="form-control" />
					<f:field property="contacto.telefono1" input-class="form-control" />
					<f:field property="contacto.telefono2" input-class="form-control" />
					<f:field property="contacto.direccion" input-class="form-control" />
				</f:with>
			</fieldset>
		</div>
		
		
	
	<g:if test="${edit}">
	<div class="form-group">
    	<div class="col-sm-offset-8 col-sm-12">
      		<g:actionSubmit class="btn btn-primary"  value="Actualizar" action="update"/>
      		<g:link class="btn btn-default" action="contactos" id="${empleadoInstance.id}" >Cancelar</g:link>
    	</div>
	</div>
	</g:if>

	

	</form>
		
</content>
		

</body>
</html>