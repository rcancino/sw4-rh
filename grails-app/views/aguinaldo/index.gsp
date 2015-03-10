<html>
<head>
	<meta charset="UTF-8">
	<title>Calculo de aguinaldo</title>
	<r:require modules="datatables"/> 
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="alert alert-info">
					<a href="" data-toggle="modal" data-target="#cambioGlobalDeEjercicioForm">
						<h3>Módulo para el calculo del aguinaldo (${session.ejercicio})</h3>
					</a>
					
					<g:if test="${flash.message}">
						<div class="message" role="status">
							<strong>${flash.message}</strong>
						</div>
					</g:if>
				</div>
			</div>
		</div><!-- end .row 1 -->
		
		<div class="row">
			
			
			<div class="button-panel">
				<div class="btn-group col-md-4">
					<input type='text' id="nombreField" placeholder="Empleado" class="form-control" autofocus="autofocus" autocomplete="off">
				</div>
				<div class="btn-group">
					<input type='text' id="ubicacionField" placeholder="Ubicacion" class="form-control" autocomplete="off" >
				</div>
				<div class="btn-group">
					<input type='text' id="tipoField" placeholder="Tipo" class="form-control" autocomplete="off" >
				</div>
				<div class="btn-group">
					<g:link action="index" class="btn btn-default"><span class="glyphicon glyphicon-refresh"></span> Refrescar</g:link>
					<button type="button" name="reportes"
							class="btn btn-default dropdown-toggle" data-toggle="dropdown"
							role="menu">
							Reportes <span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li><%--
							%{-- <g:jasperReport jasper="Aguinaldo" format="PDF" name="Aguinaldo">
							</g:jasperReport> --}%
							--%>
							<g:link action="reporte" params="[tipo:'BASE']"> Aguinaldo Base</g:link>
							<g:link action="reporte" params="[tipo:'CALCULO']"> Aguinaldo Cálculo</g:link>
							<g:link action="reporte" params="[tipo:'IMPUESTO']"> Aguinaldo Impuesto</g:link>
							<g:link action="reporte" params="[tipo:'PAGO']"> Aguinaldo Pago</g:link>
						</li>
					</ul>
				</div>
				<div class="btn-group">
					<button type="button" name="reportes"
							class="btn btn-default dropdown-toggle" data-toggle="dropdown"
							role="menu">
							Operaciones <span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li>
							<g:link action="create">
								<span class="glyphicon glyphicon-plus"></span> Nuevo
							</g:link>
						</li>
						<li>
							<g:link action="actualizar">
								<span class="glyphicon glyphicon-cog"></span> Actualizar
							</g:link>
						</li>
					</ul>
				</div>
			</div>
			
			
			
		</div><!-- end .row 2 Toolbar -->
		
		<div class="row">
			<div class="col-md-12">
				<!-- Nav tabs -->
				<ul class="nav nav-tabs" role="tablist">
  					<li role="presentation" class="active"><a href="#aguinaldo" role="tab" data-toggle="tab">Aguinaldo</a></li>
  					<li role="presentation"><a href="#calculo" role="tab" data-toggle="tab">Calculo</a></li>
  					<li role="presentation"><a href="#impuesto" role="tab" data-toggle="tab">Impuesto</a></li>
  					<li role="presentation"><a href="#pago" role="tab" data-toggle="tab">Pago</a></li>
				</ul>
				<!-- Tab panes -->
				<div class="tab-content">
  					<div role="tabpanel" class="tab-pane active" id="aguinaldo">
  						<g:render template="aguinaldoGrid"/>
  					</div>
  					<div role="tabpanel" class="tab-pane" id="calculo">
  						<g:render template="aguinaldoCalculoGrid"/>
  					</div>
  					<div role="tabpanel" class="tab-pane" id="impuesto">
  						<g:render template="aguinaldoCalculoImpuestoGrid"/>
  					</div>
  					<div role="tabpanel" class="tab-pane" id="pago">
  						<g:render template="aguinaldoPagoGrid"/>
  					</div>
  					
				</div>
			</div>
			
		</div><!--  end .row 3 Grid -->
	</div>
	
	<%--<g:render template="/_common/cambioDeEjercicioDialog"/>
	
	--%><r:script>
			$(function(){
				var table=$(".aguinaldoGrid").dataTable({
			        "paging":   false,
			        "ordering": false,
			        "info":     false,
			         "dom":'t'
    				});
    				
    				$("#nombreField").keyup(function(){
      					table.DataTable().column(0).search( $(this).val() ).draw();
					});
					
    				$("#ubicacionField").keyup(function(){
      					table.DataTable().column(1).search( $(this).val() ).draw();
					});
					
					$("#tipoField").keyup(function(){
      					table.DataTable().column(2).search( $(this).val() ).draw();
					});
					
			});
	</r:script>
</body>
</html>