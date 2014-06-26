
<%@ page import="com.luxsoft.sw4.rh.Incentivo" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Incentivo</title>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="alert alert-info">
					<h3>Incentivos ${currentCalendario?.calendario?.tipo} ${currentCalendario?.folio}</h3>
				</div>
			</div>
		</div>

		<div class="row">
			<div class="col-md-12">
				<ul class="nav nav-tabs">
					<li class="${periodicidad=='SEMANA'?'active':''}"><g:link action="index" params="[periodicidad:'SEMANA']">Semana</g:link></li>
					<li class="${periodicidad=='QUINCENA'?'active':''}"><g:link action="index" params="[periodicidad:'QUINCENA']">Quincena</g:link></li>
					
					<li><a href="">Especial</a></li>
					
				</ul>
			</div>
		</div>

		<div class="row">
				<div class="button-panel">
					
					<div class="btn-group">
						<g:form action="index" class="form-inline" method="GET">
							<g:hiddenField name="periodicidad" value="${periodicidad}"/>
							<div class="form-group">
								<label for="periodoField" class="sr-only">Periodo</label>
								<g:select id="periodoField" class="form-control"  
									name="calendarioDetId" 
									value="${currentCalendario?.id}"
									from="${calendarios}" 
									optionKey="id" 
									optionValue="${{it.calendario.tipo+' '+it.folio+' ( '+it.inicio.format('yyyy')+ ' ) '}}"
									
								/>
							</div>
							
							<g:submitButton name="refresh" value="Refrescar" class="btn btn-default"></g:submitButton>
							<g:actionSubmit value="Generar" action="generarIncentivos" class="btn btn-default"/>	
					
						</g:form>
					</div>
				</div>
				
		</div>
		
		<div class="row">
			<g:render template="gridPanel"/>
		</div>

	</div>
	
</body>
</html>
