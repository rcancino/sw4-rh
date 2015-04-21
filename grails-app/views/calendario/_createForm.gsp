<%@page expressionCodec="none" %>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">Calendario</h3>
	</div>
	<div class="panel-body">	
	
		<g:form action="${actionForm?:'save' }" role="form" class="form-horizontal" method="${actionMethod?:'POST' }">
			<fieldset>
			<f:with bean="calendarioInstance">
				<g:hiddenField name="id" value="${calendarioInstance?.id}"/>
				<f:field property="ejercicio" input-class="form-control" />
				<f:field property="tipo" input-class="form-control" />
				<f:field property="comentario" input-class="form-control" />
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