<%@page expressionCodec="none"%>

<div class="modal fade" id="sdiPorForm" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
		
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">&times;</button>
			<h4 class="modal-title" id="myModalLabel">Report Mensual </h4>
		</div>
		<div class="modal-body">
		
			<g:jasperReport
				format="PDF" 
				name="SDI por Empleado" 
				jasper="SalarioDiarioIntegradoIndividual"
         		buttonPosition="bottom"
         		class="form-horizontal">

					<g:hiddenField name="ID"
						value="${modificacionInstance?.empleado?.id}" />
					<g:hiddenField name="TIPO" value="${modificacionInstance?.empleado?.salario?.periodicidad}"/>
					<g:hiddenField name="FECHA_ULT_MODIF" value="${modificacionInstance?.fecha}"/>
					<g:hiddenField name="SDI_ANTERIOR" value="${modificacionInstance?.sdiAnterior}"/>
<%--					<g:hiddenField name="FECHA_INI" value="${'28/04/2014'}"/>--%>
					
				<div class="modal-body">
					
					
					<div class="form-group">
						<label for="inicioPago" class="col-sm-3">Inicio </label>
						<div class="col-ms-9">
							<g:datePicker id="inicioPago"
								value="${Date.parse('dd/MM/yyyy','28/04/2014') }" 
								name="FECHA_INI"  
								precision="day" 
								class="form-control"
								readonly="readonly"/>
						</div>
					</div>
					
					<div class="form-group">
						<label for="finPago" class="col-sm-3">Fin</label>
						<div class="col-ms-9">
							<g:datePicker id="finPago"
								value="${Date.parse('dd/MM/yyyy','29/06/2014') }"  
								name="FECHA_FIN"  precision="day" class="form-control"/>
						</div>
					</div>
					<%--
					<div class="form-group">
						<label for="inicioPago" class="col-sm-3">FECHA_ULT_MODIF</label>
						<div class="col-ms-9">
							<g:datePicker id="ultimaModificacion" name="FECHA_ULT_MODIF"  precision="day" class="form-control"/>
						</div>
					</div>
  					
				--%></div>
				
			</g:jasperReport>
			</div>

		</div><!-- moda-content -->
	</div><!-- modal-di -->
</div>
