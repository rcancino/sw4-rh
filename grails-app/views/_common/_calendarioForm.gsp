<%@page expressionCodec="none"%>
<r:require module="datepicker"/>
<!-- Modal para el alta de percepciones -->
<div class="modal fade" id="calendarioForm" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Selección de Calendario</h4>
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
								optionValue="${{it.calendario.tipo+' '+it.folio+' ( '+it.inicio.format('MMM-dd')+' al '+it.fin.format('MMM-dd')+ ' )'}}"
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