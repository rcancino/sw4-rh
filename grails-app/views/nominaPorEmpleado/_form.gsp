<%@page expressionCodec="none" %>
<div class="row">
	<div class="col-md-7">
		<g:form class="form-horizontal">
			<f:with bean="${nominaPorEmpleadoInstance}">
				<g:hiddenField name="id" value="${nominaPorEmpleadoInstance?.id }"/>
				<f:field property="salarioDiarioBase" label="S. Base" input-class="form-control" input-disabled=""/>
				<f:field property="salarioDiarioIntegrado" label="SDI" input-class="form-control"/>
				<f:field property="faltas" input-class="form-control"/>
				<f:field property="incapacidades" input-class="form-control" label="Inc"/>
				<%-- 
				<f:field property="incapacidades" input-class="form-control" label="Inca"/>
				<f:field property="comentario" input-class="form-control"/>
				--%>
				
			</f:with>
			<div class="form-group">
    			<div class="col-sm-offset-8 col-sm-12">
      				<g:actionSubmit class="btn btn-primary"  value="Actualizar" action="update"/>
    			</div>
			</div>
		</g:form>
	</div>
	
	<div class="col-md-5">
		<g:form class="form-horizontal">
			<div class="form-group">
   				<label class="col-sm-4 control-label">Percepciones</label>
    			<div class="col-sm-8">
      				<p class="form-control-static">
      					<g:formatNumber number="${nominaPorEmpleadoInstance?.percepciones }" format="###.##" type="currency"/>
      				</p>
    			</div>
  			</div>
  			<div class="form-group">
   				<label class="col-sm-4 control-label">Deducciones</label>
    			<div class="col-sm-8">
      				<p class="form-control-static">
      					<g:formatNumber number="${nominaPorEmpleadoInstance?.deducciones }" format="###.##" type="currency"/>
      				</p>
    			</div>
  			</div>
  			
  			<div class="form-group">
   				<label class="col-sm-4 control-label">Total</label>
    			<div class="col-sm-8">
      				<p class="form-control-static">
      					<g:formatNumber number="${nominaPorEmpleadoInstance?.total }" format="###.##" type="currency"/>
      				</p>
    			</div>
  			</div>
  			
  			<div class="form-group">
   				<label class="col-sm-4 control-label">Subsidio</label>
    			<div class="col-sm-8">
      				<p class="form-control-static">
      					<g:formatNumber number="${nominaPorEmpleadoInstance?.subsidioEmpleoAplicado}" format="###.##" type="currency"/>
      				</p>
    			</div>
  			</div>
  			
  			<div class="form-group">
   				<label class="col-sm-4 control-label">Antiguedad</label>
    			<div class="col-sm-8">
      				<p class="form-control-static">
      					<g:formatNumber number="${nominaPorEmpleadoInstance?.antiguedadEnSemanas}" format="###" />
      				</p>
    			</div>
  			</div>
  			
  			
			
		</g:form>
	</div>
</div>
