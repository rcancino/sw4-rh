<g:form class="form-horizontal" action="update" method="PUT">
<div class="row">
<div class="col-md-6">	
		<g:hiddenField name="id" value="${asistenciaImssDetInstance.id}" />
		<g:hiddenField name="version" value="${asistenciaImssDetInstance.version}" />
	
		<f:with bean="${asistenciaImssDetInstance }">
			
			<fieldset disabled>
				<f:field property="fecha" />
				<f:field property="tipo"  input-class="form-control"/>
				<f:field property="subTipo" input-class="form-control"/>
				
			</fieldset>
			<f:field property="cambio"  input-class="form-control"/>
			
			
		</f:with>
</div>
</div>	
	<div class="form-group">
		<div class="col-md-offset-2 col-sm-4">
			<g:link class="btn btn-default" action="show" controller="asistenciaImss" 
				id="${ asistenciaImssDetInstance.asistenciaImss.id}">Cancelar</g:link>
			<button type="submit" class="btn btn-default">
				<span class="glyphicon glyphicon-floppy-save"></span> Actualizar
			</button>
		</div>
	</div>

</g:form>

