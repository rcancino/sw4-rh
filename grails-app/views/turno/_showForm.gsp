<%@page expressionCodec="none" %>

<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">Alta de turno</h3>
	</div>
	<div class="panel-body">
	
		<g:form action="save" role="form" class="form-horizontal" >
			<div class="well">
				<div class="form-group">
					<label class="col-sm-3 control-label">Descripción</label>
				    <div class="col-sm-9">
				      <p class="form-control-static">${turnoInstance.descripcion}</p>
				    </div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">Inicio día</label>
				    <div class="col-sm-4">
				    	<p class="form-control-static">
				    		<joda:format value="${turnoInstance.inicioDeDia}"/>
				    		
				    	</p>
				    </div>
				    <div class="col-sm-3">
				    	      Día siguiente
				    		<g:checkBox class="" name="horaLimiteSiguienteDia" value="${turnoInstance.horaLimiteSiguienteDia}" disabled="true"/> 
				    </div>
				    
				    
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">Hora límite de trabajo</label>
				    <div class="col-sm-9">
				      <p class="form-control-static"><joda:format value="${turnoInstance.horaLimiteDeTrabajo}"/></p>
				    </div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">Inicio de tiempo extra</label>
				    <div class="col-sm-9">
				      <p class="form-control-static"><joda:format value="${turnoInstance.inicioDeTiempoExtra}"/></p>
				    </div>
				</div>

			</div>
			<div class="panel-header">
				<h3>Días</h3>
				<table class="table table-striped table-bordered table-condensed">
					<thead>
						<tr >
							<th class="text-center">Día</th>
							<th class="text-center" colspan="2">Grupo 1</th>
							<th class="text-center" colspan="2">Grupo 2</th>
						</tr>
					</thead>
					
					<tbody>
						<g:each in="${turnoInstance.dias}" var="row">
						<tr>
							<td>${row.dia}</td>
							<td><joda:format value="${row.entrada1}"/></td>
							<td><joda:format value="${row.salida1}"></joda:format></td>
							<td><joda:format value="${row.entrada2}"></joda:format></td>
							<td><joda:format value="${row.salida2}"></joda:format></td>
							
						</tr>
						</g:each>
					</tbody>
				</table>
			</div>
			
		</g:form>
	</div>
	
</div>

