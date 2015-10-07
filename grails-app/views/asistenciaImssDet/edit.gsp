<%@ page import="com.luxsoft.sw4.rh.AsistenciaImssDet" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Asistencia IMSS ${asistenciaImssDetInstance.id}</title>
</head>
<body>
	<div class="container">

		<div class="row">
			<div class="col-md-12">
			
				<div class="panel panel-primary">
					<div class="panel-heading">
						Mantenimiento de asistencia IMSS ${asistenciaImssDetInstance.asistenciaImss.empleado } (${asistenciaImssDetInstance.fecha}))
					</div>
					
					<div class="panel-body">
						<g:hasErrors bean="${asistenciaImssDetInstance}">
							<div class="alert alert-danger alert-dismissable">
								<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
								<ul>
								<g:eachError var="err" bean="${asistenciaImssDetInstance}">
									<li><g:message error="${err}"/></li>
								</g:eachError>
								</ul>
							</div>
						</g:hasErrors>
					</div><!-- end .panel-body -->
					<div>
						<g:render template="form"/>
					</div>
				</div>
			
			</div>
		</div><!-- end .row -->
	
	</div>
</body>
</html>