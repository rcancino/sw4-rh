<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="empleado"/>
	<r:require module="forms"/>
</head>
<body>

<content tag="contentTitle">
	Pensión alimenticia : ${empleadoInstance.nombre} (${empleadoInstance.id})
</content>
<content tag="actions">
	
	<g:if test="${pensionInstance==null}">
		<g:link class="btn btn-primary" action="create" id="${empleadoInstance.id}" >Registrar</g:link>
	</g:if>
	<g:else>
		<g:link class="btn btn-danger" action="eliminar" id="${pensionInstance.id}" onclick="return confirm('Eliminar la pensión?');">Eliminar</g:link>
	</g:else>
	
</content>

<content tag="content">
	
	<g:if test="${pensionInstance}">
		<div class="col-md-6">
		<g:form  class="form-horizontal numeric-form" action="actualizar">
		
			<f:with bean="${pensionInstance}">
				<g:hiddenField name="id" value="${pensionInstance.id}"/>
				<g:hiddenField name="version" value="${pensionInstance.version}"/>
				<f:field property="porcentaje" input-class="form-control" input-type="text" input-class="number"/>
				<f:field property="neto" input-class="form-control" />
				<f:field property="beneficiario" input-class="form-control" />
				<f:field property="comentario" input-class="form-control" />
				
			</f:with>
			
			<div class="form-group">
    			<div class="col-sm-offset-8 col-sm-12">
      				<g:actionSubmit class="btn btn-primary"  value="Actualizar" />
    			</div>
			</div>
		</g:form>
		</div>
	</g:if>
	
</content>
		

</body>
</html>