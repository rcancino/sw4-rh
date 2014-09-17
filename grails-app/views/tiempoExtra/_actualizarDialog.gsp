<%@page expressionCodec="none"%>
<div class="modal fade" id="actualizarDialog" tabindex="-1">
	<div class="modal-dialog ">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Actualizar control de tiempo extra</h4>
			</div>
			<g:form class="form-horizontal" action="actualizar">
				<div class="modal-body">
					
					<div class="form-group">
						<label for="bono1Field" class="col-sm-2 control-label">Ejercicio</label>
						<div class="col-sm-10">
							<g:select class="form-control"  
									name="ejercicio"
									from="${2014..2020}"
									value="${session.ejercicio}"
									/>
						</div>
					</div>
					
					<div class="form-group">
						<label for="bono1Field" class="col-sm-2 control-label">Tipo</label>
						<div class="col-sm-10">
							<g:select class="form-control"  
									name="tipo"
									from="${['SEMANA','QUINCENA']}"
									/>
						</div>
					</div>

					<div class="form-group">
						<label for="bono1Field" class="col-sm-2 control-label">Número</label>
						<div class="col-sm-10">
							<input type="text" name="numero" class="form-control">
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
