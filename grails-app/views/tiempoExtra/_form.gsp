<%@page expressionCodec="none" %>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">${tipoDeForma=='edit'?' Mantenimiento ':' Alta' } de tiempo extra</h3>
	</div>
	
	<g:if test="${tiempoExtraInstance?.hasErrors()}">
		<div class="alert alert-danger">
			<ul>
				<g:eachError bean="${tiempoExtraInstance}" var="error">
					<li><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
		</div>
	</g:if>
	<div class="panel-body">
		<g:form action="${action?:'save' }" role="form" class="form-horizontal" method="${method?:'POST' }">
			<fieldset>
			<f:with bean="${tiempoExtraInstance}">
				
				<f:field property="concepto" input-class="form-control" />
				<f:field property="empleado" input-class="form-control" />
				<f:field property="autorizo" input-class="form-control"/>
				<f:field property="periodo" input-class="form-control" >
					<input id="periodoFechaInicial" class="form-control" type="text" value="26/03/2014" name="periodo.fechaInicial">
				</f:field>
				
				
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
<r:script>
	$("#periodo\\.fechaInicial").click(function(){
		console.log("Click ....")
	});
</r:script>