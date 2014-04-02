
	<div class="row">
		<div class="col-md-12">
			<div class="btn-group">
				<button class="btn btn-default">Cargar</button>
				<button class="btn btn-default">Opcion 2</button>
				<button class="btn btn-default">Opcion 3</button>
				<button class="btn btn-default">Opcion 4</button>
			</div>
		</div>
		
	</div>





<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="folio" title="folio"/>
			<g:sortableColumn property="empresa.clave" title="Empresa"/>
			<th>
				<g:message code="nomina.tipo.label" default="Tipo" encodeAs="html"/>
			</th>
			<g:sortableColumn property="periodo.fechaInicial" title="folio"/>
			<th><g:message code="periodo.fechaFinal" encodaAs="html"/></th>
			
		</tr>
	</thead>
	<tbody>
		<g:each in="${nominaInstanceList}" var="row">
			<tr>
				<td>
					<g:link action="show" id="${row.id}">
						<g:formatNumber number="${row.folio}" format="######"/>
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empresa.clave")}</td>
				<td>${fieldValue(bean:row,field:"tipo")}</td>
				<td>${fieldValue(bean:row.periodo,field:"fechaInicial")}</td>
				<td>${fieldValue(bean:row.periodo,field:"fechaFinal")}</td>
				
			</tr>
		</g:each>
	</tbody>
</table>
