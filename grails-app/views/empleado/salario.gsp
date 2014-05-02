<html>
<head>
	<meta charset="UTF-8">
	<r:require module="forms"/>
</head>
<body>

<content tag="contentTitle">
	Condicines salariales: ${empleadoInstance.nombre} (${empleadoInstance.id})
</content>
<content tag="actions">
	
	<g:if test="${!edit}">
		<g:link class="btn btn-default" action="salario" id="${empleadoInstance.id}" params="[edit:'true']">Modificar</g:link>
	</g:if>
	
</content>

<content tag="content">
	<form  class="form-horizontal numeric-form" method="post">
		<g:hiddenField name="id" value="${empleadoInstance.id}"/>
		<g:hiddenField name="version" value="${empleadoInstance?.version}" />
		<div class="col-md-6">
		
			<fieldset ${!edit?'disabled=""':''}>
				<f:with bean="empleadoInstance">
					
					<f:field property="salario.salarioDiario" input-class="form-control moneda-field" />	
					<f:field property="salario.salarioDiarioIntegrado" input-class="form-control moneda-field" />	
					<f:field property="salario.formaDePago" input-class="form-control " />	
					<f:field property="salario.clabe" input-class="form-control" input-autocomplete="off"/>	
					<f:field property="salario.periodicidad" input-class="form-control" />	
					<f:field property="salario.banco" input-class="form-control" />	
				</f:with>
			</fieldset>
		
		</div>
		<div class="col-md-6">
		
			<fieldset ${!edit?'disabled=""':''}>
				<f:with bean="empleadoInstance">
					
					
				</f:with>
			</fieldset>
		
	</div>
	
	<g:if test="${edit}">
	<div class="form-group">
    	<div class="col-sm-offset-8 col-sm-12">
      		<g:actionSubmit class="btn btn-primary"  value="Actualizar" action="update"/>
      		<g:link class="btn btn-default" action="salario" id="${empleadoInstance.id}" >Cancelar</g:link>
    	</div>
	</div>
	</g:if>

	

	</form>

</content>
		

</body>
</html>