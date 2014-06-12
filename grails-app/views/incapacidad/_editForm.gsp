<%@page expressionCodec="none" %>
<div class="row">
	<div class="col-md-8">
		<div class="panel panel-default">
			<div class="panel-heading"><h3 class="panel-title">Incapacidad ${incapacidadInstance.id} (${tipo })</h3></div>
			<div class="panel-body">
				<g:form action="update" role="form" class="form-horizontal" >
					<f:with bean="incapacidadInstance">
						<g:hiddenField name="id" value="${incapacidadInstance.id}"/>
						<f:field property="empleado" input-class="form-control" />
						<f:field property="referenciaImms" input-class="form-control" label="Ref IMMSS"/>
						<f:field property="tipo" input-class="form-control" />
						<f:field property="fechaInicial" input-class="form-control" />
						<f:field property="fechaFinal" input-class="form-control" />
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
	
	
	
</div>



