
<%@ page import="com.luxsoft.sw4.rh.Nomina" %>
<!DOCTYPE html>
<html>
	<head>
		<g:set var="entityName" value="${message(code: 'nomina.label', default: 'Nomina')}" />
		<title><g:message code="nomina.list.label" default="Nominas" /></title>
	</head>
	<body>
		<div class="container">
			
			<div class="row">
				<div class="col-md-12">
					<ul class="nav nav-tabs">
						<li class="active"><g:link action="index" params="[tipo:'QUINCENAL']">Quncenas</g:link></li>
						<li><a href="#semanaPanel" data-toggle="tab">Semanales</a></li>
						<li><a href="#especialPanel" data-toggle="tab">Especiales</a></li>
					</ul>
				</div>
			</div>
			
		</div>
	</body>
</html>
