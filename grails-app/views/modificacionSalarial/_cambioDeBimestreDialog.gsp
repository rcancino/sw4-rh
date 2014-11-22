<%@page expressionCodec="none"%>
<div class="modal fade" id="cambioDeBimestreDialog" tabindex="-1">
	<div class="modal-dialog ">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="myModalLabel">Bimestre</h4>
			</div>
			<g:form action="cambiarBimestre" class="form-horizontal">
			
				<div class="modal-body">
					<f:with bean="${new com.luxsoft.sw4.rh.CalculoBimestralCommand()}">
						<f:field property="ejercicio" input-class="form-control" />
						<f:field property="bimestre" input-class="form-control" value="${session.bimestre }"/>
					</f:with>
				</div>
				
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
					<button type="submit" class="btn btn-primary">
		      			<span class="glyphicon glyphicon-cog"></span> Aceptar
		      		</button>
				</div>
			</g:form>

		</div>
		<!-- moda-content -->
	</div>
	<!-- modal-di -->
</div>
