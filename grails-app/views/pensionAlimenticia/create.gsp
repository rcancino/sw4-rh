<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="empleado"/>
	<r:require module="forms"/>
</head>
<body>

<content tag="contentTitle">
	Pensión alimenticia : ${empleadoInstance.nombre} (${empleadoInstance.id})
</content>
<content tag="actions">
	
	
</content>

<content tag="content">
	<div class="col-md-6">
	
	<g:if test="${pensionInstance}">
		<g:form  class="form-horizontal " action="salvar" >
		
			<f:with bean="${pensionInstance}">
				<g:hiddenField name="empleado.id" value="${empleadoInstance.id}"/>
				<f:field property="porcentaje" input-class="form-control" input-type="text" input-class="number"/>
				<f:field property="neto" input-class="form-control" />
				<f:field property="beneficiario" input-class="form-control" />
				<f:field property="comentario" input-class="form-control" />
				
			</f:with>
			
			<div class="form-group">
    			<div class="col-sm-offset-8 col-sm-12">
      				<g:actionSubmit class="btn btn-primary"  value="Salvar" />
    			</div>
			</div>
		</g:form>
		
	</g:if>
	</div>
	
	<r:script>
		$(function(){
			$(".number").autoNumeric({vMin:'0.00',vMax:99.99});
		});
	</r:script>
	
</content>
		

</body>
</html>