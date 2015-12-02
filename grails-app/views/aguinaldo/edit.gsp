<html>
<head>
	<meta charset="UTF-8">
	<title>Aguinaldo ${aguinaldoInstance.id}</title>
	<r:require module="forms"/>
</head>
<body>

	<div class="container">

		<div class="row">
			<div class="col-md-12">
				<div class="well">
					<h3>Aguinaldo de ${aguinaldoInstance.empleado} ${aguinaldoInstance.ejercicio} 
						(${g.formatDate(date:aguinaldoInstance.fechaInicial)} - ${g.formatDate(date:aguinaldoInstance.fechaFinal)})
						<p>
						<small>
							Salario diario: <g:formatNumber number="${aguinaldoInstance.salario}" type="currency"/> 
							Tipo: ${aguinaldoInstance.empleado.salario.periodicidad}
						</small>
					</h3>
					<g:if test="${ flash.message }">
						<span class="label label-warning text-center">${flash.message}</span>
					</g:if>
				</div>
			</div>
			<g:hasErrors bean="${aguinaldoInstance}">
	            <div class="alert alert-danger">
	                <g:renderErrors bean="${aguinaldoInstance}" as="list" />
	            </div>
	        </g:hasErrors>
		</div><!-- end .row 1 -->
		

		<div class="row">
			<div class="col-md-12">
				<div class="button-panel">
					<div class="btn-group">
						
					</div>
					
				</div>
			</div>
		</div><!-- end .row 2 -->

		<div class="row">
		
			<g:form  name="updateForm" class="form-horizontal" action="update" method="PUT">
			<div class="row">
			<div class="col-md-10">	
					<g:hiddenField name="id" value="${aguinaldoInstance.id}" />
					<g:hiddenField name="version" value="${aguinaldoInstance.version}" />
				
					<f:with bean="${aguinaldoInstance }">
						<fieldset disabled>
							<f:field property="fechaInicial" input-class="readOnly"/>
						</fieldset>
						<f:field property="porcentajeBono"  input-class="form-control bono" input-type="text"
							value ="${aguinaldoInstance.porcentajeBono*100}"/>
						<f:field property="faltas"  input-class="form-control" input-type="text"/>
						<f:field property="permisoEspecial"  input-class="form-control" input-type="text"/>
						<f:field property="incapacidades"  input-class="form-control" input-type="text"/>
						<f:field property="incapacidadesRTT"  input-class="form-control" input-type="text"/>
						<f:field property="incapacidadesRTE"  input-class="form-control" input-type="text"/>
						<f:field property="incapacidadesMAT"  input-class="form-control" input-type="text"/>
						<f:field property="manual"  input-class="form-control" />
					</f:with>
			</div>
			</div>	
				<div class="form-group">
					<div class="col-md-offset-2 col-sm-4">
						
						<g:link class="btn btn-default" action="show" 
							
							id="${ aguinaldoInstance.id}">Cancelar</g:link>
						<g:link action="index" class="btn btn-default">
							<span class="glyphicon glyphicon-arrow-left"></span> Aguinaldos
					    </g:link>
						<button type="submit" class="btn btn-default">
							<span class="glyphicon glyphicon-floppy-save"></span> Actualizar
						</button>
					</div>
				</div>

			</g:form>
			
		</div><!-- end .row 3 -->

		

	</div>
	
	<script type="text/javascript">
		$(function(){
			$("form[name='updateForm2']").submit(function(e){
			    
			    
			    //e.preventDefault(); //STOP default action
			});

			$(".bono").autoNumeric({vMin:'0.00',vMax:100.00});
		});
	</script>
	
	
	
</body>
</html>