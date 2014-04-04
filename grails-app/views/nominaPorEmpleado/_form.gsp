<%@page expressionCodec="none" %>
<div class="row">
	<div class="col-md-7">
		<g:form class="form-horizontal">
			<f:with bean="${nominaPorEmpleadoInstance}">
				<g:hiddenField name="id" value="${nominaPorEmpleadoInstance?.id }"/>
				<f:field property="salarioDiarioBase" label="S. Base" input-class="form-control"/>
				<f:field property="salarioDiarioIntegrado" label="SDI" input-class="form-control"/>
				<f:field property="ubicacion"/>
				<f:field property="comentario" input-class="form-control col-lg-6"/>
				<f:field property="antiguedadEnSemanas" input-disabled="" input-class="form-control"/>
			</f:with>
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
		</g:form>
	</div>
</div>
