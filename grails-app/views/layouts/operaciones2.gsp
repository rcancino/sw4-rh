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
		
		<%-- Content panel --%>
		<div class="row">
			
			<!-- Task panel -->
			<div class="col-md-2">
				<div class="panel panel-default">
					<div class="panel-heading">Consultas</div>
					<div class="task-panel">
						
					</div>
					<g:pageProperty name="page.consultas"/>
				</div>
			</div><!-- end Task panel -->
			
			<!-- Content panel -->
			<div class="col-md-10">
				<div class="panel panel-default">
					<div class="panel-heading text-center">
						<g:if test="${ pageProperty(name:'page.gridTitle') }">
							<g:pageProperty name="page.gridTitle"/>
						</g:if>
						<g:else>Listado</g:else>
					</div>
					
					<g:if test="${ pageProperty(name:'page.toolbarPanel') }">
						<g:pageProperty name="page.toolbarPanel"/>
					</g:if>
					<g:else>  toolbarPanel pendiente</g:else>

					<g:if test="${ pageProperty(name:'page.panelBody') }">
						<g:pageProperty name="page.panelBody"/>
					</g:if>
					<g:else>  panelBody pendiente</g:else>
					
					
				</div>
			</div><!-- end Content panel -->

		</div> <!-- end .row 2 Content panel -->

		
		
	</div><!-- End .container-->

</body>
</g:applyLayout>