<%@page expressionCodec="none"%>
<div class="modal fade" id="modificacionForm" tabindex="-1">
	<div class="modal-dialog ">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Modificación en grupo</h4>
			</div>
			<form id="actualizarForm" class="form-horizontal" action="${createLink(action:'actualizarAjax')}">
				<div class="modal-body">
					<g:hiddenField name="tipo" value="${tipo}"/>
					<g:if test="${tipo=='SEMANA'}">
						
					</g:if>

					<div class="form-group">
						<label for="bono1Field" class="col-sm-2 control-label">Bono 1</label>
						<div class="col-sm-10">
							<input type="text" name="tasaBono1" class="form-control porcentaje">
						</div>
					</div>
				</div>	
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
					<g:submitButton class="btn btn-primary" name="aceptar"
							value="Aceptar" />
				</div>
			</form>


		</div>
		<!-- moda-content -->
	</div>
	<!-- modal-di -->
</div>
