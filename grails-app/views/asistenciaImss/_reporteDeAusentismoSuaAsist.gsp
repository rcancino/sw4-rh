<%@page expressionCodec="none"%>

<div class="modal fade" id="reporteDeAusentismoSuaAsist" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Ausentismo Sua</h4>
			</div>
			
			
<%--			<g:form action="reporteDeAusentismoSua" class="form-horizontal">--%>
				<g:jasperReport
					format="PDF" name="AusentismoSUA Asist"
         						 jasper="AusentismoSuaR"
         						 buttonPosition="bottom"
         						 
         						 class="form-horizontal">
			
			
				<div class="modal-body">
					<div class="form-group">
    					<label for="calendarioIni" class="col-sm-3">Fecha Inicio</label>
    					<div class="col-sm-9">
<%--    						<g:field type="text" name="FECHA_INI" class="form-control"/>	--%>
							<g:datePicker id="inicioPago" name="FECHA_INI"  precision="day" class="form-control"/>
    					</div>
  					</div>
					
						<div class="form-group">
	    					<label for="calendarioIni" class="col-sm-3">Fecha fin</label>
	    					<div class="col-sm-9">
	    						
								<g:datePicker id="inicioPago" name="FECHA_FIN"  precision="day" class="form-control"/>	
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
