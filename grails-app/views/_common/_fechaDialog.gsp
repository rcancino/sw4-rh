<%@page expressionCodec="none"%>
<r:require module="datepicker"/>
<div class="modal fade" id="fechaForm" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Periodo</h4>
			</div>
			<g:form action="${action}" class="form-horizontal" id="${row.id}">
				
				<div class="modal-body">
					<div class="form-group">
    					<label for="fechaField" class="col-sm-3">Fecha</label>
    					<div class="col-sm-9">
    						<input type="text" class="form-control datepicker" id="fechaField" name="fecha" 
    							value="${g.formatDate(date:new Date(),format:'dd/MM/yyyy') }">
    					</div>
  					</div>
				</div>
				
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
					<g:submitButton class="btn btn-primary" name="Aceptar"
							value="aceptar" />
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