<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operaciones"/>
	<title>Prestamos</title>
</head>
<body>
	<content tag="header">
		<h3>Reglas de ejecuccion por nómina</h3>
	</content>
	<content tag="consultas">
		<nav:menu scope="app/configuracion/reglasDeEjecuccion" class="nav nav-pills nav-stacked" path=""/>
	</content>
	<content tag="gridTitle">Lista de reglas</content>
	<content tag="gridTasks">
  		<g:link class="btn btn-success" action="create">Alta</g:link>
	</content>
	<content tag="gridPanel">
<%--		<g:render template="gridPanel"/>--%>
	</content>
	
</body>
</html>