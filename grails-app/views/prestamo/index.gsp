<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operaciones"/>
	<title>Prestamos</title>
</head>
<body>
	<content tag="header">
		<h3>Prestamos a empleados</h3>
	</content>
	<content tag="consultas">
		<ul class="nav nav-pills nav-stacked">
  			<nav:menu scope="app/operaciones/prestamo" class="nav nav-pills nav-stacked" path=""/>
		</ul>
	</content>
	<content tag="gridTitle">Lista de prestamos</content>
	<content tag="gridTasks">

  		<div class="btn-group">
			<g:link action="index" class="btn btn-default">
				<span class="glyphicon glyphicon-repeat"></span> Todos
			</g:link>
			<g:link action="create" class="btn btn-default">
				<span class="glyphicon glyphicon-floppy-saved"></span> Nuevo
			</g:link>
			
			<button class="btn btn-default" data-toggle="modal" data-target="#searchForm">
				<span class="glyphicon glyphicon-search"></span> Buscar
			</button>
		</div>
		<div class="btn-group">
				<button type="button" name="reportes" class="btn btn-default dropdown-toggle" data-toggle="dropdown" role="menu">Reportes <span class="caret"></span></button>
				<ul class="dropdown-menu">
					<li>
						<g:jasperReport
          						jasper="PrestamosResumen"
          						format="PDF"
          						name="Resumen">
 						</g:jasperReport>
					</li>
					<li>
						<g:jasperReport
          						jasper="PrestamosDetalle"
          						format="PDF"
          						name="Detalle">
 						</g:jasperReport>
					</li>
					<li>
						<g:jasperReport
          						jasper="PrestamoHistorico"
          						format="PDF"
          						name="Histórico">
 						</g:jasperReport>
					</li>
					<li>
						<g:jasperReport
          						jasper="PrestamoAntiguedad"
          						format="PDF"
          						name="Antigüedad">
 						</g:jasperReport>
					</li>
				</ul>
			</div> <!-- Fin .btn-group -->
	</content>
	<content tag="gridPanel">
		<ul class="nav nav-tabs" role="tablist">
		  <li class="active">
		  	<a href="#semanal" role="tab" data-toggle="tab">Semanal</a>
		  </li>
		  <li >
		  	<a href="#quincenal" role="tab" data-toggle="tab">Quincenal</a>
		  </li>
		</ul>
		<div class="tab-content">
	  		<div class="tab-pane active" id="semanal">
				<g:render template="gridPanel" model="['partidasList':mapList.SEMANAL]"/>
	  		</div>
	  		<div class="tab-pane" id="quincenal">
				<g:render template="gridPanel" model="['partidasList':mapList.QUINCENAL]"/>
	  		</div>
		</div>
	</content>
	
</body>
</html>