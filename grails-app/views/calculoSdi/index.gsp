<%@page defaultCodec="none" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="procesos"/>
	<title>SDI</title>
	<r:require modules="datatables"/> 
</head>
<body>
	<content tag="header">
		<h3>Salario diario integrado Bimestre: ${session.bimestre} (${session.ejercicio })</h3>
	</content>
	
	<conente tag="buttonBar">
		
	</conente>
	
	<content tag="content">
		<div class="col-md-2">
			<input type='text' id="nombreField" placeholder="Empleado" class="form-control">
		</div>
		<div class="col-md-2">
			<input type='text' id="ubicacionField" placeholder="Ubicacion" class="form-control" autofocus="autofocus">
			
		</div>
		<div class="btn-group ">
			<a href="#cambioDeBimestreDialog" class="btn btn-default" data-toggle="modal">
				<span class="glyphicon glyphicon-search"></span> Cambiar bimestre
			</a>
			
			<g:link action="calcularSalarioDiarioIntegrado" class="btn btn-default" 
				onclick="return confirm('Generar calculo bimestral de salario diario integrado');"
				params="[bimestre:bimestre,ejercicio:ejercicio]">
				 Calcular
			</g:link>
			<g:link action="aplicarSalarioDiarioIntegrado" 
				onclick="return confirm('Seguro que desa aplicar el SDI a todos los empleados bimestre:${session.bimestre}? ejercicio:${session.ejercicio }');" 
				class="btn btn-default">
				 Aplicar
			</g:link>
			<%--<g:link action="print" class="btn btn-default" >
				 <span class="glyphicon glyphicon-print"></span> Imprimir
			</g:link>
		--%>
		
		
		
		</div>
		
		<div class="btn-group">
					<button type="button" name="reportes"
							class="btn btn-default dropdown-toggle" data-toggle="dropdown"
							role="menu">
							Reportes <span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li>
							 
							<g:link action="reporte" params="[tipo:'BASE']"> Sdi Bimestral Base</g:link>
							<g:link action="reporte" params="[tipo:'CALCULO']"> Sdi Bimestral Calculo</g:link>
							<g:link action="reporte" params="[tipo:'VARIABLES']">Sdi Bimestral Variables</g:link>
							
						</li>
					</ul>
		</div>
		
		<g:render template="gridPanel"/>
		<g:render template="cambioDeBimestreDialog"/>
		<r:script>
			$(function(){
				var table=$("#sdiGrid").dataTable({
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
					
			});
		</r:script>
	</content>
</body>
</html>