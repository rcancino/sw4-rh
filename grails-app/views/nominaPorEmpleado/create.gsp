<%@ page import="com.luxsoft.sw4.rh.NominaPorEmpleado" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="dashboard_1"/>
	<title>Nomina</title>
</head>
<body>
	<content tag="header">
		<g:link controller="nomina" action="show" id="${command.nomina.id}">
			<h4>Nómina: ${command.nomina.folio} ${command.nomina.periodicidad} </h4>
		</g:link>
	</content>
	
	<content tag="buttonBar">
	</content>
	
	<content tag="grid">
		<div class="panel panel-default">
			<div class="panel-heading">Agregar empleado </div>
			<div class="panel-body">
				<g:form action="agregar" class="form-horizontal"  >
					<g:hiddenField name="nomina.id" value="${command.nomina.id}"/>
					<formset>
						<f:with bean="command">
							<f:field property="empleado"/>
						</f:with>
					</formset>
					<div class="form-group">
		    			<div class="col-sm-offset-2 col-sm-4">
		    				<br/>
		      				<button type="submit" class="btn btn-primary">
		      					<span class="glyphicon glyphicon-floppy-save"></span> Agregar
		      				</button>
		      				<g:link action="index"  class="btn btn-default"> Cancelar</g:link>
		    			</div>
					</div>
				</g:form>
			</div>
		</div>
	</content>

	

</body>
</html>
