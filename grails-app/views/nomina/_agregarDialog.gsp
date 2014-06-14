<%@page expressionCodec="none"%>
<%@ page import="com.luxsoft.sw4.rh.CalendarioDet" %>
<div class="modal fade" id="agregarNominaForm" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Agregar nomina</h4>
			</div>
			<g:form action="generar" class="form-horizontal" >
				<div class="modal-body">
					<div class="form-group">
    					<label for="comentarioField" class="col-sm-3">Calendario</label>
    					<div class="col-sm-9">
    						<g:select id="periodoField" class="form-control"  
								name="calendarioDet" 
								from="${CalendarioDet.findAll('from CalendarioDet d where d not in (select n.calendarioDet from Nomina n)')}" 
								optionKey="id" 
								optionValue="${{it.calendario.tipo+' '+it.folio+' ( '+it.inicio.format('MMM-dd')+' al '+it.fin.format('MMM-dd')+ ' )'}}"
							/>
    					</div>
  					</div>
				</div>
				
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
					<g:submitButton class="btn btn-primary" name="Agregar"
							value="aceptar" />
				</div>
				
			</g:form>


		</div>
		<!-- moda-content -->
	</div>
	<!-- modal-di -->
</div>
