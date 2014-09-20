<%@ page import="com.luxsoft.sw4.rh.NominaPorEmpleado" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Nomina de empleado</title>
	<r:require module="jquery-ui"/>
</head>
<body>
	
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="page-header">
					<h3>Nómina de : ${nominaPorEmpleadoInstance?.empleado}
						<small>${nominaPorEmpleadoInstance?.ubicacion}  (${nominaPorEmpleadoInstance?.nomina?.periodo})  Días:${nominaPorEmpleadoInstance?.nomina?.diasPagados}</small>
					</h3>
					<g:if test="${flash.message}">
						<div class="alert alert-warning" role="status">
							<strong>${flash.message}</strong>
						</div>
					</g:if>
				</div>
			</div>
		</div>

		<div class="row">
			
			<div class="col-md-3"> <!-- Task Panel -->
				
				<div class="panel panel-default panel-primary">
					<div class="panel-heading">Operaciones</div>
					
					<div class="list-group">
						
						<g:link controller="nominaPorEmpleado" action="edit" id="${nextItem}"
							class="list-group-item" >
							<span class="glyphicon glyphicon-arrow-right"></span> Siguiente 
						</g:link>
						
						<g:link controller="nominaPorEmpleado" action="edit" id="${prevItem}"
							class="list-group-item" >
							<span class="glyphicon glyphicon-arrow-left"></span> Anterior 
						</g:link>
						
						<g:link class="list-group-item" action="show" controller="asistencia" id="${nominaPorEmpleadoInstance?.asistencia?.id }">
							<span class="glyphicon glyphicon-pencil"></span> Asistencia </g:link>
						
						
						<g:link controller="nomina" action="show" id="${nominaPorEmpleadoInstance.nomina.id}"
							class="list-group-item" >
							<span class="glyphicon glyphicon-list-alt"></span> Regresar a nómina:${nominaPorEmpleadoInstance.nomina.folio}
						</g:link>
						
						
						
						
						<a class="list-group-item" data-toggle="modal" data-target="#cambiarNominaDialog"> 
							<span class="glyphicon glyphicon-search"></span> Buscar
						</a>
						
						<g:if test="${!nominaPorEmpleadoInstance.cfdi}">
						
						<g:link  action="agregarConcepto" params="[tipo:'PERCEPCION']"
							id="${nominaPorEmpleadoInstance.id}" 
							class="list-group-item" 
							data-toggle="modal"
							data-target="#percepcionModal">
							<span class="glyphicon glyphicon-plus"></span> Agregar Percepción
						</g:link>
						
						<g:link  action="agregarConcepto" params="[tipo:'DEDUCCION']"
							id="${nominaPorEmpleadoInstance.id}" 
							class="list-group-item" 
							data-toggle="modal"
							data-target="#deduccionModal">
							<span class="glyphicon glyphicon-plus"></span> Agregar Deducción
						</g:link>
						
						
						
						<g:link class="list-group-item"
								action="actualizarNominaPorEmpleado"
								id="${nominaPorEmpleadoInstance.id}">
							<span class="glyphicon glyphicon-refresh"></span> Re-Calcular
						</g:link>
						
						
						<g:link class="list-group-item" action="delete" onClick="return confirm('Eliminar registro de nómina?');"
							id="${nominaPorEmpleadoInstance.id }" >
							<span class="glyphicon glyphicon-remove-circle"></span> Eliminar 
						</g:link>
						
						<g:link class="list-group-item" action="ajusteMensualIsr" onClick="return confirm('Aplicar ajuste mensual ISR?');"
							id="${nominaPorEmpleadoInstance.id}" >
							<span class="glyphicon glyphicon-cog"></span> Ajuste mensual ISR 
						</g:link>
						
						<g:link class="list-group-item" action="eliminarMensualIsr" onClick="return confirm('Aplicar ajuste mensual ISR?');"
							id="${nominaPorEmpleadoInstance.id}" >
							<span class="glyphicon glyphicon-trash"></span> Eliminar ajuste ISR 
						</g:link>

						</g:if>	 
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

					    	<g:if test="${!nominaPorEmpleadoInstance.cfdi}">

								<g:link class="list-group-item" action="todo" >
									<span class="glyphicon glyphicon-screenshot"></span> Timbrar
								</g:link>
							</g:if>
							
							<g:link class="list-group-item" 
								controller="reciboDeNomina" 
								action="imprimirRecibo" id="${nominaPorEmpleadoInstance.id}">
								<span class="glyphicon glyphicon-print"></span> Imprimir recibo
							</g:link>							
							
							<g:if test="${nominaPorEmpleadoInstance.cfdi}">
								<g:jasperReport
									controller="reciboDeNomina"
									action="imprimirCfdi"
									jasper="CFDI" 
									format="PDF" 
									name="Imprimir">
								<g:hiddenField name="id" value="${nominaPorEmpleadoInstance.id}"/>
								</g:jasperReport>
								<g:link class="list-group-item" action="cancelar" onclick="return confirm('Cancelar CFDI?');">
								<span class="glyphicon glyphicon-ban-circle"></span> Cancelar
							</g:link>
								<g:link class="list-group-item" action="todo">
								<span class="glyphicon glyphicon-picture"></span> Mostrar
							</g:link>
							<a: href="#" class="list-group-item">
								<span class="glyphicon glyphicon-envelope"></span> Enviar mail
							</a:>
							</g:if>
							
							<%--<g:link class="list-group-item" action="todo">
								<span class="glyphicon glyphicon-envelope"></span> Enviar mail 
							</g:link>
											
						--%></div>
				    </div>
				  </div>
				  
				  
				</div>

				
			</div>

			<div class="col-md-9">
				<div class="panel panel-default">
					<div class="panel-heading">Asistencia ${ nominaPorEmpleadoInstance?.asistencia?.periodo}</div>
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
	
	<div class="modal fade" id="deduccionModal" tabindex="-1" >
		<div class="modal-dialog">
			<div class="modal-content">
				
			</div> <!-- moda-content -->
		</div> <!-- modal-dialog -->
	</div> <!-- .modal  -->
	
	<g:render template="cambiarNominaDialog"/>
	
	<r:script>
	
	var get_data_for_popover=function(){
		var element=$(this);
		var url=$(this).attr('data-url');
		if($(this).attr('data-popover-visible')==="true"){
			
			element.popover('hide');
			element.attr('data-popover-visible',"false");
			return;
		}
		$.ajax({
			type:'GET',
			url:url,
			dataType:'html',
			success:function(data){
				element.attr('data-content',data);
				element.attr('data-popover-visible',"true");
				element.popover('show');
			}
		});
	}
	
	
	$(function(){
		$('[data-popover=true]').popover({"trigger":"manual","html":"true"});
		$('[data-popover=true]').click(get_data_for_popover);
		
	});
	
	
	
</r:script>

</body>
</html>
