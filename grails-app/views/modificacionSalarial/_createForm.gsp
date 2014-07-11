<%@page expressionCodec="none" %>
<r:require modules="jquery-ui,forms"/>

<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">Alta de modificación salarial </h3>
	</div>
	<div class="panel-body">
	
		<g:form action="save" role="form" class="form-horizontal " >
			<div class="well">
				<f:with bean="${modificacionInstance}">
					<f:field property="empleado" >
						<g:hiddenField id="empleadoId" name="empleado.id"/>
						<g:field id="empleadoField" name="empleadoNombre" class="form-control" type="text" required="" />
					</f:field>
					<f:field property="fecha" input-class="form-control" />
					<f:field property="tipo" input-class="form-control" />

					<f:field property="sdiAnterior" input-class="form-control" 
							input-readonly="" 
							input-id="sdiAnteriorField"
							input-type="text"/>

					<f:field property="salarioAnterior" input-class="form-control" 
							input-readonly="" 
							input-id="salarioAnteriorField"
							input-type="text"/>

					<f:field property="salarioNuevo" 
							input-class="form-control numerico" 
							input-type="text"
							input-autocomplete="off"/>
					
					<f:field property="sdiNuevo">
							<g:field id="sdiNuevoField" class="form-control" 
								name="sdiNuevo" type="text" readonly="" autocomplete="off"/>
					</f:field>

					<f:field property="comentario" input-class="form-control"/>
				</f:with>
			</div>
			
			<div class="form-group">
		    	<div class="col-sm-offset-9 col-sm-2">
		      		<button id="submitBtn" type="submit" class="btn btn-default">
		      			<span class="glyphicon glyphicon-floppy-save"></span> Guardar
		      		</button>
		    	</div>
		  	</div>
		</g:form>
	</div>
	
</div>

<r:script>

$(function(){
	$("#empleadoField").autocomplete({
			source:'<g:createLink controller="empleadoRest" action="getEmpleadosConSalario"/>',
			minLength:3,
			select:function(e,ui){
				console.log('Valor seleccionado: '+ui.item.id);
				$("#empleadoId").val(ui.item.id);
				$("#salarioAnteriorField").val(ui.item.salarioDiario);
				$("#sdiAnteriorField").val(ui.item.sdi);
			}
	});
	$(".numerico").autoNumeric();
	$("#salarioNuevo").blur(function(){
		var sdi=$(this).val();
		var empleadoId=$("#empleadoId").val();
		
		if(empleadoId!==""){
			console.log('Nuevo salario: '+sdi);
			$('#submitBtn').attr('disabled', 'disabled');
			jQuery.getJSON(
				'<g:createLink controller="empleadoRest" action="calcularSdi"/>',
				{empleadoId:empleadoId},function(data){

				}
			).done(function(data){
				$('#sdiNuevoField').val(data.sdi);
				if(data.sdi>0){
					$('#submitBtn').removeAttr("disabled");
				}
			});
		}
		
	});
});


</r:script>