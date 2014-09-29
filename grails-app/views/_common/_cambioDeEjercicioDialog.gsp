<%@page expressionCodec="none"%>
<div class="modal fade" id="cambioDeEjercicioForm" tabindex="-1">
	<div class="modal-dialog ">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Cambiar ejercicio de operación</h4>
			</div>
			<g:form class="form-horizontal" action="cambiarEjercicio">
				<div class="modal-body">
					<div class="form-group">
						<label for="bono1Field" class="col-sm-2 control-label">Ejercicio</label>
						<div class="col-sm-10">
							<g:select name="ejercicio" class="form-control"
									from="${2014..2019}" value="${session.ejercicio}"
          							/>
						</div>
					</div>
					
				</div>	
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
					<g:submitButton class="btn btn-primary" name="aceptar"
							value="Aceptar" />
				</div>
			</g:form>

		</div>
		<!-- moda-content -->
	</div>
	<!-- modal-di -->
</div>
