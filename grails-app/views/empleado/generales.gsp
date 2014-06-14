<html>
<head>
	<meta charset="UTF-8">
	
</head>
<body>

<content tag="actions">
	
	<g:if test="${!edit}">
		%{-- <g:link action="update" class="btn btn-primary" id="${empleadoInstance.id}">Salvar</g:link> --}%
		<g:link class="btn btn-default" action="generales" id="${empleadoInstance.id}" params="[edit:'true']">Modificar</g:link>
	</g:if>
	<div class="btn-group">
				<button type="button" name="reportes" class="btn btn-default dropdown-toggle" data-toggle="dropdown" role="menu">Reportes <span class="caret"></span></button>
				<ul class="dropdown-menu">
					<li>
						<g:jasperReport
          						jasper="CaratulaDeEmpleados"
          						format="PDF"
          						name="Carátula">
    							<g:hiddenField name="ID" value="${empleadoInstance.id}"/>
 						</g:jasperReport>
					</li>
				</ul>
				</div>
</content>

<content tag="content">
	<form class="form-horizontal" method="post">
		<g:hiddenField name="id" value="${empleadoInstance.id}"/>
		<g:hiddenField name="version" value="${empleadoInstance?.version}" />
		
		<div class="col-md-6">
		
			<fieldset ${!edit?'disabled':''}>
				<f:with bean="empleadoInstance">
					<f:field property="apellidoPaterno" input-class="form-control" label="Apellido P."/>	
					<f:field property="apellidoMaterno" input-class="form-control" label="Apellido M."/>	
					<f:field property="nombres" input-class="form-control" />	
					<f:field property="fechaDeNacimiento" input-class="form-control" label="F.Nacimiento"/>	
					<f:field property="sexo" input-class="form-control" />	
					<f:field property="activo" input-class="form-control" />
					<f:field property="controlDeAsistencia" input-class="form-control" />
				</f:with>
			</fieldset>
		
		</div>
		
		<div class="col-md-6">
		
			<fieldset ${!edit?'disabled=""':''}>
				<f:with bean="empleadoInstance">
						
					<f:field property="curp" input-class="form-control" input-autocomplete="off"/>	
					<f:field property="rfc" input-class="form-control" input-autocomplete="off"/>	
					<f:field property="clave" input-class="form-control" input-autocomplete="off"/>	
					<f:field property="status" input-class="form-control" />
					<f:field property="alta" input-class="form-control" />
						
				</f:with>
			</fieldset>
			
			
			
			<fieldset ${!edit?'disabled=""':''}>
				
				<f:with bean="${empleadoInstance}">
					<f:field property="baja.fecha"  input-class="form-control" label="Fecha B.">
						<g:field type="text" name="baja.fecha" id="bajaFecha" class="form-control" 
							value="${g.formatDate(date:empleadoInstance?.baja?.fecha,format:'dd/MM/yyyy') }"/>
					</f:field>	
					<f:field property="baja.motivo" input-class="form-control" />
					<f:field property="baja.causa" input-class="form-control" />
					<f:field property="baja.comentario" input-class="form-control" label="Comentario"/>	
				</f:with>
			</fieldset>
			
		</div>
	
	<g:if test="${edit}">
	<div class="form-group">
    	<div class="col-sm-offset-8 col-sm-12">
      		<g:actionSubmit class="btn btn-primary"  value="Actualizar" action="update"/>
      		<g:link class="btn btn-default" action="generales" id="${empleadoInstance.id}" >Cancelar</g:link>
    	</div>
	</div>
	</g:if>

	

	</form>
</content>
<r:script>
    /** JavaScrip para date picker **/
   $("#bajaFecha").datepicker({
        //showOn: "both",
        changeMonth: true,
        changeYear: true,
        appendText: "",
        showAnim: "fold",
        showButtonPanel: true,
        dateFormat:"dd/mm/yy" 
    });
    
</r:script>
	
</body>
</html>