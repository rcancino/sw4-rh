<html>
<head>
<meta charset="UTF-8">
<meta name="layout" content="reportes"/>
<title>Bitácora checado</title>
</head>
<body>

	<content tag="reporteTitle">
		Bitácora de checado
	</content>
	<content tag="reportForm">
		<g:hasErrors bean="${reportCommand}">
            <div class="alert alert-danger">
                <g:renderErrors bean="${reportCommand}" as="list" />
            </div>
        </g:hasErrors>
		<div class="col-sm-6">
		
		<g:form action="bitacoraDeChecado" class="form-horizontal">
			<g:hiddenField name="reportName" value="BitacoraChecado"/>
			<fieldset>
				<legend> Parámetros</legend>
				<f:with bean="${reportCommand}">
					<f:field property="empleado" input-class="form-control"/>
					<f:field property="calendario" input-class="form-control">
						<g:select class="form-control"  
							name="calendario"
							from="${com.luxsoft.sw4.rh.CalendarioDet.findAll().sort({it.calendario.tipo})}" 
							optionKey="id" 
							optionValue="${{it.calendario.tipo+' '+it.folio}}"
							
							/>
					</f:field>
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