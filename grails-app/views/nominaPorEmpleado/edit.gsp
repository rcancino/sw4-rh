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
						<small>${nominaPorEmpleadoInstance?.ubicacion}  (${nominaPorEmpleadoInstance?.nomina?.periodo})  Días:${nominaPorEmpleadoInstance?.nomina?.diasPagados}</small>
						
					</h3>
				</div>
			</div>
		</div>

		<div class="row">
			
			<div class="col-md-3"> <!-- Task Panel -->
				
				<div class="panel panel-default panel-primary">
					<div class="panel-heading">Operaciones</div>
					
					<div class="list-group">
						<g:link controller="nomina" action="show" id="${nominaPorEmpleadoInstance?.nomina?.id}"
							class="list-group-item" >
							<span class="glyphicon glyphicon-arrow-left"></span> Regresar 
						</g:link>	 
						<g:link  action="agregarConcepto" params="[tipo:'PERCEPCION']"
							id="${nominaPorEmpleadoInstance.id}" 
							class="list-group-item" 
							data-toggle="modal"
							data-target="#percepcionModal">
							<span class="glyphicon glyphicon-plus"></span> Agregar Percepcion
						</g:link>
						
						<g:link class="list-group-item" action="todo">
						<span class="glyphicon glyphicon-plus"></span> Agregar Deducción</g:link>
						
						<g:link class="list-group-item" 
								controller="procesadorDeNomina" 
								action="actualizarNominaPorEmpleado"
								id="${nominaPorEmpleadoInstance.id}">
							<span class="glyphicon glyphicon-refresh"></span> Actualizar
						</g:link>
						
						<g:link class="list-group-item" action="todo">
							<span class="glyphicon glyphicon-pencil"></span> Modificar </g:link>
						
						<g:link class="list-group-item" action="todo" onClick="return confirm('Eliminar registro de nómina?');">
							<span class="glyphicon glyphicon-remove-circle"></span> Eliminar 
						</g:link>
						
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

	<!-- Modal para el alta de percepciones -->
	<div class="modal fade" id="percepcionModal" tabindex="-1" >
		<div class="modal-dialog">
			<div class="modal-content">
				%{-- <div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">Percepción</h4>
				</div>
				<div class="modal-body"><p>Forma para el mantenimiento</p></div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
        			<button type="button" class="btn btn-primary">Salvar</button>
				</div> --}%
			</div> <!-- moda-content -->
		</div> <!-- modal-dialog -->
	</div> <!-- .modal  -->

</body>
</html>
