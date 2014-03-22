<html>
<head>
	<meta charset="UTF-8">
	
	<title>Conceptos</title>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="alert alert-info">
					<h3>${"Conceptos de nómina"}</h3>
					<g:render template="/_common/alertMessage"/>
				</div>
				<nav:menu scope="app/catalogos/conceptos" class="nav nav-tabs"/> 

				%{-- <ul class="nav nav-tabs">
					<li class="active"><a href="#percepciones" data-toggle="tab">Percepciones</a></li>
					<li><a href="#deducciones" data-toggle="tab">Deducciones</a></li>
				</ul>
				<div class="tab-content">
					<div class="tab-pane active fade" id="percepciones">
						<h2>Lista de precepciones</h2>
					</div>
					<div class="tab-pane fade" id="deducciones">
						<h2>Lista de deducciones</h2>
					</div>
				</div> --}%
			</div>
		</div>
	</div>
		
	</content>
</body>
</html>