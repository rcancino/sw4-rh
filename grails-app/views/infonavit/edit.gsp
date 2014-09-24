<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operacionesForm"/>
	<title>INFONAVIT ${infonavitInstance.id }</title>
	<r:require module="forms"/>
</head>
<body>
	<content tag="header">
		<h3>CréditoINFONAVIT de: ${infonavitInstance.empleado}</h3>
	</content>
	
	<content tag="operaciones">
		<ul class="nav nav-pills nav-stacked">
			<li>
				<g:link action="index" class="list-group-item"> <span class="glyphicon glyphicon-list-alt"></span> Catálogo</g:link>
			</li>
			<li>
				<g:link action="create" class="list-group-item"> <span class="glyphicon glyphicon-floppy-saved"></span> Nuevo</g:link>
			</li>
		</ul>
	</content>
	
	<content tag="formTitle">
		Crédito INFONAVIT
	</content>
	
	<content tag="form">
		<g:form class="form-horizontal numeric-form"  method="post">
		<f:with bean="${infonavitInstance}">
			<g:hiddenField name="id" value="${infonavitInstance?.id}" />
			<g:hiddenField name="version" value="${infonavitInstance?.version}" />
			
			<f:field property="alta" input-class="form-control"/>
			<f:field property="numeroDeCredito" input-class="form-control"/>
			<f:field property="tipo" input-class="form-control"/>
			<f:field property="cuotaFija" input-type="text" input-class="form-control " label="Descuento"/>
			<f:field property="activo" input-type="text" input-class="form-control " />
			<f:field property="comentario" input-class="form-control"/>
		</f:with>
		
		<div class="form-group">
		    <div class="col-sm-offset-2 col-sm-4">
		    	<br/>
		      	<g:actionSubmit class="btn btn-default" action="update" 
		      		value="Actualizar" />
				<g:actionSubmit class="btn btn-danger" action="delete" 
					value="Eliminar" 
					formnovalidate="" 
					onclick="return confirm('¿Eliminar este registro?');" />
		    </div>
		</div>
		
		</g:form>
	</content>
	
</body>
</html>