<g:applyLayout name="application">
<html>
<head>
	<title><g:layoutTitle/></title>
	<g:layoutHead/>
</head>
</html>
<body>

	<%-- Layout para altas consultas y modificaciones de operaciones especificas --%>

	<div class="container">
	
		<%-- Header/Banner panel 'page.header' --%>
		<g:if test="${ pageProperty(name:'page.header') }">
			
			<div class="row">
				<div class="col-md-12">
					<div class="page-header">
						<g:pageProperty name="page.header"/>
						<g:if test="${ flash.message }">
							<span class="label label-warning text-center">${flash.message}</span>
						</g:if>
					</div>
				</div>
			</div><!-- end .row 1-->

		</g:if>
		
		<%-- Concent panel --%>
		<div class="row">
			<div class="col-md-3">
				<div class="panel panel-primary">
					<div class="panel-heading">Operaciones</div>
					<div class="task-panel">
						<g:pageProperty name="page.operaciones"/>
					</div>
				</div>
			</div>
			
			<div class="col-md-9">
				<g:if test="${ pageProperty(name:'page.documentPanel') }">
					<g:pageProperty name="page.documentPanel"/>
				</g:if>
				
				<g:else>
					<div class="panel panel-default">
					<div class="panel-heading text-center">
						<g:if test="${ pageProperty(name:'page.formTitle') }">
							<g:pageProperty name="page.formTitle"/>
						</g:if>
						<g:else>Entidad</g:else>
					</div>
					<div class="panel-body">
						<g:pageProperty name="page.form"/>
					</div>
					<div class="grid-panel">
						<g:pageProperty name="page.gridPanel"/>
					</div>
				</div>
				
				</g:else>
				
			</div>
			
		</div> <!-- end row 2

		
		
	</div><!-- End .container-->

</body>
</g:applyLayout>