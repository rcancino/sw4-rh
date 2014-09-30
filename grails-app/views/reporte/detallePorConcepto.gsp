<html>
<head>
<meta charset="UTF-8">
<meta name="layout" content="reportes"/>
<title>Detalle por concepto</title>
</head>
<body>

	<content tag="reporteTitle">
		Detalle por concepto de nómina
	</content>
	<content tag="reportForm">
		<g:hasErrors bean="${reportCommand}">
            <div class="alert alert-danger">
                <g:renderErrors bean="${reportCommand}" as="list" />
            </div>
        </g:hasErrors>
		<div class="col-sm-6">
		
		<g:form action="detallePorConcepto" class="form-horizontal">
			<g:hiddenField name="reportName" value="DetallePorConcepto"/>
			<fieldset>
				<legend> Parámetros</legend>
				<div class="form-group">
					<label class="col-sm-2 control-label">Nómina</label>
					<g:select class="form-control"  
						name="nomina"
						from="${com.luxsoft.sw4.rh.Nomina.findAll().sort{it.periodicidad}}" 
						optionKey="id" 
						optionValue="${{it.ejercicio+' '+it.periodicidad+' '+it.folio}}"
							
					/>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">Concepto</label>
					<g:select class="form-control"  
						name="concepto"
						from="${com.luxsoft.sw4.rh.ConceptoDeNomina.findAll().sort{it.tipo}}" 
						optionKey="id"
							
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