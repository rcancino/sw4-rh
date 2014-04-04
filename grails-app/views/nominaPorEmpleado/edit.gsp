<%@ page import="com.luxsoft.sw4.rh.NominaPorEmpleado" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Nomina de empleado</title>
</head>
<body>
	
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="page-header">
					<h3>Nómina de : ${nominaPorEmpleadoInstance?.empleado}
						<small>${nominaPorEmpleadoInstance?.ubicacion}</small>
					</h3>
				</div>
			</div>
		</div>

		<div class="row">
			
			<div class="col-md-3"> <!-- Task Panel -->
				
				<div class="panel panel-default panel-primary">
					<div class="panel-heading">Operaciones</div>
					
					<div class="list-group">
						<g:link class="list-group-item" action="agregarConcepto" id="${nominaPorEmpleadoInstance?.id}">
						<span class="glyphicon glyphicon-plus"></span> Agregar Percepción</g:link>
						<g:link class="list-group-item" action="todo">
						<span class="glyphicon glyphicon-plus"></span> Agregar Deducción</g:link>
						<g:link class="list-group-item" action="todo">
						<span class="glyphicon glyphicon-pencil"></span> Modificar </g:link>
						<g:link class="list-group-item" action="todo">
						<span class="glyphicon glyphicon-remove-circle"></span> Cancelar </g:link>
					</div>

				</div>

				<div class="panel-group " id="accordion">
				  <div class="panel panel-default panel-default">
				    <div class="panel-heading">
				      <h4 class="panel-title">
				        <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
				          Recibo CFDI
				        </a>
				      </h4>
				    </div>

				    <div id="collapseOne" class="panel-collapse collapse ">
					    <div class="list-group">
							<g:link class="list-group-item" action="todo">
								<span class="glyphicon glyphicon-screenshot"></span> Timbrar
							</g:link>
							<g:link class="list-group-item" action="todo">
								<span class="glyphicon glyphicon-ban-circle"></span> Cancelar
							</g:link>
							<g:link class="list-group-item" action="todo">
								<span class="glyphicon glyphicon-print"></span> Imprimir
							</g:link>
							<g:link class="list-group-item" action="todo">
								<span class="glyphicon glyphicon-picture"></span> Mostrar
							</g:link>
							<g:link class="list-group-item" action="todo">
								<span class="glyphicon glyphicon-envelope"></span> Enviar mail 
							</g:link>
											
						</div>
				    </div>
				  </div>
				  
				  
				</div>

				
			</div>

			<div class="col-md-9">
				<div class="panel panel-default">
					<div class="panel-heading">Generales y total de la nomina</div>
					<div class="panel-body">
						<g:render template="form"/>
					</div>
					
					
				</div>
				<div class="row">
					<div class="col-md-6">
						<div class="panel panel-default">
							<div class="panel-heading">Precepciones</div>
							
							<g:render template="conceptos" model="[param:'PERCEPCION']"/>
						</div>

					</div>
					<div class="col-md-6">
						<div class="panel panel-default">
							<div class="panel-heading">Deducciones
								
							</div>
							<g:render template="conceptos" model="[param:'DEDUCCION']"/>
						</div>
					</div>
				</div>
			</div>

		</div>

	</div>

</body>
</html>
