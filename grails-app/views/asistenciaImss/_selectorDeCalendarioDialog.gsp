<%@page expressionCodec="none"%>
<div class="modal fade" id="calendarioForm" tabindex="-1">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Calendario</h4>
			</div>
			<g:form action="actualizarCalendario" class="form-horizontal">
				
				<div class="modal-body">
					<div class="form-group">
    					<label for="calendario" class="col-sm-3">Calendario Inicial</label>
    					<div class="col-sm-9">
    						<g:select id="calendario" class="form-control"  
								name="id" 
								value="${calendarioDet.id}"
								from="${periodos}" 
								optionKey="id" 
								optionValue="${{it.calendario.tipo+' '+it.folio+' ( '+it.inicio.format('MMM-dd')+' al '+it.fin.format('MMM-dd')+ ' )'+' (Asis: '+it.asistencia.fechaInicial.format('MMM-dd')+' al '+it.asistencia.fechaFinal.format('MMM-dd')+ ' )'}}"
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
