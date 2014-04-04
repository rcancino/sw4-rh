
<%@ page import="com.luxsoft.sw4.rh.Nomina" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="dashboard_1"/>
	<title>Empleados</title>
</head>
<body>
	<content tag="header">
		<h4>Listado de nóminas</h4>
	</content>
	<content tag="grid">
		<g:render template="nominaGridPanel"/>
	</content>
</body>
</html>
