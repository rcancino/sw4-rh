<%@ page import="com.luxsoft.sw4.rh.EmpleadoCmd" contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta charset="UTF-8">
<meta name="layout" content="empleado/wizard/empleadoWizard" />
<title>Alta de empleado</title>
</head>
<body>
	
	<content tag="heading">
		<h3 class="panel-title">Registro de empleado</h3>
	</content>
	
	<content tag="form">
		<g:form role="form" class="form-horizontal">
			<fieldset>
				
				<f:with bean="${empleado}">
					<f:field property="apellidoPaterno" input-class="form-control" />
					<f:field property="apellidoMaterno" input-class="form-control" />
					<f:field property="nombres" input-class="form-control" />
					<f:field property="curp" input-class="form-control" />
					<f:field property="rfc" input-class="form-control" />
					<f:field property="alta" input-class="form-control" />
					<f:field property="sexo" input-class="form-control" />
					<f:field property="status" input-class="form-control" />
					<f:field property="tipo" input-class="form-control" />
				</f:with>
				
			</fieldset>
				<div class="form-group">
					<div class="col-md-offset-6 col-md-6 ">
						<g:submitButton name="cancelar" value="Cancelar" class="btn btn-default"/>
						<%--<g:link class="btn btn-default" event="cancelar">Cancelar</g:link>
    					--%><g:submitButton name="siguiente" value="Siguiente" class="btn btn-default"/>
    					<g:submitButton name="salvar" value="Salvar" class="btn btn-default"/>
					</div>
				</div>
		</g:form>
	</content>
	
</body>
</html>