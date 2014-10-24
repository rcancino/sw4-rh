
<%@ page import="com.luxsoft.sw4.rh.Nomina" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Nóminas</title>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="alert alert-info">
					<h3>Lista de nóminas ${tipo=='ESPECIAL'?tipo:periodicidad }</h3>
				</div>
			</div>
		</div>

		<div class="row">
			<div class="col-md-12">
				<ul class="nav nav-tabs">
					<li class="${(periodicidad=='QUINCENAL' && tipo=='GENERAL')?'active':''}">
						<g:link action="index" params="[periodicidad:'QUINCENAL',tipo:'GENERAL'	]" >Quincenal</g:link>
					</li>
					<li class="${(periodicidad=='SEMANAL'&& tipo=='GENERAL')?'active':''}">
						<g:link action="index" params="[periodicidad:'SEMANAL',tipo:'GENERAL']">Semanal</g:link>
					</li>
					<li class="${tipo=='ESPECIAL'?'active':''}">
						<g:link action="index" params="[tipo:'ESPECIAL']">Especial</g:link>
					</li>
				</ul>
			</div>
		</div>

		<div class="row">
			<div class="col-md-12">
				<div class="button-panel">
							<div class="btn-group">
							<g:link action="index" class="btn btn-default">
								<span class="glyphicon glyphicon-repeat"></span> Refrescar
							</g:link>
							
							<button class="btn btn-default" data-toggle="modal" data-target="#agregarNominaForm">
								<span class="glyphicon glyphicon-calendar"></span> Agregar
							</button>
							<g:link action="buscar" class="btn btn-default" >
								<span class="glyphicon glyphicon-search"></span> Buscar
							</g:link>
							<g:link action="filtrar" class="btn btn-default" >
								<span class="glyphicon glyphicon-filter"></span> Filtrar
							</g:link>
							<g:link action="importar" class="btn btn-default" params="[periodicidad:periodicidad]" 
								>
								<span class="glyphicon glyphicon-filter"></span> Importar
							</g:link>
							</div>
						</div>
				<g:render template="nominaGridPanel"/>
			</div>
		</div>
		
		<g:render template="agregarDialog"/>

	</div>
	
</body>
</html>
