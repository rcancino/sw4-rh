<g:applyLayout name="application">
<html>
<head>
	<meta charset="UTF-8">
	<title>Empleado: ${empleadoInstance?.perfil?.numeroDeTrabajador}</title>
</head>
<body>

	<div class="container-fluid">

		
		<div class="row">
			
			<!-- Sidebar -->
			<div class="col-md-2">
				<div class="panel panel-default">
					<div class="panel-heading">Consultas</div>

					<%--<nav:menu class="nav nav-pills nav-stacked" 
						scope="app/catalogos/empleado"/>
				--%>
					<nav:menu class="nav nav-pills nav-stacked"  scope="app/catalogos/empleado" custom="true">
	    				<li class="${active?'active':''}">
	    					
	        					<p:callTag tag="g:link"
	                   				attrs="${linkArgs + [class:active ? 'active' : '',id:empleadoInstance.id]}">
	               					<nav:title item="${item}"/>
	        					</p:callTag>
	        			</li>
	    				
					</nav:menu>
				</div>
			</div>
			
			<!--content panel -->
			<div class="col-md-10">

				<g:if test="${flash.message}">
					<div class="alert alert-warning">
						<div class="message" role="status">
								<strong>${flash.message}</strong>
							</div>
					</div>
				</g:if>
				<g:hasErrors bean="${empleadoInstance}">
						<div class="alert alert-danger alert-dismissable">
							<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
							<ul>
								<g:eachError var="err" bean="${empleadoInstance}">
									<li><g:message error="${err}"/></li>
								</g:eachError>
							</ul>
						</div>
					</g:hasErrors>
				<div class="panel panel-default">
					<div class="panel-heading">
						<g:pageProperty name="page.contentTitle"/>
						<div class="btn-group">
							<g:pageProperty name="page.actions"/>
						</div>
					</div>
					<div class="panel-body">
						<g:pageProperty name="page.content"/>
					</div>

					
					
				</div>
			</div>

		</div><!-- End .row1-->

		

	</div>
	
	
</body>
</html>
</g:applyLayout>