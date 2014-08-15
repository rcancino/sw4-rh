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
	<form  class="form-horizontal " method="post">
		<g:hiddenField name="id" value="${empleadoInstance.id}"/>
		<g:hiddenField name="version" value="${empleadoInstance?.version}" />
		<g:hiddenField name="view" value="salario" />
		<div class="col-md-6">
		
			<fieldset ${!edit?'disabled=""':''}>
				<f:with bean="empleadoInstance">
					<g:if test="${empleadoInstance?.salario?.salarioDiario}">
						<f:field property="salario.salarioDiario" input-id="salarioNuevo"
							input-class="form-control"  input-readonly="true"/>	
					</g:if>
						
					<g:else>
						<f:field property="salario.salarioDiario" input-id="salarioNuevo"
							input-class="form-control numeric" />	
					</g:else>
					<f:field property="salario.salarioDiarioIntegrado" input-id="sdiNuevo"
						input-class="form-control " input-type="text" 
						input-readonly="true"
						/>	
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
      		<g:actionSubmit class="btn btn-primary"  value="Actualizar" action="updateSalario"/>
      		<g:link class="btn btn-default" action="salario" id="${empleadoInstance.id}" >Cancelar</g:link>
    	</div>
	</div>
	</g:if>

	

	</form>
	
	<r:script>
		$(function(){
			$(".numeric").attr("type",'text');
			$(".numeric").autoNumeric();
			
			$("#salarioNuevo").blur(function(){
				var salario=$(this).val();
				console.log('Calculando SDI para salario:'+salario);
				jQuery.getJSON(
						'<g:createLink controller="empleadoRest" action="calcularSdiNuevo"/>',
						{empleadoId:${empleadoInstance.id},salarioNuevo:salario}
						,function(data){

						}).done(function(data){
							$('#sdiNuevo').val(data.sdi);
						});
			});
			
			
		});
	</r:script>

</content>
		

</body>
</html>