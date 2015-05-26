<form  class="form-horizontal">
	

	<fieldset>
		<div class="form-group">
			<label for="socio" class="col-sm-2 control-label">Serie</label>
			<div class="col-sm-4">
				<p class="form-control-static"><g:fieldValue bean="${cfdiInstance}" field="serie"/> </p>
			</div>
		</div>
		
		<div class="form-group">
			<label for="socio" class="col-sm-2 control-label">Folio</label>
			<div class="col-sm-4">
				<p class="form-control-static"><g:fieldValue bean="${cfdiInstance}" field="folio"/> </p>
			</div>
		</div>
		
		<div class="form-group">
			<label for="socio" class="col-sm-2 control-label">UUID</label>
			<div class="col-sm-4">
				<p class="form-control-static"><g:fieldValue bean="${cfdiInstance}" field="uuid"/> </p>
			</div>
		</div>
		
		<div class="form-group">
			<label for="socio" class="col-sm-2 control-label">Timbrado</label>
				<p class="form-control-static"><g:formatDate date="${cfdiInstance?.timbrado}" format="dd/MM/yyyy hh:mm"/> </p>
			<div class="col-sm-4">
			</div>
		</div>
		<%--
		<div class="form-group">
			<label for="socio" class="col-sm-2 control-label">Tipo</label>
			<div class="col-sm-4">
				<p class="form-control-static"><g:fieldValue bean="${cfdiInstance}" field="tipo"/> </p>
			</div>
		</div>
		--%>
		<div class="form-group">
			<label for="socio" class="col-sm-2 control-label">Fecha</label>
			<div class="col-sm-4">
				<p class="form-control-static"><g:formatDate date="${cfdiInstance?.fecha}" format="dd/MM/yyyy"/> </p>
			</div>
		</div>
		<div class="form-group">
			<label for="socio" class="col-sm-2 control-label">Receptor</label>
			<div class="col-sm-4">
				<p class="form-control-static"><g:fieldValue bean="${cfdiInstance}" field="receptor"/> </p>
			</div>
		</div>
		
		<div class="form-group">
			<label for="socio" class="col-sm-2 control-label">RFC</label>
			<div class="col-sm-4">
				<p class="form-control-static"><g:fieldValue bean="${cfdiInstance}" field="receptorRfc"/> </p>
			</div>
		</div>
				
	</fieldset>


</form>

