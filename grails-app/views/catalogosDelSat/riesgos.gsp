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
					<g:sortableColumn property="clave" title="Clave"/>
					<g:sortableColumn property="descripcion" title="Descripción"/>
				</tr>
			</thead>
			<tbody>
				<g:each in="${rows}" var="row">
					<tr>
						<td>${fieldValue(bean:row,field:"clave")}</td>
						<td>${fieldValue(bean:row,field:"descripcion")}</td>
					</tr>
				</g:each>
			</tbody>
		</table>
	</content>
</body>
</html>