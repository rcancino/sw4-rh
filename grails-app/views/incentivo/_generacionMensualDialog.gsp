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
    					<label for="calendarioIni" class="col-sm-2">Ejercicio</label>
    					<div class="col-sm-10">
    						<p class="form-control-static">${session.ejercicioIncentivoMensual}</p>
    					</div>
  					</div>
  					<div class="form-group">
    					<label for="calendarioIni" class="col-sm-2">Mes</label>
    					<div class="col-sm-10">
    						<p class="form-control-static">${mes}</p>
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
