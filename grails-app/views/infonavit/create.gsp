<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operacionesForm"/>
	<title>INFONAVIT (alta)</title>
	<r:require module="forms"/>
</head>
<body>
	<content tag="header">
		<h3>Alta de crédito INFONAVIT</h3>
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
		<g:form class="form-horizontal numeric-form" action="save">
		<f:with bean="${infonavitInstance}">
			<f:field property="empleado" input-class="form-control"/>
			<f:field property="alta" input-class="form-control"/>
			<f:field property="numeroDeCredito" input-class="form-control"/>
			<f:field property="tipo" input-class="form-control"/>
			<f:field property="cuotaFija" input-type="text" input-class="form-control" label="Descuento"/>
			<f:field property="comentario" input-class="form-control"/>
		</f:with>
		
		<div class="form-group">
		    <div class="col-sm-offset-2 col-sm-4">
		    	<br/>
		      	<button type="submit" class="btn btn-primary">
		      		<span class="glyphicon glyphicon-floppy-save"></span> Salvar
		      	</button>
		      	<g:link action="index"  class="btn btn-default"> Cancelar</g:link>
		    </div>
		</div>
		
		</g:form>
	</content>
	
</body>
</html>