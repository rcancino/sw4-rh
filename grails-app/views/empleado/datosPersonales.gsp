<html>
<head>
	<meta charset="UTF-8">
	<r:require module="forms"/>
</head>
<body>

<content tag="contentTitle">
	Datos personales: ${empleadoInstance.nombre} (${empleadoInstance.id})
</content>
<content tag="actions">
	
	<g:if test="${!edit}">
		<g:link class="btn btn-default" action="datosPersonales" id="${empleadoInstance.id}" params="[edit:'true']">Modificar</g:link>
	</g:if>
	
</content>

<content tag="content">
	<form  class="form-horizontal" method="post">
		<g:hiddenField name="id" value="${empleadoInstance.id}"/>
		<g:hiddenField name="version" value="${empleadoInstance?.version}" />
		
		<div class="col-md-6">
		
			<fieldset ${!edit?'disabled=""':''}>
				<f:with bean="empleadoInstance">
					<f:field property="datosPersonales.lugarDeNacimiento" input-class="form-control" />	
					<f:field property="datosPersonales.telefono1" input-class="form-control" />
					<f:field property="datosPersonales.telefono2" input-class="form-control" />
					<f:field property="datosPersonales.email" input-class="form-control" />
					<f:field property="datosPersonales.tipoDeSangre" input-class="form-control" />
					<f:field property="datosPersonales.estadoCivil" input-class="form-control" />
					<f:field property="datosPersonales.conyuge" label="Conyuge" input-class="form-control" />
					
					<f:field property="datosPersonales.nombreDelPader" input-class="form-control" />
					
					<f:field property="datosPersonales.nombreDeLaMadre" input-class="form-control" />
						
				</f:with>
			</fieldset>
		
		</div>
		<div class="col-md-6">
		
			<fieldset ${!edit?'disabled=""':''}>
				<f:with bean="empleadoInstance">
					<f:field property="datosPersonales.direccion" input-class="form-control" />	
					
						
				</f:with>
			</fieldset>
		
		</div>
	
	<g:if test="${edit}">
	<div class="form-group">
    	<div class="col-sm-offset-8 col-sm-12">
      		<g:actionSubmit class="btn btn-primary"  value="Actualizar" action="update"/>
      		<g:link class="btn btn-default" action="salario" id="${empleadoInstance.id}" >Cancelar</g:link>
    	</div>
	</div>
	</g:if>

	

	</form>


</content>
		

</body>
</html>