<%@ page import="com.luxsoft.sw4.rh.Incentivo" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operaciones2"/>
	<title>Incentivos</title>
	<r:require modules="datatables,forms"/> 
</head>
<body>
	<content tag="header">
		<h3>Control de incentivos ${tipo}</h3>
	</content>
	
	<content tag="consultas">
		<div class="list-group">
 			<g:link action="semanal" class="list-group-item" >
					Semanal
			</g:link>
			<g:link action="quincenal" class="list-group-item" >
					Quincenal
			</g:link>
  			<g:link action="mensual" class="list-group-item" >
					Mensual
			</g:link>
			
		</div>
	</content>
	
	<content tag="gridTitle">
		<a href="" data-toggle="modal" data-target="#calendarioForm">
			 <strong>${mes} - ${ejercicio}</strong> 
		</a>
	</content>
	
	<content tag="toolbarPanel">
		
		<div class="row button-panel">
			
			<div class="col-md-4 ">
				<input id="searchField" class="form-control" type="text" placeholder="Empleado" autofocus="autofocus">
			</div>

			<div class="col-md-8">
				<div class="btn-group">
					<g:link action="mensual" 
							class="btn btn-default">
							<span class="glyphicon glyphicon-refresh"></span> Refresacar
					</g:link>
					
					<a href="#generarIncentivoForm" data-toggle="modal" class="btn btn-default" >
			 			<span class="glyphicon glyphicon-cog"></span> Generar
					</a>
					
					<a href="#calcularIncentivoMensualForm" data-toggle="modal" class="btn btn-default" >
			 			<span class="glyphicon glyphicon-wrench"></span> Calcular
					</a>
					<a href="#semanaForm" data-toggle="modal" class="btn btn-default" >
			 			<span class="glyphicon glyphicon-calendar"></span> Asignar calendario
					</a>
					
					
				</div>
				<div class="btn-group">
					<button type="button" name="reportes"
						class="btn btn-default dropdown-toggle" data-toggle="dropdown"
						role="menu">
						Reportes <span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li>
							<a href="#reporteIncentivoMensualDialog" data-toggle="modal">Incentivo</a>
						</li>
					</ul>
				</div><%-- end .btn-group reportes --%>
			</div><%-- end .col-md button panel --%>
			
		</div>	
  		
  		
	</content><!-- end .gridTask -->
	
	<content tag="panelBody">
		<g:render template="gridMensual" />
		<g:render template="mesesDialog"/>
		<g:render template="generacionMensualDialog" />
		<g:render template="calculoMensualDialog" />
		<g:render template="reporteIncentivoMensualDialog" />
		
		
<div class="modal fade" id="semanaForm" tabindex="-1">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Periodo</h4>
			</div>
			<g:form action="asignarCalendarioDeIncentivoMensual" class="form-horizontal">
				
				<g:hiddenField name="ejercicio" value="${ejercicio}"/>
				<g:hiddenField name="mes" value="${mes}"/>
				<div class="modal-body">
					<div class="form-group">
    					<label for="calendarioIni" class="col-sm-3">Calendario</label>
    					<div class="col-sm-9">
    						<g:select id="calendarioField" class="form-control"  
								name="calendarioDetId" 
								value="${calendarioDet}"
								from="${periodos}" 
								optionKey="id" 
								optionValue="${{it.calendario.tipo+' '+it.folio+' ( '+it.inicio.format('MMM-dd')+' al '+it.fin.format('MMM-dd')+ ' )'+' (Asis: '+it.asistencia.fechaInicial.format('MMM-dd')+' al '+it.asistencia.fechaFinal.format('MMM-dd')+ ' )'}}"
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
			$(function(){
				var table=$(".incentivoGrid").dataTable({
			        "paging":   false,
			        "ordering": false,
			        "info":     false,
			        "dom":'t'
    				});
    				
    				$("#searchField").keyup(function(){
      					table.DataTable().search( $(this).val() ).draw();
					});
					
				$('.porcentaje').autoNumeric({vMin:'0.00',vMax:'99.00',mDec:'6'});
			});
		</r:script>
	</content>

</body>
</html>