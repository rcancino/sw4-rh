<%@page expressionCodec="none" %>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">${tipoDeForma=='edit'?' Mantenimiento ':' Alta' } de concepto</h3>
	</div>
	
	<g:if test="${conceptoInstance?.hasErrors()}">
	
	<div class="alert alert-danger">
		<ul>
			<g:eachError bean="${conceptoInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
			</g:eachError>
		</ul>
	</div>
	</g:if>
	<div class="panel-body">
		<g:form action="${action?:'save' }" role="form" class="form-horizontal" method="${method?:'POST' }">
			<fieldset>
			<f:with bean="${conceptoInstance}">
				<g:hiddenField name="id" value="${conceptoInstance?.id}"/>
				<f:field property="clave" input-class="form-control" value="${conceptoInstance?.clave }"/>
				<f:field property="descripcion" input-class="form-control" value="${conceptoInstance?.descripcion }"/>
				<f:field property="claveSat" input-class="form-control" value="${conceptoInstance?.claveSat}"/>
				<f:field property="tipo" input-class="form-control" value="${conceptoInstance?.tipo }"/>
				<f:field property="general" input-class="form-control" value="${conceptoInstance?.general }"/>
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