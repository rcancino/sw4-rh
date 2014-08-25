<%@page defaultCodec="none" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	
	<title>SDI</title>
	<r:require modules="datatables"/> 
</head>
<body>
	<content tag="header">
		<h3>Salario diario integrado Bimestre: ${bimestre} (${session.ejercicio })</h3>
	</content>
	
	<conente tag="buttonBar">
		
	</conente>
	
	<content tag="content">
		<div class="col-md-2">
			<input type='text' id="ubicacionField" placeholder="Ubicacion" class="form-control" autofocus="autofocus">
			
		</div>
		<div class="col-md-2">
			
			<input type='text' id="nombreField" placeholder="Empleado" class="form-control">
		</div>
		<div class="btn-group ">
			<a href="#" class="btn btn-default">
				<span class="glyphicon glyphicon-search"></span> Cambiar bimestre
			</a>
			
			<g:link action="calcularSalarioDiarioIntegrado" class="btn btn-default" >
				 Calcular
			</g:link>
			<g:link action="aplicarSalarioDiarioIntegrad" onclick="return confirm('Seguro que desa aplicar el SDI a todos los empleados?');" class="btn btn-default" target="_blank">
				 Aplicar
			</g:link>
		</div>
		
		<g:render template="sdiGrid"/>
		<r:script>
			$(function(){
				var table=$("#sdiGrid").dataTable({
			        "paging":   false,
			        "ordering": false,
			        "info":     false,
			         "dom":'t'
    				});
    				
    				$("#ubicacionField").keyup(function(){
      					table.DataTable().column(2).search( $(this).val() ).draw();
					});
					$("#nombreField").keyup(function(){
      					table.DataTable().column(1).search( $(this).val() ).draw();
					});
			});
		</r:script>
	</content>
</body>
</html>