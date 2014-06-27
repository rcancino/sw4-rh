<%@page expressionCodec="none"%>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-hidden="true">&times;</button>
	<h4 class="modal-title" id="myModalLabel">Percepción</h4>
</div>
<g:form action="agregarConcepto" id="${nominaEmpleadoId}" class="form-horizontal" >
	<div class="modal-body">
		<f:with bean="${nominaPorEmpleadoDetInstance}">
			<f:field property="concepto">
				<g:select class="form-control"  
					name="${property}" 
					value="${value?.id}"
					from="${conceptosList}" 
					optionKey="id" 
					optionValue="${ { it.clave+' '+it.descripcion }}"
					noSelection="[null:'Seleccione una peercepción']"
				/>
			</f:field>
			<f:field property="importeGravado" input-class="form-control" label="Importe gravado"/>
			<f:field property="importeExcento" input-class="form-control" label="Importe excento"/>
			<f:field property="comentario" input-class="form-control"/>
		</f:with>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
		<g:submitButton class="btn btn-primary" name="update" value="Salvar"/>
	</div>
</g:form>