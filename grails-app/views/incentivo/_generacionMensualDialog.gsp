<%@page expressionCodec="none"%>
<div class="modal fade" id="generarIncentivoForm" tabindex="-1">
	<div class="modal-dialog ">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Meses</h4>
			</div>
			<g:form action="generarIncencivoMensual" class="form-horizontal">
				<g:hiddenField name="mes" value="${mes}"/>
				<div class="modal-body">
					<div class="form-group">
    					<label for="calendarioIni" class="col-sm-2">Mes</label>
    					<div class="col-sm-10">
    						<p class="form-control-static">${mes}</p>
    					</div>
  					</div>
					<%--<div class="form-group">
    					<label for="calendarioIni" class="col-sm-2">Calendario</label>
    					<div class="col-sm-10">
    						<g:select id="calendarioField" class="form-control"  
								name="calendarioDetId" 
								value="${calendarioDet}"
								from="${periodos}" 
								optionKey="id" 
								optionValue="${{it.calendario.tipo+' '+it.folio+' ( '+it.inicio.format('MMM-dd')+' al '+it.fin.format('MMM-dd')+ ' )'+' (Asis: '+it.asistencia.fechaInicial.format('MMM-dd')+' al '+it.asistencia.fechaFinal.format('MMM-dd')+ ' )'}}"
								/>
								
    					</div>
  					</div>
				--%></div>
				
				
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
