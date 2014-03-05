<html>
<head>
	<meta charset="UTF-8">
	<title>Catálgos del SAT</title>
</head>
<body>
	<content tag="grid">
		<table class="table">
			<thead>
				<tr>
					<g:sortableColumn property="clave" title="Id SAT"/>
					<g:sortableColumn property="nombreCorto" title="Clave"/>
					<g:sortableColumn property="nombre" title="Nombre"/>
				</tr>
			</thead>
			<tbody>
				<g:each in="${rows}" var="row">
					<tr>
						<td>${fieldValue(bean:row,field:"clave")}</td>
						<td>${fieldValue(bean:row,field:"nombreCorto")}</td>
						<td>${fieldValue(bean:row,field:"nombre")}</td>
					</tr>
				</g:each>
			</tbody>
		</table>
	</content>
</body>
</html>