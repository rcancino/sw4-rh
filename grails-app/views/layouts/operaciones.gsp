<g:applyLayout name="application">
<html>
<head>
	<title><g:layoutTitle/></title>
	<g:layoutHead/>
</head>
</html>
<body>

	<div class="container-fluid">
	
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
			<div class="col-md-2">
				<div class="panel panel-primary">
					<div class="panel-heading">Consultas</div>
					<div class="task-panel">
						<g:pageProperty name="page.consultas"/>
					</div>
				</div>
			</div>
			<div class="col-md-10">
				<div class="panel panel-default">
					<div class="panel-heading text-center">
						<g:if test="${ pageProperty(name:'page.gridTitle') }">
							<g:pageProperty name="page.gridTitle"/>
						</g:if>
						<g:else>Listado</g:else>
					</div>
					<div class="btn-group button-panel">
						<g:pageProperty name="page.gridTasks"/>
					</div>
					<div class="grid-panel">
						<g:pageProperty name="page.gridPanel"/>
					</div>
					
				</div>
			</div>
		</div> <!-- end row 2

		
		
	</div><!-- End .container-->

</body>
</g:applyLayout>