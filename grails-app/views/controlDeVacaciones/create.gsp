<html>
<head>
	<meta charset="UTF-8">
	<title>Vacaciones</title>
	<r:require modules="datatables"/>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="page-header">
					<h3>Alta de control de vacaciones ${session.ejercicio }</h3>
					<g:if test="${flash.message}">
						<span class="label label-warning text-center">${flash.message}</span>
					</g:if>
				</div>
			</div>
		</div>

		<div class="row">
			<div class="col-md-12">
				<div class="button-panel">
					
				</div>
			</div>
		</div><!-- end .row 2 -->

		<div class="row">
			<div class="col-md-8">
				<g:form action="save" class="form-horizontal" >
					<fieldset>
						
					<f:with bean="${controlDeVacacionesInstance}">
						<f:field property="ejercicio" input-class="form-control" input-type="text" input-disabled="disabled"/>
						<f:field property="empleado" input-class="form-control" />
						
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
		</div>

	</div>
	
	<r:script>
			
	</r:script>
	
	
	
</body>
</html>

