<g:applyLayout name="application">
	<html>
	<head>
		<title><g:layoutTitle/></title>
		<g:layoutHead/>
	</head>
	</html>
	<body>
		<div class="container-fluid">
		
			<div class="row">
				<div class="col-md-12">
					<div class="alert alert-info">
						<g:pageProperty name="page.header"/>
						<g:if test="${flash.message}">
							<div class="alert alert-warning" role="status">
								<strong>${flash.message}</strong>
							</div>
						</g:if>
					</div>
				</div>
			</div><!-- end .row 1 (Header) -->
		
			<div class="row">
				<div class="col-md-12">
					<g:pageProperty name="page.buttonBar"/>
				</div>
			</div><!-- End .row 2 (Button bar) -->
			
			<div class="row">
				<div class="col-md-12">
					<g:pageProperty name="page.content"/>
				</div>
			</div><!-- End .row 3 (Content) -->
						
		</div><!-- End .container -->
		
	</body>
</g:applyLayout>