<r:require module="mask"/>


<g:form class="form-horizontal" action="update">
<div class="row">
<div class="col-md-8">	
		<g:hiddenField name="id" value="${asistenciaDetInstance.id}" />
	
		<f:with bean="${asistenciaDetInstance }">
			<f:field property="entrada1">
				<input class="time " type="text" name="entrada1" value="${g.formatDate(date:asistenciaDetInstance.entrada1,format:'HH:mm') }" >
			</f:field>
			<f:field property="salida1">
				<input class="time" type="text" name="salida1" value="${g.formatDate(date:asistenciaDetInstance.salida1,format:'HH:mm') }">
			</f:field>
			<f:field property="entrada2">
				<input class="time" type="text" name="entrada2" value="${g.formatDate(date:asistenciaDetInstance.entrada2,format:'HH:mm') }">
			</f:field>
			<f:field property="salida2">
				<input class="time" type="text" name="salida2" value="${g.formatDate(date:asistenciaDetInstance.salida2,format:'HH:mm') }">
			</f:field>
			<f:field property="manual"/>
			<f:field property="cancelarIncentivo"/>
			<f:field property="pagarTiempoExtra"/>
		</f:with>
</div>
</div>	
	<div class="form-group">
		<div class="col-md-offset-2 col-sm-4">
			<g:link class="btn btn-default" action="show" controller="asistencia" id="${ asistenciaDetInstance.asistencia.id}">Cancelar</g:link>
			<button type="submit" class="btn btn-default">
				<span class="glyphicon glyphicon-floppy-save"></span> Actualizar
			</button>
		</div>
	</div>

</g:form>

<r:script>
	$(document).ready(function(){
  		$('.time').mask('00:00:00');
	});
</r:script>