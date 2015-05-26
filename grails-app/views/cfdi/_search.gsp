<%@page expressionCodec="none"%>
<div class="modal fade" id="searchDialog" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Busqueda avanzada</h4>
				
			</div>

			<g:form class="form-horizontal" action="search" >
				
				<div class="modal-body">

					<div class="form-group">
						<label for="nombre" class="col-sm-3 control-label">Cliente</label>
						<div class="col-sm-9">
							<g:field id="receptor" type="text" placeholder="Cliente" 
								name="receptor" class="form-control"  />
							<span class="help-block">${"Puede usuari el comodín %"}</span>
						</div>
					</div>

					<div class="form-group">
						<label for="fechaInicial" class="col-sm-3 control-label">Fecha Inicial</label>
						<div class="col-sm-9">
							<g:field  type="text" name="fechaInicial" class="form-control dateField"  
							value="${(new Date()-10).format('dd/MM/yyyy')}"/>
						</div>
					</div>
					<div class="form-group">
						<label for="fechaFinal" class="col-sm-3 control-label">Fecha Final</label>
						<div class="col-sm-9">
							<g:field  type="text" name="fechaFinal" class="form-control dateField"  
							value="${(new Date()).format('dd/MM/yyyy')}"/>
						</div>
					</div>

					<div class="form-group">
						<label for="fechaFinal" class="col-sm-3 control-label">Folio</label>
						<div class="col-sm-9">
							<g:field  type="text" name="folio" class="form-control" />
						</div>
					</div>
					
					
				</div>	
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
					<g:submitButton class="btn btn-primary" name="aceptar"
							value="Buscar" />
				</div>
			</g:form>

		</div>
		<!-- moda-content -->
	</div>
	<!-- modal-di -->

</div>

