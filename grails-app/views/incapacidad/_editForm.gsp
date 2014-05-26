<%@page expressionCodec="none" %>
<div class="row">
	<div class="col-md-8">
		<div class="panel panel-default">
			<div class="panel-heading"><h3 class="panel-title">Incapacidad ${incapacidadInstance.id}</h3></div>
			<div class="panel-body">
				<g:form action="update" role="form" class="form-horizontal" >
					<f:with bean="incapacidadInstance">
						<g:hiddenField name="id" value="${incapacidadInstance.id}"/>
						<f:field property="empleado" input-class="form-control" />
						<f:field property="referenciaImms" input-class="form-control" label="Ref IMMSS"/>
						<f:field property="tipo" input-class="form-control" />
						<f:field property="comentario" input-class="form-control" />
					</f:with>
					<div class="col-sm-offset-3 col-sm-9">
						
		      			<button type="submit" class="btn btn-default">
		      				<span class="glyphicon glyphicon-floppy-save"></span> Actualizar
		      			</button>
		      				
		      			<g:link action="delete" id="${incapacidadInstance.id}" class="btn btn-danger">
		      				<span class="glyphicon glyphicon-trash"></span> Eliminar
		      			</g:link>
		      			
		    		</div>
					
				</g:form>
			</div><!-- end .panel-body -->
		</div> <!-- end .panel -->
		
		
		
	</div><!-- end .col-md -->
	
	<div class="col-md-4">
		<div class="panel panel-default">
			<div class="panel-heading"><h3 class="panel-title">Fechas</h3>
				
			</div>
			
				<ul class="list-group">
					<g:each in="${incapacidadInstance.dias.sort()}" var="row">
						<li class="list-group-item"><g:formatDate date="${row}" format="dd/MM/yyyy"/>
							
							<g:link action="eliminarFecha" id="${ incapacidadInstance.id}" 
								params="[fecha:g.formatDate(date:row,format:'dd/MM/yyyy')]"> Eliminar
							</g:link>
							
							
						</li>
					</g:each>
				</ul>
			<div class="panel-footer">
				<button class="btn btn-primary btn-sm" data-toggle="modal" data-target="#fechaForm">
  					Agregar fecha
				</button>
			</div>
			
		</div> <!-- end .panel -->
		
	</div><!-- end .col-md -->
	
</div>

<div class="row">
	<g:render template="/_common/fechaDialog"  model="[action:'agregarFecha',row:incapacidadInstance ]"/>
	
</div>

<%-- 

<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">Incapacidad ${incapacidadInstance.id}</h3>
	</div>
	<div class="panel-body">	
		
		<div class="row">
			<div class="col-md-6">
				<g:form action="update" role="form" class="form-horizontal" >
				<div class="form-group">
				<fieldset>
						<f:with bean="incapacidadInstance">
							
						</f:with>
					</fieldset>
		    			<div class="col-sm-offset-9 col-sm-2">
		      				<button type="submit" class="btn btn-default">
		      					<span class="glyphicon glyphicon-floppy-save"></span> Actualizar
		      				</button>
		    			</div>
		  			</div>
		</g:form>
			</div><!-- End col-md-6 -->
			
			<div class="col-md-6">
				<fieldset><legend>Fechas</legend>
				<ul>
					<g:each in="${incapacidadInstance.partidas}" var="row">
						<li><g:formatDate date="${row.fecha}" format="dd/MM/yyyy"/></li>
					</g:each>
				</ul>
				
				<g:form action="agregarFecha" class="form-inline" id="${incapacidadInstance.id}">
					<f:field bean="${new com.luxsoft.sw4.rh.IncapacidadDet()}" property="fecha" required="true"/>
					<button type="submit" class="btn btn-default">
		      				<span class="glyphicon glyphicon-floppy-save"></span> Agregar
		      		</button>
					
				</g:form>
				</fieldset>
				
			</div>
			
		</div><!-- End .row -->
		
	</div>
	<div class="panel-footer"></div>
</div>
--%>