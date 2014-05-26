<%@page expressionCodec="none"%>

<div class="modal fade" id="autorizacionForm" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Autorización</h4>
			</div>
			<g:form action="${action}" class="form-horizontal" id="${row.id}">
				<div class="modal-body">
					<div class="form-group">
    					<label for="comentarioField" class="col-sm-3">Comentario</label>
    					<div class="col-sm-9">
    						<input type="text" class="form-control " id="comentarioField" name="comentarioAutorizacion">
    					</div>
  					</div>
				</div>
				
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
					<g:submitButton class="btn btn-primary" name="Aceptar"
							value="aceptar" />
				</div>
				
			</g:form>


		</div>
		<!-- moda-content -->
	</div>
	<!-- modal-di -->
</div>
