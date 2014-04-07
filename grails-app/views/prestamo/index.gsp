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
  		<g:link class="btn btn-success" action="create">Alta</g:link>
	</content>
	<content tag="gridPanel">
		<g:render template="gridPanel"/>
	</content>
	
</body>
</html>