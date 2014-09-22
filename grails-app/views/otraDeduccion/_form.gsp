<fieldset>
<r:require module="forms"/>
<g:form class="form-horizontal numeric-form" action="update">
	<g:hiddenField name="id" value="${otraDeduccionInstance.id}" />
	<g:hiddenField name="version" value="${otraDeduccionInstance.version}" />
	<f:with bean="${otraDeduccionInstance }">
		<f:field property="tipo" input-class="form-control" />
		<f:field property="importe" input-class="form-control " input-type="text" />
		<f:field property="totalAbonos" input-class="form-control " input-type="text" input-disabled="disabled"/>
		<f:field property="saldo" input-class="form-control " input-type="text" input-disabled="disabled"/>
		<f:field property="comentario" input-class="form-control" />
	</f:with>
	<div class="form-group">
		<div class="col-sm-offset-9 col-sm-2">
			<button type="submit" class="btn btn-default">
				<span class="glyphicon glyphicon-floppy-save"></span> Actualizar
			</button>
		</div>
	</div>
</g:form>
</fieldset>
