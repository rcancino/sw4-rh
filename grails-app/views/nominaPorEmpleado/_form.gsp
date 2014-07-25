<%@page expressionCodec="none" %>
<div class="row">
	<div class="col-md-7">
		<g:form class="form-horizontal">
			
			<div class="form-group">
   				<label class="col-sm-6 control-label">Salario Diario</label>
    			<div class="col-sm-6">
      				<p class="form-control-static">
      					<g:formatNumber number="${nominaPorEmpleadoInstance?.salarioDiarioBase}" format="###.##" type="currency"/>
      				</p>
    			</div>
  			</div>
  			
  			<div class="form-group">
   				<label class="col-sm-6 control-label">Salario Diario Integrado</label>
    			<div class="col-sm-6">
      				<p class="form-control-static">
      					<g:formatNumber number="${nominaPorEmpleadoInstance?.salarioDiarioIntegrado}" format="###.##" type="currency"/>
      				</p>
    			</div>
  			</div>
  			
  			<div class="form-group">
   				<label class="col-sm-6 control-label">Subsidio aplicado</label>
    			<div class="col-sm-6">
      				<p class="form-control-static">
      					<g:formatNumber number="${nominaPorEmpleadoInstance?.subsidioEmpleoAplicado}" format="###.##" type="currency"/>
      				</p>
    			</div>
  			</div>
		
			
      <g:if test="${!nominaPorEmpleadoInstance.cfdi}">
			<div class="form-group">
    			<%--<div class="col-sm-offset-8 col-sm-12">
      				<g:actionSubmit class="btn btn-primary"  value="Actualizar" action="update"/>
    			</div>
			--%></div>
      </g:if>
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
  			
  			<%--<div class="form-group">
   				<label class="col-sm-4 control-label">Subsidio</label>
    			<div class="col-sm-8">
      				<p class="form-control-static">
      					<g:formatNumber number="${nominaPorEmpleadoInstance?.subsidioEmpleoAplicado}" format="###.##" type="currency"/>
      				</p>
    			</div>
  			</div>
  			
  			--%><div class="form-group">
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
