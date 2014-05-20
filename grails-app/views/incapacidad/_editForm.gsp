<%@page expressionCodec="none" %>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">Incapacidad ${incapacidadInstance.id}</h3>
	</div>
	<div class="panel-body">	
		
		<div class="row">
			<div class="col-md-8">
				<g:form action="update" role="form" class="form-horizontal" >
				<div class="form-group">
				<fieldset>
						<f:with bean="incapacidadInstance">
							<g:hiddenField name="id" value="${incapacidadInstance.id}"/>
							<f:field property="empleado" input-class="form-control" />
							<f:field property="referenciaImms" input-class="form-control" label="Ref IMMSS"/>
							<f:field property="tipo" input-class="form-control" />
							<f:field property="comentario" input-class="form-control" />
						</f:with>
					</fieldset>
		    			<div class="col-sm-offset-9 col-sm-2">
		      				<button type="submit" class="btn btn-default">
		      					<span class="glyphicon glyphicon-floppy-save"></span> Actualizar
		      				</button>
		    			</div>
		  			</div>
		</g:form>
			</div><!-- End col-md-8 -->
			
			<div class="col-md-4">
				<fieldset><legend>Fechas</legend>
				<ul>
					<g:each in="${incapacidadInstance.partidas}" var="row">
						<li><g:formatDate date="${row.fecha}" format="dd/MM/yyyy"/></li>
					</g:each>
				</ul>
				
				<g:form action="agregarFecha" class="form-inline" id="${incapacidadInstance.id}">
					<f:field bean="${new com.luxsoft.sw4.rh.IncapacidadDet()}" property="fecha" required="true"></f:field>
					<div class="form-group">
		    			<div class="col-sm-offset-4 col-sm-2">
		    				<br/>
		      				<button type="submit" class="btn btn-default">
		      					<span class="glyphicon glyphicon-floppy-save"></span> Agregar
		      				</button>
		    			</div>
		  			</div>
				</g:form>
				</fieldset>
				
			</div>
			
		</div><!-- End .row -->
		
	</div>
	<div class="panel-footer"></div>
</div>