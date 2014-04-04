
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="dashboard_1"/>
	<title>Nomina</title>
</head>
<body>
	<content tag="header">
		<g:link controller="nomina" action="show" id="${nominaInstance.id}">
			<h4>Nómina : ${nominaInstance} </h4>
		</g:link>
	</content>
	<content tag="buttonBar">
		
		
	</content>
	<content tag="grid">
		<div class="panel panel-default">
			<div class="panel-heading">
				Seleccione el nombre y ubicación: ${nominaEmpleado?.empleado?.nombre} 
			</div>
			<div class="panel-body">
				<g:hasErrors bean="${nominaEmpleado}">
            		<div class="alert alert-danger">
                		<g:renderErrors bean="${nominaEmpleado}" as="list" />
            		</div>
        		</g:hasErrors>
				
				<g:form class="form-horizontal " >
					
					<div class="form-group ${hasErrors(bean:nominaEmpleado,field:'empleado','has-error has-feedback') }">
						<label for="empleado" class="col-sm-2 control-label">Empleado</label>
						<div class="col-sm-10">
							<g:select class="form-control" 
								id="empleado" 
								name="empleado" 
								value="${nominaEmpleado?.empleado?.id}"
								from="${empleadosList}" 
								optionKey="id" 
								optionValue="nombre"
								noSelection="['':'Seleccione un empleado']"
								/>
							<g:hasErrors bean="${nominaEmpleado}" property="empleado">
								<span class="glyphicon glyphicon-remove form-control-feedback"></span>
							</g:hasErrors>
						</div>
					</div>
					
					<div class="form-group ${hasErrors(bean:nominaEmpleado,field:'empleado','has-error has-feedback') }">
						<label for="ubicacion" class="col-sm-2 control-label">Ubicación</label>
						
						<div class="col-sm-10">
							<g:select class="form-control"  
								id="ubicacion"
								name="ubicacion" 
								value="${nominaEmpleado?.ubicacion?.id}"
								from="${com.luxsoft.sw4.rh.Ubicacion.findAll()}" 
								optionKey="id" 
								optionValue="descripcion"
								noSelection="['':'Seleccione una ubicacion']"
								/>
							<g:hasErrors bean="${nominaEmpleado}" property="ubicacion">
								<span class="glyphicon glyphicon-remove form-control-feedback"></span>
							</g:hasErrors>
						</div>
					</div>
					<%-- 
					<div class="from-group ${hasErrors(bean:nominaEmpleado,field:'ubicacion','has-error has-feedback') } ">
						<label for="ubicacion" class="col-sm-2 control-label">Ubicación</label>
						
					</div>	
					--%>
					<g:submitButton name="cancelar" value="Cancelar" />
                    <g:submitButton name="siguiente" value="Siguiente" />
					
				

				</g:form>
			</div>
		</div>
	</content>

	

</body>
</html>
