<%@page expressionCodec="none"%>
<div class="modal fade" id="calcularIncentivoMensualForm" tabindex="-1">
	<div class="modal-dialog ">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Cálculo incentivo mensual</h4>
			</div>
			<g:form class="form-horizontal" action="actualizarIncentivoMensual">
				<div class="modal-body">
					<g:hiddenField name="mes" value="${mes}"/>
					<g:hiddenField name="ejercicio" value="${ejercicio}"/>
					<g:hiddenField name="tipo" value="MENSUAL"/>
					
					
					<div class="form-group">
						<label for="bono1Field" class="col-sm-2 control-label">Ubicación</label>
						<div class="col-sm-10">
							<g:select class="form-control"  
									name="ubicacion"
									from="${com.luxsoft.sw4.rh.Ubicacion.findAll()}" 
									optionKey="id" optionValue="descripcion"
									noSelection="['':'Seleccione una ubicacion']"/>
						</div>
					</div>

					<div class="form-group">
						<label for="bono1Field" class="col-sm-2 control-label">Bono 1</label>
						<div class="col-sm-10">
							<input type="text" name="tasaBono1" class="form-control porcentaje">
						</div>
					</div>
					<div class="form-group">
						<label for="bono1Field" class="col-sm-2 control-label">Comentario</label>
						<div class="col-sm-10">
							<input type="text" name="comentario" class="form-control ">
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
