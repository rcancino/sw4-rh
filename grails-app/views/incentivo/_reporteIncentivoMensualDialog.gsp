<%@page expressionCodec="none"%>
<div class="modal fade" id="reporteIncentivoMensualDialog" tabindex="-1">
	<div class="modal-dialog ">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="myModalLabel">Meses</h4>
			</div>
			<g:form controller="reporte" action="incentivoMensual" class="form-horizontal">
				<div class="modal-body">
				
					<g:hiddenField name="reportName" value="IncentivoMensualRH"/>
					<fieldset>
						<legend> Parámetros</legend>
						<f:with bean="${new com.luxsoft.sw4.IncentivoMensualCommand()}">
							<f:field property="ejercicio" input-class="form-control"/>
							<f:field property="mes" input-class="form-control"/>
						</f:with>
					</fieldset>
					
				
				</div>
				
				<div class="modal-footer">
					<button type="submit" class="btn btn-default">
		      			<span class="glyphicon glyphicon-cog"></span> Ejecutar
		      		</button>
				</div>
			</g:form>

		</div>
		<!-- moda-content -->
	</div>
	<!-- modal-di -->
</div>
