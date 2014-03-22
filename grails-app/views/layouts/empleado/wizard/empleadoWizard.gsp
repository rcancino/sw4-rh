<g:applyLayout name="application">
	<!DOCTYPE html> <!-- Start page -->
	<html>
	<head>
		<title><g:layoutTitle/></title>
		<g:layoutHead/>
	</head>
	</html>
	<body>
		<div class="container">
			<div class="row">
				<div class="col-md-12">
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
							<g:pageProperty name="page.heading"/>
						</div>
						<div class="panel-body">
							<g:pageProperty name="page.form"/>
						</div>
						
					</div>
				</div>
			</div>

			
			
		</div>
	</body>
</g:applyLayout>