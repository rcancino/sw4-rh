<html>
<head>
<meta charset="UTF-8">
<meta name="layout" content="reportes"/>
<title>Incrementos individuales</title>
</head>
<body>

	<content tag="reporteTitle">
		Incementos de salario individuales
	</content>
	<content tag="reportForm">
		<g:hasErrors bean="${reportCommand}">
            <div class="alert alert-danger">
                <g:renderErrors bean="${reportCommand}" as="list" />
            </div>
        </g:hasErrors>
		<div class="col-sm-6">
		
		<g:form action="incrementosIndividuales" class="form-horizontal">
			<g:hiddenField name="reportName" value="IncrementosIndividuales"/>
			<fieldset>
				<legend> Parámetros</legend>
				<div class="form-group">
					<label class="col-sm-2 control-label">Nómina</label>
					<g:select class="form-control"  
						name="id"
						from="${com.luxsoft.sw4.rh.Nomina.findAll().sort{it.periodicidad}}" 
						optionKey="id" 
						optionValue="${{it.ejercicio+' '+it.periodicidad+' '+it.folio}}"
							
					/>
				</div>
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