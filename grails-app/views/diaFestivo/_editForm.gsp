<%@page expressionCodec="none" %>

<div class="row">
	<div class="col-md-8">
		<div class="panel panel-default">
			<div class="panel-heading"><h3 class="panel-title">Dia festivo </h3></div>
			<div class="panel-body">
				<g:form action="update" role="form" class="form-horizontal" >
					<f:with bean="diaFestivoInstance">
						<g:hiddenField name="id" value="${diaFestivoInstance.id}"/>
						<f:field property="ejercicio" input-class="form-control" />
						<f:field property="fecha" input-class="form-control" />
						<f:field property="descripcion" input-class="form-control" />
						<f:field property="parcial" input-class="form-control" />
						<f:field property="salida" input-class="form-control" >
							<g:datePicker name="salida" precision="minute" class="form-control"/>
						</f:field>
					</f:with>
					<div class="col-sm-offset-3 col-sm-9">
		      				<button type="submit" class="btn btn-default">
		      					<span class="glyphicon glyphicon-floppy-save"></span> Actualizar
		      				</button>
		      				
		      				<g:link action="delete" id="${diaFestivoInstance.id}" class="btn btn-danger">
		      					<span class="glyphicon glyphicon-trash"></span> Eliminar
		      				</g:link>
		      			
		    		</div>
					
				</g:form>
			</div><!-- end .panel-body -->
		</div> <!-- end .panel -->
		
		
		
	</div><!-- end .col-md -->
	
	


</div>