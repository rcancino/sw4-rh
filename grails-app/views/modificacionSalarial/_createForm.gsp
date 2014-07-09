<%@page expressionCodec="none" %>
<r:require modules="jquery-ui,forms"/>

<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">Alta de modificación salarial </h3>
	</div>
	<div class="panel-body">
	
		<g:form action="save" role="form" class="form-horizontal" >
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
							input-id="sdiAnteriorField"/>

					<f:field property="salarioAnterior" input-class="form-control" 
							input-readonly="" 
							input-id="salarioAnteriorField"/>

					<f:field property="salarioNuevo" input-class="form-control numeric" input-type="text"/>
					<f:field property="comentario" input-class="form-control"/>
				</f:with>
			</div>
			
			<div class="form-group">
		    	<div class="col-sm-offset-9 col-sm-2">
		      		<button type="submit" class="btn btn-default">
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
	$(".numeric").autoNumeric({vMin:'0.00',wEmpty:'zero',mRound:'B',mDec:'2'});
});


</r:script>