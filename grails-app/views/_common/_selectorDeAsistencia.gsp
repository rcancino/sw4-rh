<%@page expressionCodec="none"%>
<div class="modal fade" id="calendarioDeAsistenciaForm" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Periodo de asistencia</h4>
			</div>
			<g:form action="${action?:'cambiarCalendario'}" class="form-horizontal">
				
				<div class="modal-body">
					<div class="form-group">
    					<label for="calendarioIni" class="col-sm-3">Calendario</label>
    					<div class="col-sm-9">
    						<g:select id="calendarioField" class="form-control"  
								name="calendarioDetId" 
								value="${calendarioDet}"
								from="${periodos}" 
								optionKey="id" 
								optionValue="${{it.calendario.tipo+' '+it.folio+' ( '+it.asistencia+ ' )'}}"
								/>
    					</div>
  					</div>
  					
				</div>
				
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
					<g:submitButton class="btn btn-primary" name="aceptar"
							value="Aceptar" />
				</div>
				
			</g:form>


		</div>
		<!-- moda-content -->
	</div>
	<!-- modal-di -->
</div>
