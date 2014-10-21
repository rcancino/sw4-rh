<html>
<head>
<meta charset="UTF-8">
<meta name="layout" content="reportes"/>
<title>Ausentismo por día</title>
</head>
<body>

	<content tag="reporteTitle">
		Ausentismo Por Día
	</content>
	
	<content tag="reportForm">
		<g:hasErrors bean="${reportCommand}">
            <div class="alert alert-danger">
                <g:renderErrors bean="${reportCommand}" as="list" />
            </div>
        </g:hasErrors>
		<div class="col-sm-8">
		
		<g:form action="ausentismoPorDia" class="form-horizontal">
			<g:hiddenField name="reportName" value="AusentismoPorDia"/>
			<fieldset>
				<legend> Parámetros</legend>
				<f:with bean="${reportCommand}">
					<f:field property="fecha" input-class="form-control"/>
					
				</f:with>
			</fieldset>
			<div class="form-group">
		    	<div class="col-sm-offset-2 col-sm-3">
		      		<button type="submit" class="btn btn-default">
		      			<span class="glyphicon glyphicon-cog"></span> Ejecutar
		      		</button>
		    	</div>
		  	</div>
		</g:form>
		</div>
	</content>
	
</body>

</html>