<fieldset>
<r:require module="forms"/>
<g:form class="form-horizontal numeric-form" action="update">
	<g:hiddenField name="id" value="${prestamoInstance.id}" />
	
	<f:with bean="${prestamoInstance }">
		<f:field property="empleado" input-class="form-control" />
		<f:field property="alta" input-class="form-control" />
		<f:field property="fechaDeAutorizacion" input-class="form-control"
			label="Autorización" />
		<f:field property="autorizo" input-class="form-control" />
		<f:field property="importe" input-class="form-control " input-type="text" />
		<f:field property="tipo" input-class="form-control" />
		<f:field property="tasaDescuento" input-class="form-control numeric-field" input-type="text"/>
		<f:field property="importeFijo" input-class="form-control" disabled='true'/>
		<f:field property="comentario" input-class="form-control" />
	</f:with>
	

	<div class="form-group">
		<div class="col-sm-offset-9 col-sm-2">
			<button type="submit" class="btn btn-default">
				<span class="glyphicon glyphicon-floppy-save"></span> Actualizar
			</button>
		</div>
		<!--<div class="col-sm-offset-9 col-sm-2">
			<button type="submit" class="btn btn-default">
				<span class="glyphicon glyphicon-floppy-save"></span> ActualizarSaldos
			</button>
		</div> -->
	</div>
</g:form>
</fieldset>
<r:script>
	$(function(){

		$("#tipo").on('change',function(e){
			if ($(this).val()==='IMPORTE_FIJO'){
				$("[name='tasaDescuento']").prop('disabled', true).val(0.0);
				$("[name='importeFijo']").prop('disabled', false);
			}else{
				$("[name='tasaDescuento']").prop('disabled', false);
				$("[name='importeFijo']").prop('disabled', true).val(0.0);
			}
			console.log('Tipo de de prestamo: '+$(this).val());
		});

		
	});
</r:script>