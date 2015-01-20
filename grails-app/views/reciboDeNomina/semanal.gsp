<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Recibos de nómina</title>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-header">
				<h2>Recibos de nómina <strong>${session.ejercicio}</strong></h2>
				</div>
			</div>
			
		</div>

		<div class="row">
			<div class="col-md-3">
				<div class="alert alert-info">
					<h4 class="text-center">Nomina</h4>
				</div>
				<div class="panel-group" id="accordion">
					<g:each in="${com.luxsoft.sw4.Mes.meses}" var="mes">
					<div class="panel panel-default">
						
							<div class="panel-heading">
							<h4 class="panel-title">
								<a href="#${mes.nombre}" data-toggle="collapse" data-parent="#accordion">${mes.nombre}</a>
							</h4>
						</div>
						<div class="panell-collapse collapse ${mesInstance==mes.nombre?'in':'' }" id="${mes.nombre}">
							<div class="panel-body">
								<ul class="nav nav-pills nav-stacked">
									<g:each in="${nominasPorMesInstanceMap[mes.nombre]}" var="nomina">
										<li class="${nomina.id==nominaInstance?.id?'active':'' }">
											<g:link action='semanal' params="[nominaId:nomina.id,mesInstance:mes.nombre]" >Folio: ${nomina.folio} (${nomina.tipo}/ ${nomina.formaDePago })</g:link>
										</li>
									</g:each>
								</ul>
								
							</div>
						</div>
						
					</div>
					</g:each>
				</div>
			</div>
			<div class="col-md-9">
				<div class="alert alert-info">
					<h4 class="text-center">Recibos</h4>
				</div>
				<g:render template="grid"/>
			</div>
		</div>

	</div>
</body>
</html>