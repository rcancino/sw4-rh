<html>
<head>
	<meta charset="UTF-8">
	<title>Control de vacaciones</title>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="well col-md-12">
				<h3>Control de vacaciones: ${controlDeVacacionesInstance?.empleado}</h3>
				<g:if test="${flash.message}">
                     <div class="col-md-offset-4">
                         <span class="label label-warning">${flash.message}</span>
                     </div>
                </g:if> 
			</div>
		</div>
		
		<div class="row">
			
			<div class="col-md-6">
				<form class="form-horizontal">
					<fieldset disabled>
					
					<div class="form-group">
						<label class="control-label col-sm-4">Acumulado Excento</label>
						<div class="col-sm-6">
							<input type="text" class="form-control"
								value="${controlDeVacacionesInstance.acumuladoExcento}" />
						</div>
					</div>
					
					<div class="form-group">
						<label class="control-label col-sm-4">Acumulado Gravado</label>
						<div class="col-sm-6">
							<input type="text" class="form-control"
								value="${controlDeVacacionesInstance.acumuladoGravado}" />
						</div>
					</div>
					
					<fieldset><legend>Antigüedad</legend>
					
					<div class="form-group">
						<label class="control-label col-sm-4">Dias</label>
						<div class="col-sm-6">
							<input type="text" class="form-control"
								value="${controlDeVacacionesInstance.antiguedadDias}" />
						</div>
					</div>
					
					<div class="form-group">
						<label class="control-label col-sm-4">Años</label>
						<div class="col-sm-6">
							<input type="text" class="form-control"
								value="${controlDeVacacionesInstance.antiguedadYears}" />
						</div>
					</div>
					
					
					
					<div class="form-group">
						<label class="control-label col-sm-4">Aniversario</label>
						<div class="col-sm-6">
							<input type="text" class="form-control"
								value="${formatDate(date:controlDeVacacionesInstance.aniversario,format:'dd/MM/yyyy')}" />
						</div>
					</div>
					
					</fieldset>
					
					</fieldset>
					<div class="form-group">
						<div class="col-sm-4 col-sm-offset-4">
							<g:link class="btn btn-default" action="index"> Regresar</g:link>
						</div>
					</div>
				</form>
			</div>
			
			
			<div class="col-md-6">
				<form class="form-horizontal">
				<fieldset disabled>
					
					<div class="form-group">
						<label class="control-label col-sm-4">Dias trasladados (${controlDeVacacionesInstance.ejercicio-1})</label>
						<div class="col-sm-6">
							<input type="text" class="form-control"
								value="${controlDeVacacionesInstance.diasTrasladados}" />
						</div>
					</div>
					
					<div class="form-group">
						<label class="control-label col-sm-4">Otorgadas (${controlDeVacacionesInstance.ejercicio})</label>
						<div class="col-sm-6">
							<input type="text" class="form-control"
								value="${controlDeVacacionesInstance.diasVacaciones}" />
						</div>
					</div>
					
					<div class="form-group">
						<label class="control-label col-sm-4">Dias tomados</label>
						<div class="col-sm-6">
							<input type="text" class="form-control"
								value="${controlDeVacacionesInstance.diasTomados}" />
						</div>
					</div>
					
					<div class="form-group">
						<label class="control-label col-sm-4">Dias disponibles</label>
						<div class="col-sm-6">
							<input type="text" class="form-control"
								value="${controlDeVacacionesInstance.diasDisponibles}" />
						</div>
					</div>
					
					
					<div class="form-group">
						<label class="control-label col-sm-4">Vigencia</label>
						<div class="col-sm-6">
							<input type="text" class="form-control"
								value="${formatDate(date:controlDeVacacionesInstance.getVigencia(),format:'dd/MM/yyyy')}" />
						</div>
					</div>
					
					
					
					
				</fieldset>
					<div class="form-group">
						<div class="col-sm-4 col-sm-offset-4">
							<g:link class="btn btn-warning" action="actualizar" id="${controlDeVacacionesInstance.id }">Actualizar</g:link>
						</div>
					</div>
				</form>
			</div>
			
		</div>
		
		<div class="row">
			<div class="col-md-6">
				<div class="page-header">	
					<h3> Percepciones de primas vacacionales</h3>
				</div>
				<g:render template="primasVacacionalesGrid"/>
			</div>
		</div>
		
	</div><%-- End .container --%>
	
	
	
	
</body>
</html>