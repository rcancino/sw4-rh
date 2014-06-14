<%@page expressionCodec="none" %>

<div class="panel panel-default">
	<div class="panel-heading">
		
		<h3 class="panel-title">Alta de empleado</h3>
	</div>
	<div class="panel-body">	
	
		<g:form action="create" role="form" class="form-horizontal">
			<fieldset>
				<f:with bean="empleadoInstance">
					<f:field property="apellidoPaterno" input-class="form-control" />
					<f:field property="apellidoMaterno" input-class="form-control" />
					<f:field property="nombres" input-class="form-control"/>
					<f:field property="curp" input-class="form-control"/>
					<f:field property="rfc" input-class="form-control"/>
					<f:field property="activo" input-class="form-control" />
					<f:field property="controlDeAsistencia" input-class="form-control" />
                                        
                                        
                                        <f:field property="alta" input-class="form-control" input-id="alta"/>
                                        <%--
					<f:field property="alta">
                                            <jqueryPicker:date class="form-control" name="alta" value="${empleadoInstance.alta}" />
                                        </f:field>
                                        --%>
                                        
				</f:with>
			</fieldset>
			
			<div class="form-group">
		    	<div class="col-sm-offset-9 col-sm-2">
		      		<button type="submit" class="btn btn-default">
		      			<span class="glyphicon glyphicon-floppy-save"></span> Guardar
		      		</button>
		    	</div>
		  	</div>
		</g:form>

	</div>

	<div class="panel-footer"></div>
</div>