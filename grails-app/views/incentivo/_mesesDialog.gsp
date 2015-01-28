<%@page expressionCodec="none"%>
<div class="modal fade" id="calendarioForm" tabindex="-1">
	<div class="modal-dialog ">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Meses</h4>
			</div>
			<g:form action="actualizarPeriodoMensual" class="form-horizontal">
				<g:hiddenField name="tipo" value="${mes}"/>
				<div class="modal-body">
					<div class="form-group">
    					<label for="calendarioIni" class="col-sm-3">Ejercicio</label>
    					<div class="col-sm-9">
    						<g:select id="ejercicioField" class="form-control"  
								name="ejercicio" 
								value="${session.ejercicio}"
								from="${(2014..2020)}"
								/>
								
    					</div>
  					</div>
					<div class="form-group">
    					<label for="calendarioIni" class="col-sm-3">Mes</label>
    					<div class="col-sm-9">
    						<g:select id="mesField" class="form-control"  
								name="mes" 
								value="${mes}"
								optionKey="nombre"
								optionValue="nombre" 
								from="${meses}"
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
