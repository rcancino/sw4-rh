<html>
<head>
<meta charset="UTF-8">
<meta name="layout" content="reportes"/>
<title>Exportacion</title>
</head>
<body>

	<content tag="reporteTitle">
		Generación de layout para Altas y Bajas del IMSS
	</content>
	
	<content tag="reportForm">
		<g:hasErrors bean="${reportCommand}">
            <div class="alert alert-danger">
                <g:renderErrors bean="${reportCommand}" as="list" />
            </div>
        </g:hasErrors>
		<div class="col-sm-12">
		
		<g:form action="generarAltasBajasImss" class="form-horizontal">
			<fieldset>
				<legend> Parámetros</legend>
				<div class="form-group">
					<label for="cantidad" class="col-sm-2 control-label">?</label>
					<div class="col-sm-10">
						<%--<g:select class="form-control"  
							name="id" 
							value="com.luxsoft.sw4.rh.Nomina.last()"
							from="${com.luxsoft.sw4.rh.Nomina.findAll("from Nomina n order by n.lastUpdated desc")}" 
							optionKey="id" 
							noSelection="[null:'Seleccione una nómina']"
							/>
					--%></div>
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