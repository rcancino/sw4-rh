<%@ page import="com.luxsoft.sw4.rh.Nomina" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="dashboard_1"/>
	<title>Nomina</title>
</head>
<body>
	
	<content tag="header">
		<g:link action="index" id="${nominaInstance.id}" params="[periodicidad:nominaInstance.periodicidad]">
			<h4>Nómina: ${nominaInstance.folio} ${nominaInstance.periodicidad} (${nominaInstance.periodo}) </h4>
		</g:link>
	</content>
	
	<content tag="buttonBar">
	
		<div class="button-panel">
			<div class="btn-group">
			<g:link action="index" class="btn btn-default">
				<span class="glyphicon glyphicon-repeat"></span> Refrescar
			</g:link>
			
			<g:link action="create" class="btn btn-default">
				<span class="glyphicon glyphicon-search"></span> Buscar
			</g:link>
			<g:link action="create" class="btn btn-default">
				<span class="glyphicon glyphicon-filter"></span> Filtrar
			</g:link>
			</div>
			
			<div class="btn-group">
				<button type="button" name="reportes" class="btn btn-default dropdown-toggle" data-toggle="dropdown" role="menu">Reportes <span class="caret"></span></button>
				<ul class="dropdown-menu">
					<li>
						<g:jasperReport
          						jasper="NominaCaratula"
          						format="PDF"
          						name="Carátula">
    							<g:hiddenField name="NOMINA" value="${nominaInstance.id}"/>
 						</g:jasperReport>
					</li>
					<li>
						<g:jasperReport
          						jasper="Nomina"
          						format="PDF"
          						name="Detalle">
    							<g:hiddenField name="NOMINA" value="${nominaInstance.id}"/>
 						</g:jasperReport>
					</li>
					<g:if test="${['QUINCENAL','SEMANAL'].contains(nominaInstance.periodicidad) }">
						
						
						<li>
							<g:jasperReport
          						jasper="${nominaInstance.periodicidad=='QUINCENAL'?'NominaCaratulaQ':'NominaCaratulaS' }"
          						format="PDF"
          						name="Resumen">
    							<g:hiddenField name="NOMINA" value="${nominaInstance.id}"/>
 							 </g:jasperReport>
						</li>
						
					</g:if>
				</ul>
			</div> <!-- Fin .btn-group -->
			
			<div class="btn-group">
				<button type="button" name="reportes" class="btn btn-default dropdown-toggle" data-toggle="dropdown" >Operaciones <span class="caret"></span></button>
				<ul class="dropdown-menu">
					<li>
						<g:link controller="procesadorDeNomina" action="generarPlantilla" id="${nominaInstance.id}"> 
							<span class="glyphicon glyphicon-cog"></span> Generar
						</g:link> 
					</li>
					<li>
						<g:link action="delete" id="${nominaInstance.id}" onclick="return confirm('Eliminar toda la nomina?');"> 
							<span class="glyphicon glyphicon-trash"></span> Eliminar
						</g:link> 
					</li>
					<li>
						<g:link action="timbrar" id="${nominaInstance.id}" onclick="return confirm('Timbrar toda la nomina?');"> 
							 Timbrar
						</g:link> 
					</li>
				</ul>
			</div> <!-- Fin .btn-group -->
			
		</div>
		
	</content>
	<content tag="grid">
		<g:render template="nominaDetGridPanel"/>
	</content>

	

</body>
</html>
