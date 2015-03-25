<html>
<head>
	<meta charset="UTF-8">
</head>
<body>

<content tag="content">
	<div class="well">
		<h3>Baja de empleado: ${empleadoInstance }</h3>
	</div>
	<g:form class="form-horizontal numeric-form" action="registrarBaja">
		<g:hiddenField name="empleado.id" value="${empleadoInstance.id}"/>
		<div class="col-md-6">
		
			<fieldset >
				<f:with bean="bajaInstance">
					<f:field property="fecha" input-class="form-control"/>	
					<f:field property="motivo" input-class="form-control"/>
					<f:field property="causa" input-class="form-control"/>
					<f:field property="comentario" input-class="form-control"/>
				</f:with>
			</fieldset>
			
			<div class="form-group">
		    <div class="col-sm-offset-2 col-sm-4">
		    	<br/>
		      	<button type="submit" class="btn btn-primary">
		      		<span class="glyphicon glyphicon-floppy-save"></span> Salvar
		      	</button>
		      	<g:link class="btn btn-default" action="generales" id="${empleadoInstance.id}" >Cancelar</g:link>
		    </div>
		</div>
		
		</div>
		
		
		
		
	</g:form>
</content>

	
</body>
</html>