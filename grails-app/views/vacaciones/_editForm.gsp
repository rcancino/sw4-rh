<%@page expressionCodec="none" %>

<div class="row">
	<div class="col-md-8">
		<div class="panel panel-default">
			<div class="panel-heading"><h3 class="panel-title">Solicitud ${vacacionesInstance.id} (${tipo })</h3></div>
			<div class="panel-body">
				<g:form action="update" role="form" class="form-horizontal" >
					<f:with bean="vacacionesInstance">
						<g:hiddenField name="id" value="${vacacionesInstance.id}"/>
						<f:field property="empleado" input-class="form-control" />
<%--						<f:field property="solicitud" input-class="form-control" />--%>
						<f:field property="comentario" input-class="form-control" />
					</f:with>
					<div class="col-sm-offset-3 col-sm-9">
						<g:if test="${!vacacionesInstance.autorizacion }">
						
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
					<button class="btn btn-primary btn-sm" data-toggle="modal" data-target="#fechaForm">
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