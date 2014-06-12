<%@page expressionCodec="none" %>

<div class="row">
	<div class="col-md-8">
		<div class="panel panel-default">
			<div class="panel-heading"><h3 class="panel-title">Incidencia ${incidenciaInstance.id} (${tipo })</h3></div>
			<div class="panel-body">
				<g:form action="update" role="form" class="form-horizontal" >
					<f:with bean="incidenciaInstance">
						<g:hiddenField name="id" value="${incidenciaInstance.id}"/>
						<f:field property="empleado" input-class="form-control" />
						<f:field property="fechaInicial" input-class="form-control" />
						<f:field property="fechaFinal" input-class="form-control" />
						<f:field property="tipo" input-class="form-control" />
						<f:field property="pagado" input-class="form-control" label="Con goce"/>
						<f:field property="comentario" input-class="form-control" />
					</f:with>
					<div class="col-sm-offset-3 col-sm-9">
						<g:if test="${!incidenciaInstance.autorizacion }">
						
		      				<button type="submit" class="btn btn-default">
		      					<span class="glyphicon glyphicon-floppy-save"></span> Actualizar
		      				</button>
		      				
		      				<g:link action="delete" id="${incidenciaInstance.id}" class="btn btn-danger">
		      					<span class="glyphicon glyphicon-trash"></span> Eliminar
		      				</g:link>
		      			</g:if>
		    		</div>
					
				</g:form>
			</div><!-- end .panel-body -->
		</div> <!-- end .panel -->
		
		<div class="panel panel-default">
			<div class="panel-heading"><h3 class="panel-title">Autorización ${incidenciaInstance?.autorizacion?.id}</h3></div>
			<div class="panel-body">
				<g:if test="${incidenciaInstance.autorizacion }">
					<form class="form-horizontal" >
						<div class="form-group">
							<label class="col-sm-2 control-label">Autorizó</label>
							<div class="col-sm-10">
      							<p class="form-control-static">${incidenciaInstance?.autorizacion?.autorizo?.username }</p>
    						</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">Comentario</label>
							<div class="col-sm-10">
      							<p class="form-control-static">${incidenciaInstance?.autorizacion?.descripcion }</p>
    						</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label">Fecha</label>
							<div class="col-sm-10">
      							<p class="form-control-static"><g:formatDate date="${incidenciaInstance.autorizacion.dateCreated}" format="dd/MM/yyyy HH:ss"/></p>
    						</div>
						</div>
					</form>
				</g:if>
				<g:else>
					<h3> Pendiente</h3>
				</g:else>
			</div> <!-- .end panel-body -->
			<div class="panel-footer">
				<g:if test="${incidenciaInstance.autorizacion }">
					<g:link action="cancelarAutorizacion" id="${incidenciaInstance.id}" class="btn btn-danger">
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
	
	
	
</div>

<div class="row">
	<g:render template="/_common/fechaDialog"  model="[action:'agregarFecha',row:incidenciaInstance ]"/>
	<g:if test="${!incidenciaInstance.autorizacion}">
		<g:render template="/_common/autorizacionDialog"  model="[action:'autorizar',row:incidenciaInstance ]"/>
	</g:if>
	
</div>