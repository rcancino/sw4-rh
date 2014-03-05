<%@page expressionCodec="none" %>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">Alta de departamento</h3>
	</div>
	<div class="panel-body">	
	
		<g:form action="create" role="form" class="form-horizontal">
			<fieldset>
			<f:with bean="departamentoInstance">
				<f:field property="clave" input-class="form-control" />
				<f:field property="descripcion" input-class="form-control" />
			</f:with>
			</fieldset>
			<div class="form-group">
		    	<div class="col-sm-offset-9 col-sm-2">
		      		<button type="submit" class="btn btn-default">
		      			<span class="glyphicon glyphicon-floppy-save"></span> Guardar
		      		</button>
		    	</div>
		  	</div>
		</g:form>
	</div>
	<div class="panel-footer"></div>
</div>