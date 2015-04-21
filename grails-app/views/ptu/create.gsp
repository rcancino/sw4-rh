<html>
<head>
	<meta charset="UTF-8">
	<title>Alta de PTU</title>
	<r:require modules="forms"/>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="alert alert-info">
					<h3>Alta de  PTU </h3>
					<g:if test="${flash.message}">
						<div class="message" role="status">
							<strong>${flash.message}</strong>
						</div>
					</g:if>
				</div>
			</div>
		</div><!-- end .row 1 -->
		
		<div class="row">
			<div class="col-md-6">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">Parámetros</h3>
					</div>
					<div class="panel-body">	
						<g:hasErrors bean="${ptuInstance}">
							<div class="alert alert-danger alert-dismissable">
								<ul>
									<g:eachError var="err" bean="${ptuInstance}">
										<li><g:message error="${err}"/></li>
									</g:eachError>
								</ul>
							</div>
						</g:hasErrors>
						<g:form action="save" 
							role="form" class="form-horizontal" >
							<fieldset>
							<f:with bean="${ptuInstance}">
								<f:field property="ejercicio" 
									input-class="form-control" input-type="text" input-readonly='readonly'
									value="${ptuInstance.ejercicio}"/>
								<f:field property="remanente" input-class="form-control" input-type="text"/>
								<f:field property="monto" input-class="form-control" input-type="text"/>
								<f:field property="factor" input-class="form-control" input-type="text"/>
							</f:with>
							</fieldset>
							<div class="form-group">
						    	<div class="col-sm-offset-9 col-sm-2">
						      		<button type="submit" class="btn btn-default">
						      			<span class="glyphicon glyphicon-floppy-save"></span> Salvar
						      		</button>
						    	</div>
						  	</div>
						</g:form>
					</div>
					<div class="panel-footer"></div>
				</div>
			</div>
		</div>
		
		
	</div>
	
	<r:script>
		$(function(){
			$(".numerico").autoNumeric();
		});
	</r:script>
</body>
</html>