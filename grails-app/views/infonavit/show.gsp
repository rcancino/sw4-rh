<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operacionesForm"/>
	<title>INFONAVIT (alta)</title>
	<r:require module="forms"/>
</head>
<body>
	<content tag="header">
		<h3>Crédito INFONAVIT: ${infonavitInstance.empleado}</h3>
	</content>
	
	<content tag="formTitle">
		Crédito INFONAVIT ${infonavitInstance.id}
	</content>
	
	<content tag="operaciones">
		<ul class="nav nav-pills nav-stacked">
			<li>
				<g:link action="index" class="list-group-item"> <span class="glyphicon glyphicon-list-alt"></span> Catálogo</g:link>
			</li>
			<li>
				<g:link action="create" class="list-group-item"> <span class="glyphicon glyphicon-floppy-saved"></span> Nuevo</g:link>
			</li>
			
			<li>
				<g:link action="edit" class="list-group-item" id="${infonavitInstance.id}"> Editar</g:link>
			</li>
			
			<li>
				<g:link action="delete" id="${infonavitInstance.id}" 
					class="list-group-item"
					onclick="return confirm('Eliminar prestamo?')"> 
					<span class="glyphicon glyphicon-trash"></span> Eliminar
				</g:link>
			</li>
			
			
		</ul>
		
	</content>
	
	<content tag="form">
		<g:form class="form-horizontal numeric-form" action="save">
		<fieldset disabled="disabled">
		<f:with bean="${infonavitInstance}">
			<f:field property="empleado" input-class="form-control"/>
			<f:field property="alta" input-class="form-control"/>
			<f:field property="numeroDeCredito" input-class="form-control"/>
			<f:field property="tipo" input-class="form-control"/>
			<f:field property="cuotaFija" input-type="text" input-class="form-control " label="Descuento"/>
			<f:field property="cuotaDiaria" input-type="text" input-class="form-control " />
			<f:field property="comentario" input-class="form-control"/>
		</f:with>
		</fieldset>
		<div class="form-group">
		    
		</div>
		
		</g:form>
	</content>
	
</body>
</html>