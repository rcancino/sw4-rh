<html>
<head>
<meta charset="UTF-8">
<meta name="layout" content="empleado/wizard/empleadoWizard" />
<title>Perfil de puesto</title>
</head>
<body>
	
	<content tag="heading">
		<h3 class="panel-title"><strong>Perfil de puesto para: </strong>${empleado} </h3>
	</content>
	
	<content tag="form">
		<g:hasErrors bean="${perfil}">
			<div class="alert alert-danger alert-dismissable">
				<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
				<ul>
					<g:eachError var="err" bean="${perfil}">
						<li><g:message error="${err}"/></li>
					</g:eachError>
				</ul>
			</div>
		</g:hasErrors>
		<g:form role="form" class="form-horizontal">
			<fieldset>
				<f:with bean="${perfil}">
					<g:hiddenField name="empleado.id" value="${perfil?.empleado }"/>
					<g:hiddenField name="empresa.id" value="${perfil?.empresa }"/>
					<f:field property="numeroDeTrabajador" input-class="form-control" />
					<f:field property="puesto" input-class="form-control" value="${perfil?.puesto }"/>
					<f:field property="departamento" input-class="form-control" value="${perfil?.departamento }" />
					<f:field property="ubicacion"  value="${perfil?.ubicacion }"/>
					
				</f:with>
			</fieldset>
				<div class="form-group">
					<div class="col-md-offset-6 col-md-6 ">
						<g:submitButton name="cancelar" value="Cancelar" class="btn btn-default"/>
						<g:submitButton name="anterior" value="Anterior" class="btn btn-default"/>
    					<g:submitButton name="siguiente" value="Siguiente" class="btn btn-default"/>
    					
					</div>
				</div>
		</g:form>
	</content>
	
</body>
</html>