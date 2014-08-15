<%@page expressionCodec="none"%>

<div class="modal fade" id="calendarioForm" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Report Mensual </h4>
			</div>
			
			
<%--			<g:form action="reporteMensual" class="form-horizontal">--%>
				<g:jasperReport
					format="PDF" name="Retardo mensual"
         						 jasper="RetardoMensualPorEmpleado"
         						 buttonPosition="bottom"
         						 
         						 class="form-horizontal">
				<g:hiddenField name="tipo" value="${tipo}"/>
				<g:hiddenField name="ID" value="${asistenciaInstance?.empleado?.id?:'%'}"/>
				<div class="modal-body">
					<div class="form-group">
    					<label for="calendarioIni" class="col-sm-3">Calendario Inicial</label>
    					<div class="col-sm-9">
    						<g:select id="calendarioField" class="form-control"  
								name="CAL_ID_INI" 
								value="${calendarioDet}"
								from="${periodos}" 
								optionKey="id" 
								optionValue="${{it.calendario.tipo+' '+it.folio+' ( '+it.inicio.format('MMM-dd')+' al '+it.fin.format('MMM-dd')+ ' )'}}"
								/>
								
    					</div>
  					</div>
					
						<div class="form-group">
	    					<label for="calendarioIni" class="col-sm-3">Calendario Final</label>
	    					<div class="col-sm-9">
	    						<g:select id="calendarioField" class="form-control"  
									name="CAL_ID_FIN" 
									value="${calendarioDet2}"
									from="${periodos}" 
									optionKey="id" 
									optionValue="${{it.calendario.tipo+' '+it.folio+' ( '+it.inicio.format('MMM-dd')+' al '+it.fin.format('MMM-dd')+ ' )'}}"
									/>
									
	    					</div>
	  					</div>
						<div class="form-group">
							<label for="inicioPago" class="col-sm-3">Inicio pago</label>
							<div class="col-ms-9">
								<g:datePicker id="inicioPago" name="FECHA_INI_PAGO"  precision="day" class="form-control"/>
							</div>
						</div>
						<div class="form-group">
							<label for="finPago" class="col-sm-3">Fin pago</label>
							<div class="col-ms-9">
								<g:datePicker id="finPago" name="FECHA_FIN_PAGO"  precision="day" class="form-control"/>
							</div>
						</div>
					
  					
				</div>
				
				<div class="modal-footer">
<%--					<g:jasperButton format="pdf"  jasper="RetardoMensualPorEmpleado" text="PDF" class="btn btn-default"/>--%>
<%--					<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>--%>
<%--					<g:submitButton class="btn btn-primary" name="aceptar" value="Aceptar" />--%>
					
				</div>
				</g:jasperReport>


		</div>
		<!-- moda-content -->
	</div>
	<!-- modal-di -->
</div>
