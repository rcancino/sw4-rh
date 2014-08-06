<div class="modal fade" id="detalleDeNominaForm" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Nómina detalle </h4>
			</div>

				<g:jasperReport
							format="PDF" name="Retardo mensual"
         					jasper="Nomina"
         					buttonPosition="bottom"
         					class="form-horizontal">
				
				<div class="modal-body">
						<div class="form-group">
							<label for="inicioPago" class="col-sm-3">Ubicación</label>
							<div class="col-ms-9">
								<g:hiddenField name="NOMINA" value="${nominaInstance.id}"/>
								<g:select name="UBICACION" from="${com.luxsoft.sw4.rh.Ubicacion.list()}" 
									optionKey="clave"
          						noSelection="['%':'TODAS']"/>
							</div>
						</div>
						
					
  					
				</div>
				
				<div class="modal-footer">
					
				</div>
				</g:jasperReport>


		</div>
		<!-- moda-content -->
	</div>
	<!-- modal-di -->
</div>
