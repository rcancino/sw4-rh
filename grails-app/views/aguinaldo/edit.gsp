<%@ page import="com.luxsoft.sw4.rh.NominaPorEmpleado" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Aguinaldo</title>
</head>
<body>
	
	<div class="container">
		
		<div class="row">
			<div class="col-md-12">
				<div class="page-header">
					<h3>Calculo de aguinaldo para: ${aguinaldo.empleado}
						<small>${aguinaldo.empleado.perfil.ubicacion.clave}  (${aguinaldoInstance.ejercicio})</small>
					</h3>
					<g:if test="${flash.message}">
						<div class="alert alert-warning" role="status">
							<strong>${flash.message}</strong>
						</div>
					</g:if>
				</div>
			</div>
		</div><!-- end .row1 Header -->
		
	</div>			
	

	
	
</body>
</html>
