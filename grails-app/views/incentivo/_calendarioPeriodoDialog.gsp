<%@page expressionCodec="none"%>
<div class="modal fade" id="calendarioForm" tabindex="-1">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Periodo</h4>
			</div>
			<g:form action="actualizarPeriodo" class="form-horizontal">
				<g:hiddenField name="tipo" value="${tipo}"/>
				<div class="modal-body">
					<div class="form-group">
    					<label for="calendarioIni" class="col-sm-3">Calendario Inicial</label>
    					<div class="col-sm-9">
    						<g:select id="calendarioField" class="form-control"  
								name="calendarioDetId" 
								value="${calendarioDet}"
								from="${periodos}" 
								optionKey="id" 
								optionValue="${{it.calendario.tipo+' '+it.folio+' ( '+it.inicio.format('MMM-dd')+' al '+it.fin.format('MMM-dd')+ ' )'+' (Asis: '+it.asistencia.fechaInicial.format('MMM-dd')+' al '+it.asistencia.fechaFinal.format('MMM-dd')+ ' )'}}"
								/>
								
    					</div>
  					</div>
					<g:if test="${tipo=='SEMANA'}">
						<div class="form-group">
	    					<label for="calendarioIni" class="col-sm-3">Calendario Final</label>
	    					<div class="col-sm-9">
	    						<g:select id="calendarioField" class="form-control"  
									name="calendarioDetId2" 
									value="${calendarioDet2}"
									from="${periodos}" 
									optionKey="id" 
									optionValue="${{it.calendario.tipo+' '+it.folio+' ( '+it.inicio.format('MMM-dd')+' al '+it.fin.format('MMM-dd')+ ' )'}}"
									/>
									
	    					</div>
	  					</div>
					</g:if>
					
  					
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
