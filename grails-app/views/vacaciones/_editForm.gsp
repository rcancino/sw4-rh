<%@page expressionCodec="none" %>

<div class="row">
	<div class="col-md-8">
		<div class="panel panel-default">
			<div class="panel-heading"><h3 class="panel-title">Solicitud ${vacacionesInstance.id} (${tipo })</h3></div>
			<div class="panel-body">
				<g:form action="update" role="form" class="form-horizontal" >
					
					<div class="form-group">
						<label class="control-label col-sm-2">Control</label>
						<div class="col-sm-2">
							<input type="text" class="form-control"
								value="${vacacionesInstance?.control?.id}" disabled/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="control-label col-sm-2">Disponibles</label>
						<div class="col-sm-6">
							<input type="text" class="form-control"
								value="${vacacionesInstance?.control?.diasDisponibles}" disabled/>
						</div>
					</div>
					
					
					<div class="form-group">
						<label class="control-label col-sm-2">Vigencia</label>
						<div class="col-sm-6">
							<input type="text" class="form-control"
								value="${formatDate(date:vacacionesInstance?.control?.getVigencia(),format:'dd/MM/yyyy')}" 
								disabled />
						</div>
					</div>
					
					<f:with bean="vacacionesInstance">
						<g:hiddenField name="id" value="${vacacionesInstance.id}"/>
						
						<f:field property="empleado" input-class="form-control" />
<%--						<f:field property="solicitud" input-class="form-control" />--%>
						<f:field property="comentario" input-class="form-control" />
						<f:field input-id="pagadoField" property="pg" input-class="form-control"
							input-autocomplete="off" 
							label="Pagadas"/>
						<f:field input-id="diasPagadosField" 
							property="diasPagados" 
							input-class="form-control" label="Pagadas"
						 	input-disabled="${!vacacionesInstance.diasPagados}"/>
						
						<div class="form-group"><label for="calendarioField" class="col-sm-2 control-label">Calendario</label>
							<div class="col-sm-10">
								<g:select id="calendarioField" 
									class="form-control"  
									name="calendarioDet.id" 
									value="${vacacionesInstance?.calendarioDet?.id}"
									from="${periodos}" 
									optionKey="id" 
									optionValue="${{it.calendario.tipo+' '+it.folio+' ( '+it.inicio.format('MMM-dd')+' al '+it.fin.format('MMM-dd')+ ' )'}}"
									disabled="${vacacionesInstance.diasPagados<=0}?'':'disabled'"
									noSelection="['':'Seleccione un calendario']"
									autocomplete="off"
								/>
							</div>
						</div>
						
						<f:field input-id="cierreAnualField" 
							property="cierreAnual" 
							input-class="form-control" label="Cierre anual"
						 	/>
						

					</f:with>
					<div class="col-sm-offset-3 col-sm-9">
						<g:if test="${!vacacionesInstance.autorizacion }">
						
							<g:link action="index" class="btn btn-default">
		      					Cancelar
		      				</g:link>
		      				
		      				<button type="submit" class="btn btn-default">
		      					<span class="glyphicon glyphicon-floppy-save"></span> Actualizar
		      				</button>
		      				
		      				<g:link action="delete" id="${vacacionesInstance.id}" class="btn btn-danger">
		      					<span class="glyphicon glyphicon-trash"></span> Eliminar
		      				</g:link>
		      			</g:if>
		    		</div>
					
				</g:form>
			</div><!-- end .panel-body -->
		</div> <!-- end .panel -->
		
		<div class="panel panel-default">
			<div class="panel-heading"><h3 class="panel-title">Autorización ${vacacionesInstance?.autorizacion?.id}</h3></div>
			<div class="panel-body">
				<g:if test="${vacacionesInstance.autorizacion }">
					<form class="form-horizontal" >
						<div class="form-group">
							<label class="col-sm-2 control-label">Autorizó</label>
							<div class="col-sm-10">
      							<p class="form-control-static">${vacacionesInstance?.autorizacion?.autorizo?.username }</p>
    						</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">Comentario</label>
							<div class="col-sm-10">
      							<p class="form-control-static">${vacacionesInstance?.autorizacion?.descripcion }</p>
    						</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label">Fecha</label>
							<div class="col-sm-10">
      							<p class="form-control-static"><g:formatDate date="${vacacionesInstance.autorizacion.dateCreated}" format="dd/MM/yyyy HH:ss"/></p>
    						</div>
						</div>
					</form>
				</g:if>
				<g:else>
					<h3> Pendiente</h3>
				</g:else>
			</div> <!-- .end panel-body -->
			<div class="panel-footer">
				<g:if test="${vacacionesInstance.autorizacion }">
					<g:link action="cancelarAutorizacion" id="${vacacionesInstance.id}" class="btn btn-danger">
		      			<span class="glyphicon glyphicon-trash"></span> Cancelar autorización
		      		</g:link>
				</g:if>
				<g:else>
					<button class="btn btn-success btn-sm" data-toggle="modal" data-target="#autorizacionForm">
  						<span class="glyphicon glyphicon-ok"></span> Autorizar
					</button>
				</g:else>
				
			</div><!-- end .panel-footer -->
		</div><!--  end .panel autorizacion -->
		
	</div><!-- end .col-md -->
	
	<div class="col-md-4">
		<div class="panel panel-default">
			<div class="panel-heading"><h3 class="panel-title">Fechas</h3>
				
			</div>
			
				<ul class="list-group">
					<g:each in="${vacacionesInstance.dias.sort()}" var="row">
						<li class="list-group-item"><g:formatDate date="${row}" format="dd/MM/yyyy"/>
							<g:if test="${!vacacionesInstance.autorizacion }">
								<g:link action="eliminarFecha" id="${ vacacionesInstance.id}" 
								params="[fecha:g.formatDate(date:row,format:'dd/MM/yyyy')]"> Eliminar
							</g:link>
							</g:if>
							
						</li>
					</g:each>
				</ul>
			<div class="panel-footer">
				<g:if test="${!vacacionesInstance.autorizacion }">
					<button id="addFecha" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#fechaForm"
					${vacacionesInstance.pg?'disabled':''}>
  					Agregar fecha
				</button>
				</g:if>
				
			</div>
			
		</div> <!-- end .panel -->
		
	</div><!-- end .col-md -->
	
</div>

<div class="row">
	<g:render template="/_common/fechaDialog"  model="[action:'agregarFecha',row:vacacionesInstance ]"/>
	<g:if test="${!vacacionesInstance.autorizacion}">
		<g:render template="/_common/autorizacionDialog"  model="[action:'autorizar',row:vacacionesInstance ]"/>
	</g:if>
	
</div>
<r:script>
	$(function(){
		$("#pagadoField").on('change',function(e){
			if ($(this).is(':checked') == false){
				$('#diasPagadosField').prop('disabled', true);
				$('#calendarioField').prop('disabled', true);
				$('#addFecha').prop('disabled', false);
			}else{
				$('#diasPagadosField').prop('disabled', false);
				$('#calendarioField').prop('disabled', false);
				$('#addFecha').prop('disabled', true);
			}
			console.log('Vacaciones pagadas: '+$(this).is(':checked'));
		});
		var dp=$("#diasPagadosField").val();
		if(dp!=='undefined' && dp>0){
			$('#calendarioField').prop('disabled', false);
		}
		
	});
</r:script>