<html>
<head>
	<meta charset="UTF-8">
	<title>Incentivo ${incentivoInstance.id}</title>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="alert alert-info">
				<h4>${incentivoInstance.empleado} Tipo:${incentivoInstance.tipo} </h4>
				<g:if test="${incentivoInstance.tipo=='MENSUAL'}">
					<h5>${incentivoInstance?.fechaInicial?.format('dd/MM/yyyy')} al 
					${incentivoInstance?.fechaFinal?.format('dd/MM/yyyy')}</h5>
				</g:if>
				<g:else>
					<h5>${incentivoInstance.asistencia.periodo.fechaInicial?.format('dd/MM/yyyy')} al 
					${incentivoInstance.asistencia.periodo.fechaFinal?.format('dd/MM/yyyy')}</h5>
				</g:else>
				

			</div>
			<g:hasErrors bean="${incentivoInstance}">
				<div class="alert alert-danger">
					<g:renderErrors bean="${incentivoInstance}" as="list" />
				</div>
			</g:hasErrors>
		</div>

		<div class="row">
			<div class="col-md-2">
				<div class="panel panel-default">
  					<div class="panel-heading">Operaciones</div>
    				<div class="list-group">
    					<g:link action="recalcular" class="list-group-item" id="${incentivoInstance.id}">
    						<span class="glyphicon glyphicon-wrench"></span> Re calcular
    					</g:link>
    				</div>
				</div>
			</div>
			<div class="col-md-6">
				<g:form action="update" id="${incentivoInstance.id}" class="form-horizontal">
					<f:with bean="${incentivoInstance}">
						<f:field property="otorgado" input-class="form-control"/>
						<f:field property="calificacion" input-class="form-control"/>
						<f:field property="tasaBono1" input-class="form-control" input-type="text"
							label="${incentivoInstance.tipo=='MENSUAL'?'Autorizado':'Entrada'}"/>
						<f:field property="tasaBono2" input-class="form-control" input-type="text"
							label="${incentivoInstance.tipo=='MENSUAL'?'Asignado':'Comida'}"/>
					</f:with>
					
					<div class="form-group">
						<div class="col-md-offset-2 col-sm-10">
							<g:link class="btn btn-default" action="index" params="[tipo:incentivoInstance.tipo]"> Cancelar</g:link>
							<g:submitButton name="Salvar" class="btn btn-primary " />
						</div>
					</div>
				</g:form>
			</div>
			
			<div class="col-md-4">
				<form action="" class="form-horizontal">
					<div class="form-group">
    					<label class="col-sm-3 control-label">Faltas</label>
    					<div class="col-sm-9">
      						<p class="form-control-static">${incentivoInstance.faltas}</p>
    					</div>
  					</div>
  					<div class="form-group">
    					<label class="col-sm-3 control-label">Minutos</label>
    					<div class="col-sm-9">
      						<p class="form-control-static">${incentivoInstance.minutosNoLaborados}</p>
    					</div>
  					</div>
  					<div class="form-group">
    					<label class="col-sm-3 control-label">Chec F</label>
    					<div class="col-sm-9">
      						<p class="form-control-static">${incentivoInstance.checadasFaltantes}</p>
    					</div>
  					</div>
				</form>
			</div>
			
		</div> <!-- end .row 1 -->

		<div class="row">
			<div class="col-md-12">
				<legend>Días</legend>
				
				<table  class="table table-striped table-bordered table-condensed incentivoGrid ">
					<thead>
						<tr>
							<th>Fecha</th>
							<th><g:message code="asistenciaDet.entrada1.label" default="Ent 1"/></th>
							<th><g:message code="asistenciaDet.salida1.label" default="Sal 1"/></th>
							<th><g:message code="asistenciaDet.entrada2.label" default="Ent 2"/></th>
							<th><g:message code="asistenciaDet.salida2.label" default="Sal 2"/></th>
							<th><g:message code="asistenciaDet.retardoMenor.label" default="Ret men"/></th>
							<th><g:message code="asistenciaDet.retardoMayor.label" default="Ret may"/></th>
							<th>RMen(Com)</th>
							<th>RMay(Com)</th>
							<th><g:message code="asistenciaDet.retardoComida.label" default="Min NL"/></th>
							<th><g:message code="asistenciaDet.retardoComida.label" default="Hrs Tr"/></th>
							<th><g:message code="asistenciaDet.tipo.label" default="Tipo"/></th>
						</tr>
					</thead>
					<tbody>
						<g:each in="${asistencias}" var="row">
							<tr>
								<td id="${row.id}">
									<g:link controller="asistencia" action="edit" id="${row.asistencia.id}">
										<g:formatDate date="${row.fecha}" format="dd/MM/yyyy"/>
									</g:link>
									<td><g:formatDate date="${row.entrada1}" format="HH:mm"/></td>
									<td><g:formatDate date="${row.salida1}" format="HH:mm"/></td>
									<td><g:formatDate date="${row.entrada2}" format="HH:mm"/></td>
									<td><g:formatDate date="${row.salida2}" format="HH:mm"/></td>
									<td><g:fieldValue bean="${row}" field="retardoMenor"/> </td>
									<td><g:fieldValue bean="${row}" field="retardoMayor"/> </td>
									<td><g:fieldValue bean="${row}" field="retardoMenorComida"/> </td>
									<td><g:fieldValue bean="${row}" field="retardoComida"/> </td>
									<td><g:fieldValue bean="${row}" field="minutosNoLaborados"/> </td>
									<td><g:fieldValue bean="${row}" field="horasTrabajadas"/> </td>
									<td><g:fieldValue bean="${row}" field="tipo"/> </td>
								</td>
								
								
							</tr>
						</g:each>
					</tbody>
					<tfoot>
						<tr>
							<th></th>
							<th></th>
							<th></th>
							<th></th>
							<th>Totales:</th>
							<th><g:formatNumber number="${asistencias.sum (0.0,{it.retardoMenor}) }" format="###"/></th>
							<th><g:formatNumber number="${asistencias.sum (0.0,{it.retardoMayor}) }" format="###"/></th>
							<th><g:formatNumber number="${asistencias.sum (0.0,{it.retardoMenorComida}) }" format="###"/></th>
							<th><g:formatNumber number="${asistencias.sum (0.0,{it.retardoComida}) }" format="###"/></th>
							<th><g:formatNumber number="${asistencias.sum (0.0,{it.minutosNoLaborados}) }" format="###"/></th>
							<th><g:formatNumber number="${asistencias.sum (0.0,{it.horasTrabajadas}) }" format="###"/></th>
							<th><g:formatNumber number="${asistencias.count({it.tipo}) }" format="###"/> dias</th>
						</tr>
					</tfoot>
				</table>


			</div>
		</div><!-- end .row 2 -->

		
	</div>
	
	<r:script>
		
	</r:script>
	
</body>
</html>