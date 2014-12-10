<%@page expressionCodec="none"%>
<%@ page import="com.luxsoft.sw4.rh.ReporteDeNominaCommand" %>
<div class="modal fade" id="reporteDeNominaForm" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Reporte Nómina</h4>
			</div>
			<div class="modal-body">
				<g:form controller='reporte' action="nomina" class="form-horizontal">
					<fieldset>
						<legend> Parámetros</legend>
						<f:with bean="${new ReporteDeNominaCommand(ejercicio:session.ejercicio,periodicidad:periodicidad)}">
							<f:field property="ejercicio" input-class="form-control"/>
							<f:field property="tipo" input-class="form-control"/>
							<f:field property="periodicidad" input-class="form-control"/>
							<f:field property="folio" input-class="form-control" input-type="text"/>
						</f:with>
					</fieldset>
					<div class="form-group">
				    	<div class="col-sm-offset-2 col-sm-3">
				      		<button type="submit" class="btn btn-default">
				      			<span class="glyphicon glyphicon-cog"></span> Ejecutar
				      		</button>
				    	</div>
				  	</div>
				</g:form>
			</div>


		</div>
		<!-- moda-content -->
	</div>
	<!-- modal-di -->
</div>
