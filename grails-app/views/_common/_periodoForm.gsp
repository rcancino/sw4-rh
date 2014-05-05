<%@page expressionCodec="none"%>
<r:require module="datepicker"/>
<!-- Modal para el alta de percepciones -->
<div class="modal fade" id="periodoForm" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Periodo</h4>
			</div>
			<g:form action="${action}" class="form-horizontal">
				
				<div class="modal-body">
					<div class="form-group">
    					<label for="fechaIni" class="col-sm-3">Fecha inicial</label>
    					<div class="col-sm-9">
    						<input type="text" class="form-control datepicker" id="fechaIni" name="fechaInicial" 
    							value="${g.formatDate(date:periodo.fechaInicial,format:'dd/MM/yyyy') }">
    					</div>
  					</div>
  					
  					<div class="form-group">
    					<label for="fechaFin" class="col-sm-3">Fecha final</label>
    					<div class="col-sm-9" >
    						<input type="text" class="form-control datepicker" id="fechaFin" name="fechaFinal" 
    							value="${g.formatDate(date:periodo.fechaFinal,format:'dd/MM/yyyy') }">
    					</div>
  					</div>
  					
				</div>
				
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
					<g:submitButton class="btn btn-primary" name="Ejecutar"
							value="ejecutar" />
				</div>
				
			</g:form>


		</div>
		<!-- moda-content -->
	</div>
	<!-- modal-di -->
</div>
<r:script>
    /** Registro datepicker **/
	$(".datepicker").datepicker({
        //showOn: "both",
        changeMonth: true,
        changeYear: true,
        appendText: "",
        showAnim: "fold",
        showButtonPanel: true,
        dateFormat:"dd/mm/yy" 
    });
</r:script>