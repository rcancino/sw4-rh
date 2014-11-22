<%@page expressionCodec="none"%>
<div class="modal fade" id="filterDialog" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Filtrar</h4>
				<span class="label label-info">Puede usar el comodin %</span>
			</div>

			<g:form class="form-horizontal" action="search" >
				<div class="modal-body">
					<%--<div class="form-group">
						<label for="nombreField" class="col-sm-2 control-label">Empleado</label>
						<div class="col-sm-10">
							<g:field id="empleadoField" type="text" placeholder="Empleado" name="empleado" class="form-control"  />
						</div>
					</div>

					--%>
					<f:with bean="${new com.luxsoft.sw4.rh.ModificacionSearch()}">
						
						<f:field property="empleado" input-class="form-control"/>
						
					</f:with>
					<%--<div class="form-group">
						<label for="fechaInicial" class="col-sm-2 control-label">Fecha Inicial</label>
						<div class="col-sm-10">
							<g:field  type="text" name="fechaInicial" class="form-control dateField"  
							value="${(new Date()-60).format('dd/MM/yyyy')}"/>
						</div>
					</div>
					<div class="form-group">
						<label for="fechaFinal" class="col-sm-2 control-label">Fecha Final</label>
						<div class="col-sm-10">
							<g:field  type="text" name="fechaFinal" class="form-control dateField"  
							value="${(new Date()).format('dd/MM/yyyy')}"/>
						</div>
					</div>					
					--%>
				</div>	
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
					<g:submitButton class="btn btn-primary" name="aceptar"
							value="Filtrar" />
				</div>
			</g:form>

		</div>
		<!-- moda-content -->
	</div>
	<!-- modal-di -->

</div>
