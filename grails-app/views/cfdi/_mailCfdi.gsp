<%@page expressionCodec="none"%>
<div class="modal fade" id="enviarCorreoForm" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Enviar correo electrónico</h4>
			</div>

			<g:form class="form-horizontal" action="mandarCorreo" >
				<g:hiddenField name="cfdi.id" value="${cfdiInstance.id}"/>
				<div class="modal-body">
					<div class="form-group">
						<label for="mailField" class="col-sm-2 control-label">E-mail</label>
						<div class="col-sm-10">
							<g:field id="mailField" 
								type="email" 
								value="${correoDeEnvio}" 
								name="mail" 
								class="form-control"  />
						</div>
					</div>
					
				</div>	
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
					<g:submitButton class="btn btn-primary" name="acepta"
							value="Enviar" />
				</div>
			</g:form>

		</div>
		<!-- moda-content -->
	</div>
	<!-- modal-di -->
</div>
